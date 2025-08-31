package com.emotionstorage.tutorial.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.auth.domain.usecase.AutomaticLoginUseCase
import com.emotionstorage.auth.presentation.LoginViewModel.State
import com.emotionstorage.tutorial.presentation.SplashViewModel.State.AutoLoginState
import com.emotionstorage.tutorial.presentation.SplashViewModel.State.SplashState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val automaticLogin: AutomaticLoginUseCase
) : ViewModel() {
    private val _splashState = MutableStateFlow<SplashState>(SplashState.Loading)
    private val _autoLoginState = MutableStateFlow<AutoLoginState>(AutoLoginState.Loading)

    val state = combine(
        _splashState,
        _autoLoginState,
    ) { splashState, autoLoginState ->
        State(splashState, autoLoginState)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = State()
    )

    init {
        Log.d("SplashViewModel", "SplashViewModel initialized")

        viewModelScope.launch {
            _splashState.update {
                SplashState.Loading
            }
            delay(2000)
            _splashState.update {
                SplashState.Done
            }
        }

        viewModelScope.launch {
            _autoLoginState.update {
                AutoLoginState.Loading
            }
            if (automaticLogin()) {
                _autoLoginState.update {
                    AutoLoginState.Success
                }
            } else {
                _autoLoginState.update {
                    AutoLoginState.Fail
                }
            }
        }
    }

    data class State(
        val splashState: SplashState = SplashState.Loading,
        val autoLoginState: AutoLoginState = AutoLoginState.Loading
    ) {
        sealed interface SplashState {
            object Loading : SplashState
            object Done : SplashState
        }

        sealed interface AutoLoginState {
            object Loading : AutoLoginState
            object Success : AutoLoginState
            object Fail : AutoLoginState
        }
    }
}