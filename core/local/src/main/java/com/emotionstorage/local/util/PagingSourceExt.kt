package com.emotionstorage.local.util

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult.*
import androidx.paging.PagingState


fun <Key : Any, Value1 : Any, Value2 : Any> PagingSource<Key, Value1>.mapValue(
    mapper: (Value1) -> Value2
): PagingSource<Key, Value2> = object : PagingSource<Key, Value2>() {
    override fun getRefreshKey(state: PagingState<Key, Value2>): Key? {
        return this@mapValue.getRefreshKey(
            state as PagingState<Key, Value1>
        )
    }

    override suspend fun load(params: LoadParams<Key>): LoadResult<Key, Value2> {
        val result = this@mapValue.load(params)
        try {
            return when (result) {
                is Page -> {
                    Page(
                        data = result.data.map(mapper),
                        prevKey = result.prevKey,
                        nextKey = result.nextKey
                    )
                }

                is Error -> {
                    result
                }

                else -> {
                    throw IllegalStateException("Invalid LoadResult type")
                }
            } as LoadResult<Key, Value2>
        } catch (e: Exception) {
            return Error(e)
        }
    }
}
