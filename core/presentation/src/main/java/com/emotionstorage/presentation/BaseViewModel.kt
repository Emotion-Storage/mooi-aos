package com.emotionstorage.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.domain.common.isAuthError
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

    data class TemporalError(
        val code: ErrorCode,
        val throwable: Throwable,
    ) : BaseSideEffect
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
                error as? BaseException ?: BaseException(message = error.message, code = ErrorCode.UNKNOWN, cause = error),
            ) {
                if (code == ErrorCode.NETWORK_ERROR) {
                    // handle network error
                    postSideEffect(BaseSideEffect.NetworkError)
                } else if (code.isAuthError()) {
                    // handle auth expiration error
                    postSideEffect(BaseSideEffect.SessionExpired)
                } else {
                    // handle internal server error / unknown error / etc
                    postSideEffect(BaseSideEffect.TemporalError(code, error))
                }
            }
        }
}
