package com.emotionstorage.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.auth.domain.usecase.LoginUseCase
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.User
import com.orhanobut.logger.Logger
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
    fun clearState()
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
    ) { provider, idToken,  loginState ->
        State(provider, idToken,  loginState)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = State()
    )
    val event: LoginEvent = this@LoginViewModel

    init {
        Logger.v("LoginViewModel init")
    }

    override suspend fun onLoginButtonClick(
        provider: User.AuthProvider
    ) {
        Logger.v("onLoginButtonClick, provider: $provider")
        _provider.update { provider }

        viewModelScope.launch {
            login(provider).collect { result ->
                when (result) {
                    is DataState.Loading -> {
                        if(result.isLoading) _loginState.update { State.LoginState.IDLE }
                    }


                    is DataState.Success -> {
                        Logger.i("Login success, $result")
                        _loginState.update { State.LoginState.SUCCESS }
                    }

                    is DataState.Error -> {
                        Logger.e("Login error, $result")
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

    override fun clearState() {
        _provider.update { null }
        _idToken.update { null }
        _loginState.update { State.LoginState.IDLE }
    }

    data class State(
        val provider: User.AuthProvider? = null,
        val idToken: String? = null,
        val loginState: LoginState = LoginState.IDLE
    ) {
        enum class LoginState {
            IDLE,
            SUCCESS,
            SIGN_UP,
            ERROR
        }
    }
}
