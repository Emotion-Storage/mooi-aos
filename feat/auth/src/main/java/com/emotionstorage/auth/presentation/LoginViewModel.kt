package com.emotionstorage.auth.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.auth.domain.usecase.LoginUseCase
import com.emotionstorage.domain.common.DataState
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
    private val _provider = MutableStateFlow<User.AuthProvider?>(null)
    private val _idToken = MutableStateFlow<String?>(null)
    private val _loginState = MutableStateFlow<State.LoginState>(State.LoginState.IDLE)

    val state = combine(
        _provider,
        _idToken,
        _loginState
    ) { provider, idToken, loginState ->
        State(provider, idToken, loginState)
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
        _provider.update { provider }

        viewModelScope.launch {
            login(provider).collect { result ->
                when (result) {
                    is DataState.Loading -> {
                        _loginState.update { if (result.isLoading) State.LoginState.LOADING else State.LoginState.IDLE }
                    }


                    is DataState.Success -> {
                        _loginState.update { State.LoginState.SUCCESS }
                    }

                    is DataState.Error -> {
                        if (result.data != null) {
                            _idToken.update { result.data as String }
                            _loginState.update { State.LoginState.SIGN_UP }
                        } else {
                            _loginState.update { State.LoginState.ERROR }
                        }
                    }
                }
            }
        }
    }

    data class State(
        val provider: User.AuthProvider? = null,
        val idToken: String? = null,
        val loginState: LoginState = LoginState.IDLE
    ) {
        enum class LoginState {
            IDLE,
            LOADING,
            SUCCESS,
            SIGN_UP,
            ERROR
        }
    }
}
