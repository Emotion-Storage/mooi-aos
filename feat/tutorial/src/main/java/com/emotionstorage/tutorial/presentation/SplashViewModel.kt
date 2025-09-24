package com.emotionstorage.tutorial.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.auth.domain.usecase.AutomaticLoginUseCase
import com.emotionstorage.domain.common.DataState
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

private const val SPLASH_DURATION = 2000L

sealed class SplashAction {
    object Initiate : SplashAction()
}

sealed class SplashSideEffect {
    object AutoLoginSuccess : SplashSideEffect()
    object AutoLoginFailed : SplashSideEffect()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val automaticLogin: AutomaticLoginUseCase
) : ViewModel(), ContainerHost<Unit, SplashSideEffect> {
    override val container = container<Unit, SplashSideEffect>(Unit)

    suspend fun onAction(action: SplashAction) {
        when (action) {
            SplashAction.Initiate -> {
                delay(SPLASH_DURATION)
                handleAutoLogin()
            }
        }
    }

    private fun handleAutoLogin() = intent {
        automaticLogin().collect { result ->
            Logger.d("SplashViewModel handleAutoLogin, result: $result")

            when (result) {
                is DataState.Loading -> {
                    // do nothing
                }

                is DataState.Success -> {
                    Logger.i("Auto login success")
                    postSideEffect(SplashSideEffect.AutoLoginSuccess)
                }

                is DataState.Error -> {
                    Logger.e("Auto login error, ${result.throwable}")
                    postSideEffect(SplashSideEffect.AutoLoginFailed)
                }
            }
        }
    }
}
