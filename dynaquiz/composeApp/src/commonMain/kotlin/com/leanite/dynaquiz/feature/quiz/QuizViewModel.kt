package com.leanite.dynaquiz.feature.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leanite.dynaquiz.core.domain.model.Answer
import com.leanite.dynaquiz.core.domain.model.AnswerLog
import com.leanite.dynaquiz.core.domain.model.AnswerOutcome
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.Question
import com.leanite.dynaquiz.core.domain.model.QuizSessionResult
import com.leanite.dynaquiz.core.domain.model.computeScore
import com.leanite.dynaquiz.core.domain.result.AppResult
import com.leanite.dynaquiz.core.domain.usecase.GetRandomQuestionUseCase
import com.leanite.dynaquiz.core.domain.usecase.SaveQuizSessionUseCase
import com.leanite.dynaquiz.core.domain.usecase.SubmitAnswerUseCase
import kotlinx.coroutines.Deferred
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
    private val saveQuizSessionUseCase: SaveQuizSessionUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState(challengeMode = challengeMode))
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private val _events = Channel<QuizEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var timerJob: Job? = null
    private var prefetchedNextQuestion: Deferred<Question?>? = null

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
            // A ideia é a contagem regressiva mascarar o tempo de request da primeira pergunta
            val firstQuestionDeferred = async { fetchQuestion() }
            runCountdown()
            val firstQuestion = firstQuestionDeferred.await()

            if (firstQuestion == null) {
                abortWithLoadError()
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

    private fun startPlaying(question: Question) {
        _uiState.update { it.copy(phase = QuizPhase.Playing(question = question)) }
        startTimer()
        schedulePrefetchOfNextQuestion()
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
        val playing = currentPlaying() ?: return
        val log = AnswerLog(
            questionId = playing.question.id,
            chosenAnswer = null,
            timeRemainingSec = 0,
            outcome = AnswerOutcome.TimedOut,
        )
        viewModelScope.launch { advanceToNextQuestion(log) }
    }

    // Inicia em background o fetch da próxima pergunta enquanto o user resolve a atual.
    private fun schedulePrefetchOfNextQuestion() {
        prefetchedNextQuestion?.cancel()
        val nextIndex = _uiState.value.currentQuestionIndex + 1

        prefetchedNextQuestion = if (nextIndex < QuizRules.TOTAL_QUESTIONS) {
            viewModelScope.async { fetchQuestion() }
        } else {
            null
        }
    }

    private fun onAnswerSelected(answer: String) {
        val state = _uiState.value
        val playing = state.phase as? QuizPhase.Playing ?: return
        if (playing.isSubmitting) return

        markAnswerSelected(playing, answer)

        val timeRemaining = state.timeRemainingSec ?: 0
        viewModelScope.launch {
            val entry = submitAndBuildEntry(playing.question, answer, timeRemaining)
            advanceToNextQuestion(entry)
        }
    }

    private fun markAnswerSelected(playing: QuizPhase.Playing, answer: String) {
        timerJob?.cancel()
        _uiState.update {
            it.copy(phase = playing.copy(selectedAnswer = answer, isSubmitting = true))
        }
    }

    private suspend fun submitAndBuildEntry(
        question: Question,
        answer: String,
        timeRemaining: Int,
    ): AnswerLog = AnswerLog(
        questionId = question.id,
        chosenAnswer = answer,
        timeRemainingSec = timeRemaining,
        outcome = submitAnswerUseCase(question.id, answer).toOutcome(),
    )

    private suspend fun AppResult<Answer>.toOutcome(): AnswerOutcome = when (this) {
        is AppResult.Success -> AnswerOutcome.Confirmed(correct = data.correct)
        is AppResult.Error -> {
            _events.send(QuizEvent.ShowMessage(QuizMessage.AnswerSubmitFailed))
            AnswerOutcome.SubmitFailed
        }
    }

    private suspend fun advanceToNextQuestion(entry: AnswerLog) {
        val state = _uiState.value
        val updatedLog = state.answerLog + entry
        val nextIndex = state.currentQuestionIndex + 1

        if (nextIndex >= QuizRules.TOTAL_QUESTIONS) {
            completeSession(updatedLog, nextIndex)
        } else {
            goToQuestion(updatedLog, nextIndex)
        }
    }

    private suspend fun completeSession(answerLog: List<AnswerLog>, finalIndex: Int) {
        val mode = _uiState.value.challengeMode
        _uiState.update {
            it.copy(
                phase = QuizPhase.Completed,
                answerLog = answerLog,
                currentQuestionIndex = finalIndex,
                timeRemainingSec = null,
            )
        }

        val result = buildSessionResult(answerLog, mode)
        // TODO: mover esse save pro ResultViewModel quando for criado
        saveQuizSessionUseCase(result)
        _events.send(QuizEvent.NavigateToResult(result))
    }

    private fun buildSessionResult(
        answerLog: List<AnswerLog>,
        mode: ChallengeMode,
    ): QuizSessionResult {
        val confirmedCorrect = answerLog.count {
            (it.outcome as? AnswerOutcome.Confirmed)?.correct == true
        }
        return QuizSessionResult(
            playerName = playerName,
            challengeMode = mode,
            score = answerLog.computeScore(mode),
            correctAnswers = confirmedCorrect,
            totalQuestions = QuizRules.TOTAL_QUESTIONS,
        )
    }

    private suspend fun goToQuestion(newLog: List<AnswerLog>, nextIndex: Int) {
        // Consome o prefetched provavelmente já pronto
        // Fallback é um fetch reativo caso o prefetched falhe
        val nextQuestion = prefetchedNextQuestion?.await() ?: fetchQuestion()
        prefetchedNextQuestion = null

        if (nextQuestion == null) {
            abortWithLoadError()
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
        schedulePrefetchOfNextQuestion()
    }

    private suspend fun fetchQuestion(): Question? =
        when (val result = getRandomQuestionUseCase()) {
            is AppResult.Success -> result.data
            is AppResult.Error -> null
        }

    private suspend fun abortWithLoadError() {
        _events.send(QuizEvent.ShowMessage(QuizMessage.QuestionLoadFailed))
        _events.send(QuizEvent.NavigateBack)
    }

    private fun currentPlaying(): QuizPhase.Playing? =
        _uiState.value.phase as? QuizPhase.Playing

    override fun onCleared() {
        timerJob?.cancel()
        prefetchedNextQuestion?.cancel()
        super.onCleared()
    }
}