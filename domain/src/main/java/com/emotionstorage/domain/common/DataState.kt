package com.emotionstorage.domain.common

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
        val data: Any? = null,
    ) : DataState<Nothing>()

    override fun toString(): String =
        when (this) {
            is Success -> "Success[data=$data]"
            is Loading -> "Loading[isLoading=$isLoading, data=$data]"
            is Error -> "Error[throwable=$throwable]"
        }
}
