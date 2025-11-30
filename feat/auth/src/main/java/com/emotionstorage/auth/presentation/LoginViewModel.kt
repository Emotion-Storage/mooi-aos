package com.emotionstorage.auth.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.model.User
import com.emotionstorage.domain.useCase.auth.LoginUseCase
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class LoginState(
    val isLoading: Boolean = false
)

sealed class LoginAction {
    data class Login(
        val provider: User.AuthProvider,
    ) : LoginAction()
}

sealed class LoginSideEffect {
    object LoginSuccess : LoginSideEffect()

    data class NeedSignUp(
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
    ) : ViewModel(),
        ContainerHost<LoginState, LoginSideEffect> {
        override val container = container<LoginState, LoginSideEffect>(LoginState())

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
                reduce {
                    state.copy(isLoading = true)
                }
                login(provider).handle(
                    onSuccess = {
                        reduce {
                            state.copy(isLoading = false)
                        }
                    },
                    onError = { throwable, code, data ->
                        reduce {
                            state.copy(isLoading = false)
                        }
                        if (data != null) {
                            val idToken = data.toString()
                            postSideEffect(LoginSideEffect.NeedSignUp(provider, idToken))
                        } else {
                            postSideEffect(LoginSideEffect.LoginFailedWithException)
                        }
                    },
                )
            }
    }
