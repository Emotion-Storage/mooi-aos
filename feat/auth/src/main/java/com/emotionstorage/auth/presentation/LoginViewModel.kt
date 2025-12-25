package com.emotionstorage.auth.presentation

import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.domain.model.User.AuthProvider
import com.emotionstorage.domain.useCase.auth.LoginUseCase
import com.emotionstorage.domain.useCase.auth.LoginWithIdTokenUseCase
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
        val provider: AuthProvider,
    ) : LoginAction()
}

sealed class LoginSideEffect : BaseSideEffect {
    object SocialLoginError : LoginSideEffect()

    data class NeedSignUp(
        val provider: AuthProvider,
        val idToken: String,
    ) : LoginSideEffect()

    data class RetryLogin(
        val provider: AuthProvider,
        val idToken: String,
    ) : LoginSideEffect()

    object InquireLoginError : LoginSideEffect()

    object LoginSuccess : LoginSideEffect()
}

@OptIn(OrbitExperimental::class)
@HiltViewModel
class LoginViewModel
@Inject constructor(
    private val login: LoginUseCase,
    private val loginWithIdToken: LoginWithIdTokenUseCase
) : BaseViewModel<LoginState>(
    LoginState(),
) {
    private var retryCount: Int = 0

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.Login -> {
                handleLogin(action.provider)
            }
        }
    }

    private fun handleLogin(provider: AuthProvider) = baseIntent {
        reduce {
            state.copy(isLoading = true)
        }
        login(provider).handle(onSuccess = {
            reduce {
                state.copy(isLoading = false)
            }
            retryCount = 0
            postSideEffect(LoginSideEffect.LoginSuccess)
        }, onError = { throwable, code, data ->
            reduce {
                state.copy(isLoading = false)
            }
            handleLoginError(code, provider, data as? String)
        })
    }

    private fun handleLogin(provider: AuthProvider, idToken: String) = baseIntent {
        reduce {
            state.copy(isLoading = true)
        }
        loginWithIdToken(provider, idToken).handle(
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
                handleLoginError(code, provider, idToken)
            }
        )
    }

    private suspend fun handleLoginError(code: ErrorCode, provider: AuthProvider, idToken: String?) = subIntent {
        if (code == ErrorCode.INVALID_ID_TOKEN || code == ErrorCode.INVALID_KAKAO_ACCESS_TOKEN || idToken == null) {
            // invalid social id - show toast
            retryCount = 0
            postSideEffect(LoginSideEffect.SocialLoginError)
        } else if (code == ErrorCode.NEED_SIGN_UP) {
            // need sign up - nav to on boarding
            retryCount = 0
            postSideEffect(LoginSideEffect.NeedSignUp(provider, idToken))
        } else {
            // show login error modal on any login errors
            if (retryCount < 3) {
                retryCount++
                postSideEffect(LoginSideEffect.RetryLogin(provider, idToken))
            } else {
                postSideEffect(LoginSideEffect.InquireLoginError)
            }
        }
    }
}
