package com.emotionstorage.domain.common

import ErrorCode

/**
 * Sealed class to represent data state - **used in ui & presentation layer**
 * - Success: success, with data
 * - Loading: loading with optional data
 * - Fail: failed with throwable
 */
sealed class DataState<out T> {
    class Success<T>(
        val data: T,
    ) : DataState<T>()

    class Loading<T>(
        val isLoading: Boolean,
        val data: T? = null,
    ) : DataState<T>()

    class Error(
        val throwable: Throwable,
        val code: ErrorCode = ErrorCode.UNKNOWN,
        val data: Any? = null,
    ) : DataState<Nothing>()

    override fun toString(): String =
        when (this) {
            is Success -> "Success[data=$data]"
            is Loading -> "Loading[isLoading=$isLoading, data=$data]"
            is Error -> "Error[code: ${code.name}, throwable=$throwable]"
        }

    @Deprecated("use handle instead")
    suspend fun handle(
        onSuccess: suspend (data: T) -> Unit,
        onError: suspend (throwable: Throwable, data: Any?) -> Unit = { _, _ -> },
        onLoading: suspend (isLoading: Boolean) -> Unit = {},
    ) {
        when (this) {
            is Success -> onSuccess(data)
            is Error -> onError(throwable, data)
            is Loading -> onLoading(isLoading)
        }
    }

    suspend fun handle(
        onSuccess: suspend (data: T) -> Unit,
        onError: suspend (throwable: Throwable, code: ErrorCode, data: Any?) -> Unit = { _, _, _ -> },
        onLoading: suspend (isLoading: Boolean) -> Unit = {},
    ) {
        when (this) {
            is Success -> onSuccess(data)
            is Error -> onError(throwable, code, data)
            is Loading -> onLoading(isLoading)
        }
    }
}
