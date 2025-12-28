package com.emotionstorage.auth.presentation

import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.domain.model.User.AuthProvider
import com.emotionstorage.domain.useCase.auth.HandleLoginUseCase
import com.emotionstorage.domain.useCase.auth.LoginUseCase
import com.emotionstorage.presentation.BaseException
import com.emotionstorage.presentation.BaseSideEffect
import com.emotionstorage.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.annotation.OrbitExperimental
import javax.inject.Inject

data class LoginState(
    val isLoading: Boolean = false,
)

sealed class LoginAction {
    data class Login(
        val provider: AuthProvider,
    ) : LoginAction()

    data class RetryLogin(
        val accessToken: String,
    ) : LoginAction()
}

sealed class LoginSideEffect : BaseSideEffect {
    object SocialLoginError : LoginSideEffect()

    data class NeedSignUp(
        val provider: AuthProvider,
        val idToken: String,
    ) : LoginSideEffect()

    data class RetryLogin(
        val accessToken: String,
    ) : LoginSideEffect()

    data class InquireLoginError(
        val errorCode: ErrorCode,
        val throwable: Throwable,
    ) : LoginSideEffect()

    object LoginSuccess : LoginSideEffect()
}

@OptIn(OrbitExperimental::class)
@HiltViewModel
class LoginViewModel
@Inject constructor(
    private val login: LoginUseCase,
    private val handleLoginUseCase: HandleLoginUseCase,
) : BaseViewModel<LoginState>(
    LoginState(),
) {
    private var retryCount: Int = 0

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.Login -> {
                handleLogin(action.provider)
            }

            is LoginAction.RetryLogin -> {
                handleRetryLogin(action.accessToken)
            }
        }
    }

    private fun handleLogin(provider: AuthProvider) =
        baseIntent {
            reduce {
                state.copy(isLoading = true)
            }
            retryCount = 0
            login(provider).handle(onSuccess = {
                reduce {
                    state.copy(isLoading = false)
                }
                postSideEffect(LoginSideEffect.LoginSuccess)
            }, onError = { throwable, code, data ->
                reduce {
                    state.copy(isLoading = false)
                }
                if (code == ErrorCode.INVALID_ID_TOKEN || code == ErrorCode.INVALID_KAKAO_ACCESS_TOKEN) {
                    // invalid social id - show toast
                    retryCount = 0
                    postSideEffect(LoginSideEffect.SocialLoginError)
                } else if (code == ErrorCode.NEED_SIGN_UP) {
                    // need sign up - nav to on boarding
                    retryCount = 0
                    postSideEffect(LoginSideEffect.NeedSignUp(provider, data as String))
                } else if (code == ErrorCode.LOGIN_CLIENT_ERROR) {
                    // client login handling error - show login error modal
                    retryCount++
                    postSideEffect(LoginSideEffect.RetryLogin(data as String))
                } else {
                    // throw base exception for base viewmodel to handle
                    throw BaseException(
                        code = code,
                        message = throwable.message,
                        cause = throwable,
                    )
                }
            })
        }

    private fun handleRetryLogin(accessToken: String) =
        baseIntent {
            reduce {
                state.copy(isLoading = true)
            }
            handleLoginUseCase(accessToken).handle(
                onSuccess = {
                    reduce {
                        state.copy(isLoading = false)
                    }
                    retryCount = 0
                    postSideEffect(LoginSideEffect.LoginSuccess)
                },
                onError = { throwable, code, data ->
                    reduce {
                        state.copy(isLoading = false)
                    }
                    if (retryCount < 3) {
                        retryCount++
                        postSideEffect(LoginSideEffect.RetryLogin(data as String))
                    } else {
                        retryCount = 0
                        postSideEffect(
                            LoginSideEffect.InquireLoginError(
                                code,
                                throwable
                            )
                        )
                    }
                },
            )
        }
}
