package com.sunjoolee.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.domain.common.isAuthError
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.Syntax
import org.orbitmvi.orbit.viewmodel.container

interface BaseSideEffect {
    object NetworkError : BaseSideEffect

    object SessionExpired : BaseSideEffect

    object TemporalError : BaseSideEffect
}

@OptIn(OrbitExperimental::class)
open class BaseViewModel<STATE : Any>(
    initialState: STATE,
) : ViewModel(),
    ContainerHost<STATE, BaseSideEffect> {
    override val container = container<STATE, BaseSideEffect>(initialState)

    protected fun baseIntent(transformer: suspend Syntax<STATE, BaseSideEffect>.() -> Unit) =
        intent {
            try {
                transformer()
            } catch (e: Throwable) {
                handleError(e)
            }
        }

    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            viewModelScope.launch {
                handleError(throwable)
            }
        }
    protected val baseViewModelScope = viewModelScope.plus(exceptionHandler)

    protected suspend fun handleError(error: Throwable) =
        subIntent {
            with(
                if (error is BaseException) {
                    error
                } else {
                    BaseException(message = error.message, code = ErrorCode.UNKNOWN, throwable = error)
                },
            ) {
                if (code == ErrorCode.NETWORK_ERROR) {
                    // handle network error
                    postSideEffect(BaseSideEffect.NetworkError)
                } else if (code.isAuthError()) {
                    // handle auth expiration error
                    // todo: refresh token here? or in remote interceptor?
                    postSideEffect(BaseSideEffect.SessionExpired)
                } else if (code == ErrorCode.INTERNAL_SERVER_ERROR) {
                    // handle server error
                    postSideEffect(BaseSideEffect.TemporalError)
                } else if (code == ErrorCode.UNKNOWN) {
                    // handle unknown error
                    postSideEffect(BaseSideEffect.TemporalError)
                } else {
                    Logger.e("Error could not be handled in BaseViewModel, $this")
                    throw this
                }
            }
        }
}
