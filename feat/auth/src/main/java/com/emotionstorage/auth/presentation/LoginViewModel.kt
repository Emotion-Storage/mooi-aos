package com.emotionstorage.auth.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.auth.domain.usecase.LoginUseCase
import com.emotionstorage.auth.presentation.LoginViewModel.State.LoginState
import com.emotionstorage.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

interface LoginEvent {
    suspend fun onLoginButtonClick(
        provider: User.AuthProvider
    )
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val login: LoginUseCase
) : ViewModel(), LoginEvent {
    private val _loginState = MutableStateFlow(LoginState.Idle)

    val state = combine(
        _loginState
    ) { (loginState) ->
        State(loginState)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = State()
    )
    val event: LoginEvent = this@LoginViewModel

    init {
        Log.d("LoginViewModel", "LoginViewModel initialized")
    }

    override suspend fun onLoginButtonClick(
        provider: User.AuthProvider
    ) {
        Log.d("LoginViewModel", "onLoginButtonClick called with provider: $provider")
        _loginState.update { LoginState.Loading }

        viewModelScope.launch {
            if (login(provider)) {
                _loginState.update { LoginState.Success }
            } else {
                _loginState.update { LoginState.Error }
            }
        }
    }

    data class State(
        val loginState: LoginState = LoginState.Idle
    ) {
        enum class LoginState {
            Idle,
            Loading,
            Success,
            Error
        }
    }
}
