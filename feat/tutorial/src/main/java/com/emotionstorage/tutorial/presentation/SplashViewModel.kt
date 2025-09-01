package com.emotionstorage.tutorial.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.auth.domain.usecase.AutomaticLoginUseCase
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.tutorial.presentation.SplashViewModel.State.AutoLoginState
import com.emotionstorage.tutorial.presentation.SplashViewModel.State.SplashState
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
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
        Logger.v("SplashViewModel init")

        handleSplashState()
        handleAutoLogin()
    }

    private fun handleSplashState() {
        // set splash state as loading for 2s
        // to insure splash screen is shown for at least 2s
        viewModelScope.launch {
            _splashState.update {
                SplashState.Loading
            }
            delay(2000)
            _splashState.update {
                SplashState.Done
            }
        }
    }

    private fun handleAutoLogin() {
        viewModelScope.launch {
            _autoLoginState.update {
                AutoLoginState.Loading
            }

            automaticLogin().collectLatest { result ->
                Logger.d("SplashViewModel handleAutoLogin, result: $result")

                when (result) {
                    is DataState.Loading -> {
                        if (result.isLoading) {
                            _autoLoginState.update {
                                AutoLoginState.Loading
                            }
                        }
                    }

                    is DataState.Success -> {
                        _autoLoginState.update {
                            AutoLoginState.Success
                        }
                    }

                    is DataState.Error -> {
                        Logger.e("Auto login error, ${result.throwable.toString()}")
                        _autoLoginState.update {
                            AutoLoginState.Failed(result.throwable)
                        }
                    }

                }
            }
        }
    }

    data class State(
        val splashState: SplashState = SplashState.Loading,
        val autoLoginState: AutoLoginState = AutoLoginState.Loading
    ) {
        sealed class SplashState {
            object Loading : SplashState()
            object Done : SplashState()
        }

        sealed class AutoLoginState {
            object Loading : AutoLoginState()
            object Success : AutoLoginState()
            data class Failed(val throwable: Throwable) :
                AutoLoginState()
        }
    }
}