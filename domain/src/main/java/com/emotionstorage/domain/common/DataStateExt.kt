package com.emotionstorage.domain.common

import kotlinx.coroutines.flow.Flow

fun <T, K> DataState<T>.map(
    convertData: (T) -> K
): DataState<K> =
    when (this) {
        is DataState.Success -> DataState.Success(convertData(data))
        is DataState.Error -> DataState.Error(throwable, code, data)
        is DataState.Loading -> DataState.Loading(isLoading, data?.let { convertData(it) })
    }

suspend fun <T> collectDataState(
    flow: Flow<DataState<T>>,
    onSuccess: suspend (data: T) -> Unit,
    onError: suspend (throwable: Throwable, data: Any?) -> Unit = { _, _ -> },
    onLoading: suspend (isLoading: Boolean) -> Unit = {},
) {
    flow.collect { result ->
        when (result) {
            is DataState.Success -> onSuccess(result.data)
            is DataState.Error -> onError(result.throwable, result.data)
            is DataState.Loading -> onLoading(result.isLoading)
        }
    }
}
