package com.emotionstorage.local.util

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult.Page
import androidx.paging.PagingState
import com.orhanobut.logger.Logger

fun <Key : Any, Value1 : Any, Value2 : Any> PagingSource<Key, Value1>.mapValue(
    mapper: (Value1) -> Value2,
): PagingSource<Key, Value2> =
    object : PagingSource<Key, Value2>() {
        override fun getRefreshKey(state: PagingState<Key, Value2>): Key? {
            Logger.d("getRefreshKey called, state: $state")
            return state.anchorPosition?.let { anchorPosition ->
                val anchorPage = state.closestPageToPosition(anchorPosition)
                anchorPage?.prevKey ?: anchorPage?.nextKey
            }
        }

        override suspend fun load(params: LoadParams<Key>): LoadResult<Key, Value2> {
            val result = this@mapValue.load(params)
            Logger.d("load called, result: $result")
            try {
                return when (result) {
                    is Page -> {
                        Page(
                            data = result.data.map(mapper),
                            prevKey = result.prevKey,
                            nextKey = result.nextKey,
                        )
                    }

                    is LoadResult.Error -> {
                        result
                    }

                    is LoadResult.Invalid -> {
                        result
                    }
                } as LoadResult<Key, Value2>
            } catch (e: Exception) {
                throw e
            }
        }
    }
