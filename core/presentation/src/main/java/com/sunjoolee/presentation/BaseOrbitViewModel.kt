package com.sunjoolee.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.common.ErrorCode
import com.orhanobut.logger.Logger
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
open class BaseOrbitViewModel<STATE : BaseState, SIDE_EFFECT : BaseSideEffect>(
    initialState: STATE
) : ViewModel(), ContainerHost<STATE, SIDE_EFFECT> {
    override val container = container<STATE, SIDE_EFFECT>(initialState)

    protected fun baseIntent(transformer: suspend Syntax<STATE, SIDE_EFFECT>.() -> Unit) = intent {
        try {
            transformer()
        } catch (e: Throwable) {
            val error = if (e !is BaseException)
                BaseException(
                    message = e.message,
                    code = ErrorCode.UNKNOWN,
                    throwable = e
                ) else e

            if (error.code == ErrorCode.NETWORK_ERROR) {
                postSideEffect(BaseSideEffect.NetworkError as SIDE_EFFECT)
            } else if (error.code in listOf<ErrorCode>(
                    ErrorCode.ACCESS_TOKEN_EXPIRED,
                    ErrorCode.ACCESS_TOKEN_INVALID,
                    ErrorCode.REFRESH_TOKEN_EXPIRED,
                    ErrorCode.REFRESH_TOKEN_NOT_FOUND,
                    ErrorCode.UNAUTHORIZED,
                )
            ) {
                // todo: refresh token here? or in remote interceptor?
                postSideEffect(BaseSideEffect.SessionExpired as SIDE_EFFECT)
            } else if (error.code == ErrorCode.INTERNAL_SERVER_ERROR) {
                postSideEffect(BaseSideEffect.TemporalError as SIDE_EFFECT)
            } else if (error.code == ErrorCode.UNKNOWN) {
                postSideEffect(BaseSideEffect.TemporalError as SIDE_EFFECT)
            } else {
                Logger.e("Error could not be handled in BaseViewModel, ${error}")
                throw error
            }
        }
    }
}
