package com.emotionstorage.auth.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.auth.domain.usecase.LoginWithIdTokenUseCase
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.User
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

sealed class SignupCompleteAction {
    data class LoginWithIdToken(val provider: User.AuthProvider, val idToken: String) :
        SignupCompleteAction()
}

sealed class SignupCompleteSideEffect {
    object LoginSuccess : SignupCompleteSideEffect()

    object LoginFailed : SignupCompleteSideEffect()
}

@HiltViewModel
class SignupCompleteViewModel
    @Inject
    constructor(
        private val loginWithIdToken: LoginWithIdTokenUseCase,
    ) : ViewModel(), ContainerHost<Unit, SignupCompleteSideEffect> {
        override val container: Container<Unit, SignupCompleteSideEffect> = container(Unit)

        fun onAction(action: SignupCompleteAction) {
            when (action) {
                is SignupCompleteAction.LoginWithIdToken -> {
                    handleLoginWithIdToken(action.provider, action.idToken)
                }
            }
        }

        private fun handleLoginWithIdToken(
            provider: User.AuthProvider,
            idToken: String,
        ) = intent {
            loginWithIdToken(provider, idToken).collect { result ->
                when (result) {
                    is DataState.Loading -> Unit
                    is DataState.Success -> {
                        Logger.i("LoginWithIdToken success")
                        postSideEffect(SignupCompleteSideEffect.LoginSuccess)
                    }
                    is DataState.Error -> {
                        Logger.e("LoginWithIdToken failed: ${result.throwable?.message}")
                        postSideEffect(SignupCompleteSideEffect.LoginFailed)
                    }
                }
            }
        }
    }
