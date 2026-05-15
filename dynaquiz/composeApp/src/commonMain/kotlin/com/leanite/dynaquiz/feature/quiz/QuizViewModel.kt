package com.leanite.dynaquiz.feature.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leanite.dynaquiz.core.domain.model.AnswerLog
import com.leanite.dynaquiz.core.domain.model.AnswerOutcome
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.Question
import com.leanite.dynaquiz.core.domain.model.QuizSessionResult
import com.leanite.dynaquiz.core.domain.model.computeScore
import com.leanite.dynaquiz.core.domain.result.AppResult
import com.leanite.dynaquiz.core.domain.usecase.GetRandomQuestionUseCase
import com.leanite.dynaquiz.core.domain.usecase.SubmitAnswerUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class QuizViewModel(
    private val playerName: String,
    challengeMode: ChallengeMode,
    private val getRandomQuestionUseCase: GetRandomQuestionUseCase,
    private val submitAnswerUseCase: SubmitAnswerUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState(challengeMode = challengeMode))
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private val _events = Channel<QuizEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var timerJob: Job? = null

    fun onIntent(intent: QuizIntent) {
        when (intent) {
            QuizIntent.Started -> start()
            is QuizIntent.AnswerSelected -> onAnswerSelected(intent.answer)
            QuizIntent.BackPressed -> _uiState.update { it.copy(showExitDialog = true) }
            QuizIntent.ExitCancelled -> _uiState.update { it.copy(showExitDialog = false) }
            QuizIntent.ExitConfirmed -> {
                _uiState.update { it.copy(showExitDialog = false) }
                _events.trySend(QuizEvent.NavigateBack)
            }
        }
    }

    private fun start() {
        viewModelScope.launch {
            val firstQuestionDeferred = async { fetchQuestion() }
            runCountdown()
            val firstQuestion = firstQuestionDeferred.await()

            if (firstQuestion == null) {
                _events.send(QuizEvent.ShowMessage(QuizMessage.QuestionLoadFailed))
                _events.send(QuizEvent.NavigateBack)
                return@launch
            }

            startPlaying(firstQuestion)
        }
    }

    private suspend fun runCountdown() {
        for (sec in QuizRules.INITIAL_COUNTDOWN_SECONDS downTo 1) {
            _uiState.update { it.copy(phase = QuizPhase.Countdown(sec)) }
            delay(1.seconds)
        }
        _uiState.update { it.copy(phase = QuizPhase.Loading) }
    }

    private suspend fun fetchQuestion(): Question? =
        when (val result = getRandomQuestionUseCase()) {
            is AppResult.Success -> result.data
            is AppResult.Error -> null
        }

    private fun startPlaying(question: Question) {
        _uiState.update { it.copy(phase = QuizPhase.Playing(question = question)) }
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        val mode = _uiState.value.challengeMode
        if (mode !is ChallengeMode.Timed) {
            _uiState.update { it.copy(timeRemainingSec = null) }
            return
        }
        val totalSeconds = mode.perQuestionSeconds
        timerJob = viewModelScope.launch {
            for (sec in totalSeconds downTo 1) {
                _uiState.update { it.copy(timeRemainingSec = sec) }
                delay(1.seconds)
            }
            _uiState.update { it.copy(timeRemainingSec = 0) }
            onTimeOut()
        }
    }

    private fun onTimeOut() {
        val state = _uiState.value
        val playing = state.phase as? QuizPhase.Playing ?: return

        val log = AnswerLog(
            questionId = playing.question.id,
            chosenAnswer = null,
            timeRemainingSec = 0,
            outcome = AnswerOutcome.TimedOut,
        )

        viewModelScope.launch {
            advanceToNextQuestion(log)
        }
    }

    private fun onAnswerSelected(answer: String) {
        val state = _uiState.value
        val playing = state.phase as? QuizPhase.Playing ?: return
        if (playing.isSubmitting) return

        timerJob?.cancel()
        val timeRemaining = state.timeRemainingSec ?: 0

        _uiState.update {
            it.copy(phase = playing.copy(selectedAnswer = answer, isSubmitting = true))
        }

        viewModelScope.launch {
            val submitDeferred = async { submitAnswerUseCase(playing.question.id, answer) }
            val nextQuestionDeferred = async {
                val nextIndex = state.currentQuestionIndex + 1
                if (nextIndex < QuizRules.TOTAL_QUESTIONS) fetchQuestion() else null
            }

            val outcome = when (val r = submitDeferred.await()) {
                is AppResult.Success -> AnswerOutcome.Confirmed(correct = r.data.correct)
                is AppResult.Error -> {
                    _events.send(QuizEvent.ShowMessage(QuizMessage.AnswerSubmitFailed))
                    AnswerOutcome.SubmitFailed
                }
            }

            val log = AnswerLog(
                questionId = playing.question.id,
                chosenAnswer = answer,
                timeRemainingSec = timeRemaining,
                outcome = outcome,
            )

            advanceToNextQuestion(log, nextQuestionDeferred.await())
        }
    }

    private suspend fun advanceToNextQuestion(
        log: AnswerLog,
        preloadedNextQuestion: Question? = null,
    ) {
        val state = _uiState.value
        val newLog = state.answerLog + log
        val nextIndex = state.currentQuestionIndex + 1
        val isLast = nextIndex >= QuizRules.TOTAL_QUESTIONS

        if (isLast) {
            val finalState = state.copy(
                phase = QuizPhase.Completed,
                answerLog = newLog,
                currentQuestionIndex = nextIndex,
                timeRemainingSec = null,
            )
            _uiState.value = finalState

            val finalScore = newLog.computeScore(state.challengeMode)
            _events.send(QuizEvent.NavigateToResult(
                result = QuizSessionResult(
                    playerName = playerName,
                    challengeMode = state.challengeMode,
                    score = finalScore,
                    correctAnswers = finalState.correctAnswers,
                    totalQuestions = QuizRules.TOTAL_QUESTIONS,
                )
            ))
            return
        }

        val nextQuestion = preloadedNextQuestion ?: fetchQuestion()
        if (nextQuestion == null) {
            _events.send(QuizEvent.ShowMessage(QuizMessage.QuestionLoadFailed))
            _events.send(QuizEvent.NavigateBack)
            return
        }

        _uiState.update {
            it.copy(
                phase = QuizPhase.Playing(question = nextQuestion),
                answerLog = newLog,
                currentQuestionIndex = nextIndex,
            )
        }
        startTimer()
    }

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }
}