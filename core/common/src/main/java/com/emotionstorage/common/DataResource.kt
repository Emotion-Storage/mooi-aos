package com.emotionstorage.common

/**
 * Data resource class for handling data state
 * - Success: success
 * - Empty: success, but empty data
 * - Loading: loading with optional data
 * - Error: error with throwable
 */
sealed class DataResource<out T> {
    class Success<T>(val data: T) : DataResource<T>()
    class Loading<T>(val isLoading: Boolean, val data: T? = null) : DataResource<T>()
    class Error(val throwable: Throwable) : DataResource<Nothing>()

    companion object {
        fun <T> success(data: T) = Success(data)
        fun <T> loading(isLoading: Boolean, data: T? = null) = Loading(isLoading, data)
        fun error(throwable: Throwable) = Error(throwable)
    }
}