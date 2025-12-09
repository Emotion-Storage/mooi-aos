package com.emotionstorage.auth.presentation

import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.domain.model.User
import com.emotionstorage.domain.useCase.auth.LoginUseCase
import com.sunjoolee.presentation.BaseException
import com.sunjoolee.presentation.BaseSideEffect
import com.sunjoolee.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class LoginState(
    val isLoading: Boolean = false,
)

sealed class LoginAction {
    data class Login(
        val provider: User.AuthProvider,
    ) : LoginAction()
}

sealed class LoginSideEffect : BaseSideEffect {
    object LoginSuccess : LoginSideEffect()

    data class NeedSignUp(
        val provider: User.AuthProvider,
        val idToken: String,
    ) : LoginSideEffect()
}

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val login: LoginUseCase,
    ) : BaseViewModel<LoginState>(
            LoginState(),
        ) {
        fun onAction(action: LoginAction) {
            when (action) {
                is LoginAction.Login -> {
                    handleLogin(action.provider)
                }
            }
        }

        private fun handleLogin(provider: User.AuthProvider) =
            baseIntent {
                reduce {
                    state.copy(isLoading = true)
                }
                login(provider).handle(
                    onSuccess = {
                        reduce {
                            state.copy(isLoading = false)
                        }
                        postSideEffect(LoginSideEffect.LoginSuccess)
                    },
                    onError = { throwable, code, data ->
                        reduce {
                            state.copy(isLoading = false)
                        }
                        if (code == ErrorCode.NEED_SIGN_UP) {
                            val idToken = data?.toString() ?: throw IllegalStateException("idToken is null")
                            postSideEffect(LoginSideEffect.NeedSignUp(provider, idToken))
                        } else {
                            throw BaseException(
                                message = throwable.message,
                                code = code,
                                throwable = throwable,
                            )
                        }
                    },
                )
            }
    }
