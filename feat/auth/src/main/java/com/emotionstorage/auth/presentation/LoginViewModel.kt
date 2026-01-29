package com.emotionstorage.auth.presentation

import android.content.Context
import com.emotionstorage.auth.util.GoogleCredentialManager
import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.domain.model.User.AuthProvider
import com.emotionstorage.domain.useCase.auth.GoogleLoginUseCase
import com.emotionstorage.domain.useCase.auth.HandleLoginUseCase
import com.emotionstorage.domain.useCase.auth.LoginUseCase
import com.emotionstorage.presentation.BaseException
import com.emotionstorage.presentation.BaseSideEffect
import com.emotionstorage.presentation.BaseViewModel
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.annotation.OrbitExperimental
import javax.inject.Inject

data class LoginState(
    val isLoading: Boolean = false,
)

sealed class LoginAction {
    data class Login(
        val context: Context,
        val provider: AuthProvider,
    ) : LoginAction()

    data class RetryLogin(
        val accessToken: String,
    ) : LoginAction()
}

sealed class LoginSideEffect : BaseSideEffect {
    object SocialTokenIssueError : LoginSideEffect()

    object InvalidSocialTokenError : LoginSideEffect()

    data class NeedSignUp(
        val provider: AuthProvider,
        val idToken: String,
    ) : LoginSideEffect()

    data class RetryHandleLogin(
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
        private val googleLogin: GoogleLoginUseCase,
        private val handleLoginUseCase: HandleLoginUseCase,
    ) : BaseViewModel<LoginState>(
            LoginState(),
        ) {
        private var retryCount: Int = 0

        fun onAction(action: LoginAction) {
            when (action) {
                is LoginAction.Login -> {
                    handleLogin(action.context, action.provider)
                }

                is LoginAction.RetryLogin -> {
                    handleRetryLogin(action.accessToken)
                }
            }
        }

        private fun handleLogin(context: Context, provider: AuthProvider) =
            baseIntent {
                reduce {
                    state.copy(isLoading = true)
                }
                retryCount = 0

                val result = when(provider){
                    AuthProvider.KAKAO -> login(provider)
                    AuthProvider.GOOGLE -> {
                        val googleCredentialManager = GoogleCredentialManager(context)
                        val idToken = googleCredentialManager.getIdToken()
                        googleLogin(idToken)
                    }
                }

                result.handle(onSuccess = {
                    reduce {
                        state.copy(isLoading = false)
                    }
                    postSideEffect(LoginSideEffect.LoginSuccess)
                }, onError = { throwable, code, data ->
                    reduce {
                        state.copy(isLoading = false)
                    }
                    if (code == ErrorCode.CLIENT_ID_TOKEN_ISSUE_FAIL) {
                        Logger.d("login error - social token issue fail, ${throwable.message}")
                        retryCount = 0
                        postSideEffect(LoginSideEffect.SocialTokenIssueError)
                    } else if (code == ErrorCode.NEED_SIGN_UP) {
                        Logger.d("login error - need sign up")
                        retryCount = 0
                        postSideEffect(LoginSideEffect.NeedSignUp(provider, data as String))
                    } else if (code == ErrorCode.INVALID_ID_TOKEN || code == ErrorCode.INVALID_KAKAO_ACCESS_TOKEN) {
                        Logger.d("login error - invalid social id")
                        retryCount = 0
                        postSideEffect(LoginSideEffect.InvalidSocialTokenError)
                    } else if (code == ErrorCode.CLIENT_LOGIN_ERROR) {
                        Logger.d("login error - client error")
                        retryCount++
                        postSideEffect(LoginSideEffect.RetryHandleLogin(data as String))
                    } else {
                        Logger.e("login error - unknown error, code: $code, throwable: $throwable")
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
                            postSideEffect(LoginSideEffect.RetryHandleLogin(data as String))
                        } else {
                            retryCount = 0
                            postSideEffect(
                                LoginSideEffect.InquireLoginError(
                                    code,
                                    throwable,
                                ),
                            )
                        }
                    },
                )
            }
    }
