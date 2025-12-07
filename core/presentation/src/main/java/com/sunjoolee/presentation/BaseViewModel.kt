package com.sunjoolee.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.domain.common.ErrorCode
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.Syntax
import org.orbitmvi.orbit.viewmodel.container

data class BaseState(
    val isLoading: Boolean = false,
)

sealed class BaseSideEffect {
    object NetworkError : BaseSideEffect()
    object SessionExpired : BaseSideEffect()
    object TemporalError : BaseSideEffect()
}

@OptIn(OrbitExperimental::class)
open class BaseViewModel<STATE : BaseState, SIDE_EFFECT : BaseSideEffect>(
    initialState: STATE
) : ViewModel(), ContainerHost<STATE, SIDE_EFFECT> {
    override val container = container<STATE, SIDE_EFFECT>(initialState)

    protected fun baseIntent(transformer: suspend Syntax<STATE, SIDE_EFFECT>.() -> Unit) = intent {
        try {
            transformer()
        } catch (e: Throwable) {
            handleError(e)
        }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            handleError(throwable)
        }
    }
    protected val baseViewModelScope = viewModelScope.plus(exceptionHandler)

    protected suspend fun handleError(error: Throwable) = subIntent {
        with(
            if (error is BaseException) error
            else BaseException(message = error.message, code = ErrorCode.UNKNOWN, throwable = error)
        ) {
            if (code == ErrorCode.NETWORK_ERROR) {
                // handle network error
                postSideEffect(BaseSideEffect.NetworkError as SIDE_EFFECT)
            } else if (code in listOf<ErrorCode>(
                    ErrorCode.ACCESS_TOKEN_EXPIRED,
                    ErrorCode.ACCESS_TOKEN_INVALID,
                    ErrorCode.REFRESH_TOKEN_EXPIRED,
                    ErrorCode.REFRESH_TOKEN_NOT_FOUND,
                    ErrorCode.UNAUTHORIZED,
                )
            ) {
                // handle auth expiration error
                // todo: refresh token here? or in remote interceptor?
                postSideEffect(BaseSideEffect.SessionExpired as SIDE_EFFECT)
            } else if (code == ErrorCode.INTERNAL_SERVER_ERROR) {
                // handle server error
                postSideEffect(BaseSideEffect.TemporalError as SIDE_EFFECT)
            } else if (code == ErrorCode.UNKNOWN) {
                // handle unknown error
                postSideEffect(BaseSideEffect.TemporalError as SIDE_EFFECT)
            } else {
                Logger.e("Error could not be handled in BaseViewModel, ${this}")
                throw this
            }
        }
    }
}
