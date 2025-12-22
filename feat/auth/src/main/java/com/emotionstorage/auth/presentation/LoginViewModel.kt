package com.emotionstorage.auth.presentation

import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.domain.model.User
import com.emotionstorage.domain.useCase.auth.LoginUseCase
import com.emotionstorage.presentation.BaseException
import com.emotionstorage.presentation.BaseSideEffect
import com.emotionstorage.presentation.BaseViewModel
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
                        // TODO : OR로 2번째로 붙는 조건 관련 로직 수정 필요 (온보딩으로 넘어가지 않는 이슈 임시 해결)
                        if (code == ErrorCode.NEED_SIGN_UP || code == ErrorCode.NETWORK_ERROR) {
                            val idToken =
                                (data as? String) ?: throw BaseException(
                                    message = "idToken is null or not a String",
                                    code = ErrorCode.UNKNOWN,
                                    cause = IllegalStateException(),
                                )
                            postSideEffect(LoginSideEffect.NeedSignUp(provider, idToken))
                        } else {
                            throw BaseException(
                                message = throwable.message,
                                code = code,
                                cause = throwable,
                            )
                        }
                    },
                )
            }
    }
