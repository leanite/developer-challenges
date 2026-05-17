package com.leanite.dynaquiz.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leanite.dynaquiz.core.domain.usecase.WarmupServerUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val warmupServerUseCase: WarmupServerUseCase,
) : ViewModel() {
    private val _events = Channel<SplashEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        warmupServer()
    }

    private fun warmupServer() =
        viewModelScope.launch {
            warmupServerUseCase()
        }

    fun onIntent(intent: SplashIntent) {
        when (intent) {
            SplashIntent.AnimationFinished -> onAnimationFinished()
        }
    }

    private fun onAnimationFinished() {
        viewModelScope.launch {
            _events.send(SplashEvent.NavigateToNext)
        }
    }
}
