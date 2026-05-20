package com.leanite.dynaquiz.feature.quiz

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class QuizTimerController {
    private val _timeRemaining = MutableStateFlow<Int?>(null)
    val timeRemaining: StateFlow<Int?> = _timeRemaining.asStateFlow()

    private var job: Job? = null

    fun start(
        scope: CoroutineScope,
        durationSec: Int,
        onTimeout: () -> Unit,
    ) {
        job?.cancel()
        job =
            scope.launch {
                for (sec in durationSec downTo 1) {
                    _timeRemaining.value = sec
                    delay(1.seconds)
                }
                _timeRemaining.value = 0
                onTimeout()
            }
    }

    fun stop() {
        cancel()
        _timeRemaining.value = null
    }

    fun cancel() {
        job?.cancel()
    }

    suspend fun runCountdown(
        secondsFrom: Int,
        onTick: (Int) -> Unit,
    ) {
        for (sec in secondsFrom downTo 1) {
            onTick(sec)
            delay(1.seconds)
        }
    }
}
