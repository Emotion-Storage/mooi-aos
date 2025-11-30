package com.emotionstorage.auth.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.User
import com.emotionstorage.domain.useCase.auth._LoginUseCase
import com.emotionstorage.domain.useCase.myPage.GetAccountInfoUseCase
import com.emotionstorage.domain.useCase.user.SaveUserAccountInfoToLocalUseCase
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDateTime
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
        private val login: _LoginUseCase,
        private val getAccountInfo: GetAccountInfoUseCase,
        private val saveUserLocal: SaveUserAccountInfoToLocalUseCase,
    ) : ViewModel(),
        ContainerHost<Unit, LoginSideEffect> {
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
                            when (val accountInfo = getAccountInfo()) {
                                is DataState.Error -> {
                                    // do nothing
                                }

                                is DataState.Loading -> {
                                    // do nothing
                                }

                                is DataState.Success -> {
                                    // TODO 임시 저장 상태
                                    val savedStatus =
                                        saveUserLocal(
                                            User(
                                                socialType = enumValueOf(accountInfo.data.socialType),
                                                socialId = "Temp",
                                                email = accountInfo.data.email,
                                                nickname = accountInfo.data.nickname,
                                                profileImageUrl = null,
                                                createdAt = LocalDateTime.now(),
                                                updatedAt = LocalDateTime.now(),
                                            ),
                                        )
                                    if (savedStatus) {
                                        Logger.d("Login success, save user info success")
                                        postSideEffect(LoginSideEffect.LoginSuccess)
                                    } else {
                                        // TODO : 아무 처리가 없어서 저장 정보 처리가 안될 때 상황을 생각해 봐야함
                                        postSideEffect(LoginSideEffect.LoginFailedWithException)
                                    }
                                }
                            }
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
