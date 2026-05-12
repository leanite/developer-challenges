package com.leanite.dynaquiz.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val _events = Channel<SplashEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

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