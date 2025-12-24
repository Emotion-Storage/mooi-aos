package com.emotionstorage.auth.presentation

import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.domain.model.User
import com.emotionstorage.domain.model.User.AuthProvider
import com.emotionstorage.domain.useCase.auth.LoginUseCase
import com.emotionstorage.presentation.BaseException
import com.emotionstorage.presentation.BaseSideEffect
import com.emotionstorage.presentation.BaseViewModel
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
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
    object LoginSuccess : LoginSideEffect()

    object LoginErrorWithRetry : LoginSideEffect()

    object LoginErrorWithInquiry : LoginSideEffect()

    data class NeedSignUp(
        val provider: AuthProvider,
        val idToken: String,
    ) : LoginSideEffect()
}

@HiltViewModel
class LoginViewModel
@Inject constructor(
    private val login: LoginUseCase,
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
            postSideEffect(LoginSideEffect.LoginSuccess)
        }, onError = { throwable, code, data ->
            reduce {
                state.copy(isLoading = false)
            }
            if (code == ErrorCode.NEED_SIGN_UP && data != null) {
                val idToken = data as String
                Logger.d("Need sign up, idToken: ${idToken.take(6)}...")
                postSideEffect(LoginSideEffect.NeedSignUp(provider, idToken))
                return@handle
            }

            // show login error modal on any login errors
            if (retryCount < 3) {
                retryCount++
                postSideEffect(LoginSideEffect.LoginErrorWithRetry)
            } else {
                postSideEffect(LoginSideEffect.LoginErrorWithInquiry)
            }
        })
    }
}
