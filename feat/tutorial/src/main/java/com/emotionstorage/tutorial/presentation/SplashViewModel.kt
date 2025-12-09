package com.emotionstorage.tutorial.presentation

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.useCase.auth.AutomaticLoginUseCase
import com.orhanobut.logger.Logger
import com.sunjoolee.presentation.BaseException
import com.sunjoolee.presentation.BaseSideEffect
import com.sunjoolee.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

private const val SPLASH_DURATION = 2000L

sealed class SplashAction {
    object Init : SplashAction()
}

sealed class SplashSideEffect : BaseSideEffect {
    object AutoLoginSuccess : SplashSideEffect()
}

@HiltViewModel
class SplashViewModel
@Inject
constructor(
    private val automaticLogin: AutomaticLoginUseCase,
) : BaseViewModel<Unit>(
    initialState = Unit,
) {
    suspend fun onAction(action: SplashAction) {
        when (action) {
            SplashAction.Init -> {
                delay(SPLASH_DURATION)
                handleAutoLogin()
            }
        }
    }

    private fun handleAutoLogin() =
        baseIntent {
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
                        throw BaseException(
                            message = result.throwable.message,
                            code = result.code,
                            throwable = result.throwable,
                        )
                    }
                }
            }
        }
}
