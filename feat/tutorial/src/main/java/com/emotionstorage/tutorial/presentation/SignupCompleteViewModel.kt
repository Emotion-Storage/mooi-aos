package com.emotionstorage.tutorial.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.auth.domain.usecase.LoginWithIdTokenUseCase
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.User
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

sealed class SignupCompleteAction {
    data class Login(val provider: User.AuthProvider, val idToken: String) : SignupCompleteAction()
}

sealed class SignupCompleteSideEffect {
    object LoginSuccess : SignupCompleteSideEffect()
    object LoginFailed : SignupCompleteSideEffect()
}

@HiltViewModel
class SignupCompleteViewModel @Inject constructor(
    private val loginWithIdToken: LoginWithIdTokenUseCase
) : ViewModel(), ContainerHost<Unit, SignupCompleteSideEffect> {
    override val container = container<Unit, SignupCompleteSideEffect>(Unit)

    fun onAction(action: SignupCompleteAction) {
        when (action) {
            is SignupCompleteAction.Login -> {
                handleLogin(action.provider, action.idToken)
            }
        }
    }

    private fun handleLogin(
        provider: User.AuthProvider,
        idToken: String
    ) = intent {
        if (provider == null) {
            Logger.e("provider is null")
            postSideEffect(SignupCompleteSideEffect.LoginFailed)
            return@intent
        }
        if (idToken == null) {
            Logger.e("idToken is null")
            postSideEffect(SignupCompleteSideEffect.LoginFailed)
            return@intent
        }

        loginWithIdToken(provider, idToken).collect { result ->
            when (result) {
                is DataState.Loading -> {
                    // do nothing
                }

                is DataState.Success -> {
                    Logger.i(result.toString())
                    postSideEffect(SignupCompleteSideEffect.LoginSuccess)
                }

                is DataState.Error -> {
                    Logger.e(result.toString())
                    postSideEffect(SignupCompleteSideEffect.LoginFailed)
                }
            }
        }

    }
}

