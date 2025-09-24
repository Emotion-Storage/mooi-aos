package com.emotionstorage.auth.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.auth.domain.usecase.LoginUseCase
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.User
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

sealed class LoginAction {
    data class Login(
        val provider: User.AuthProvider,
    ) : LoginAction()
}

sealed class LoginSideEffect {
    object LoginSuccess : LoginSideEffect()

    data class LoginFailed(
        val provider: User.AuthProvider,
        val idToken: String,
    ) : LoginSideEffect()

    object LoginFailedWithException : LoginSideEffect()
}

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val login: LoginUseCase,
    ) : ViewModel(), ContainerHost<Unit, LoginSideEffect> {
        override val container = container<Unit, LoginSideEffect>(Unit)

        fun onAction(action: LoginAction) {
            when (action) {
                is LoginAction.Login -> {
                    handleLogin(action.provider)
                }
            }
        }

        private fun handleLogin(provider: User.AuthProvider) =
            intent {
                Logger.v("onLoginButtonClick, provider: $provider")

                login(provider).collect { result ->
                    when (result) {
                        is DataState.Loading -> {
                            // do nothing
                        }

                        is DataState.Success -> {
                            Logger.i("Login success, $result")
                            postSideEffect(LoginSideEffect.LoginSuccess)
                        }

                        is DataState.Error -> {
                            Logger.e("Login error, $result")
                            if (result.data != null) {
                                postSideEffect(LoginSideEffect.LoginFailed(provider, result.data!!.toString()))
                            } else {
                                postSideEffect(LoginSideEffect.LoginFailedWithException)
                            }
                        }
                    }
                }
            }
    }
