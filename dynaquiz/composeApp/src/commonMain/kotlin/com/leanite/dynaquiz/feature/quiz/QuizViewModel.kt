package com.leanite.dynaquiz.feature.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leanite.dynaquiz.core.domain.model.Answer
import com.leanite.dynaquiz.core.domain.model.AnswerLog
import com.leanite.dynaquiz.core.domain.model.AnswerOutcome
import com.leanite.dynaquiz.core.domain.model.ChallengeMode
import com.leanite.dynaquiz.core.domain.model.Question
import com.leanite.dynaquiz.core.domain.model.QuizPerformance
import com.leanite.dynaquiz.core.domain.model.QuizSessionResult
import com.leanite.dynaquiz.core.domain.model.QuizSetup
import com.leanite.dynaquiz.core.domain.model.computeScore
import com.leanite.dynaquiz.core.domain.result.AppResult
import com.leanite.dynaquiz.core.domain.usecase.GetRandomQuestionUseCase
import com.leanite.dynaquiz.core.domain.usecase.SaveQuizSessionUseCase
import com.leanite.dynaquiz.core.domain.usecase.SubmitAnswerUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuizViewModel(
    private val setup: QuizSetup,
    private val getRandomQuestionUseCase: GetRandomQuestionUseCase,
    private val submitAnswerUseCase: SubmitAnswerUseCase,
    private val saveQuizSessionUseCase: SaveQuizSessionUseCase,
    private val timer: QuizTimerController,
) : ViewModel() {
    private val _uiState = MutableStateFlow(QuizUiState(challengeMode = setup.challengeMode))
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private val _events = Channel<QuizEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val nextQuestionTrigger: Flow<Int> =
        _uiState
            .filter { it.phase is QuizPhase.Playing }
            .map { it.currentQuestionIndex + 1 }
            .distinctUntilChanged()
            .filter { it < QuizRules.TOTAL_QUESTIONS }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val prefetchedQuestion: SharedFlow<Question?> =
        nextQuestionTrigger
            .mapLatest { fetchQuestion() }
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                replay = 1,
            )

    init {
        // Reflete o timer no UiState sem o VM gerenciar a coroutine
        viewModelScope.launch {
            timer.timeRemaining.collect { sec ->
                _uiState.update { it.copy(timeRemainingSec = sec) }
            }
        }
    }

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
        timer.runCountdown(QuizRules.INITIAL_COUNTDOWN_SECONDS) { sec ->
            _uiState.update { it.copy(phase = QuizPhase.Countdown(sec)) }
        }
        _uiState.update { it.copy(phase = QuizPhase.Loading) }
    }

    private fun startPlaying(question: Question) {
        _uiState.update { it.copy(phase = QuizPhase.Playing(question = question)) }
        startQuestionTimer()
    }

    private fun startQuestionTimer() {
        val mode = _uiState.value.challengeMode
        if (mode is ChallengeMode.Timed) {
            timer.start(viewModelScope, mode.perQuestionSeconds) { onTimeOut() }
        }
    }

    private fun onTimeOut() {
        val playing = currentPlaying() ?: return
        val log =
            AnswerLog(
                questionId = playing.question.id,
                chosenAnswer = null,
                timeRemainingSec = 0,
                outcome = AnswerOutcome.TimedOut,
            )
        viewModelScope.launch { advanceToNextQuestion(log) }
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

    private fun markAnswerSelected(
        playing: QuizPhase.Playing,
        answer: String,
    ) {
        timer.cancel()
        _uiState.update {
            it.copy(phase = playing.copy(selectedAnswer = answer, isSubmitting = true))
        }
    }

    private suspend fun submitAndBuildEntry(
        question: Question,
        answer: String,
        timeRemaining: Int,
    ): AnswerLog =
        AnswerLog(
            questionId = question.id,
            chosenAnswer = answer,
            timeRemainingSec = timeRemaining,
            outcome = submitAnswerUseCase(question.id, answer).toOutcome(),
        )

    private suspend fun AppResult<Answer>.toOutcome(): AnswerOutcome =
        when (this) {
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

    private suspend fun completeSession(
        answerLog: List<AnswerLog>,
        finalIndex: Int,
    ) {
        val mode = _uiState.value.challengeMode
        timer.stop()
        _uiState.update {
            it.copy(
                phase = QuizPhase.Completed,
                answerLog = answerLog.toImmutableList(),
                currentQuestionIndex = finalIndex,
            )
        }

        val result = buildSessionResult(answerLog, mode)
        saveQuizSessionUseCase(result)
        _events.send(QuizEvent.NavigateToResult(result))
    }

    private fun buildSessionResult(
        answerLog: List<AnswerLog>,
        mode: ChallengeMode,
    ): QuizSessionResult {
        val confirmedCorrect =
            answerLog.count {
                (it.outcome as? AnswerOutcome.Confirmed)?.correct == true
            }
        return QuizSessionResult(
            setup = setup,
            performance =
                QuizPerformance(
                    score = answerLog.computeScore(mode),
                    correctAnswers = confirmedCorrect,
                    totalQuestions = QuizRules.TOTAL_QUESTIONS,
                ),
        )
    }

    private suspend fun goToQuestion(
        newLog: List<AnswerLog>,
        nextIndex: Int,
    ) {
        val nextQuestion = prefetchedQuestion.first() ?: fetchQuestion()
        if (nextQuestion == null) {
            abortWithLoadError()
            return
        }

        _uiState.update {
            it.copy(
                phase = QuizPhase.Playing(question = nextQuestion),
                answerLog = newLog.toImmutableList(),
                currentQuestionIndex = nextIndex,
            )
        }
        startQuestionTimer()
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

    private fun currentPlaying(): QuizPhase.Playing? = _uiState.value.phase as? QuizPhase.Playing

    override fun onCleared() {
        timer.cancel()
        super.onCleared()
    }
}
