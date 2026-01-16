package com.emotionstorage.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.emotionstorage.data.dataSource.remote.NotificationRemoteDataSource
import com.emotionstorage.data.model.NotificationEntity
import io.github.aakira.napier.Napier

class GetNotificationsPagingSource(
    private val remoteDataSource: NotificationRemoteDataSource,
) : PagingSource<Int, NotificationEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotificationEntity> {
        Napier.d("load params key: ${params.key}")

        val pageIndex = params.key ?: 1
        val pageSize = params.loadSize
        return try {
            val notifications =
                remoteDataSource.getNotifications(
                    page = pageIndex,
                    limit = pageSize,
                )

            LoadResult.Page(
                data = notifications,
                // only load page forwards
                prevKey = null,
                nextKey =
                    notifications.lastOrNull()?.pageData?.let {
                        if (it.hasNextPage) it.page + 1 else null
                    },
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NotificationEntity>): Int? =
        state.anchorPosition?.let { anchor ->
            Napier.d("getRefreshKey anchor: $anchor")
            val page = state.closestPageToPosition(anchor)
            page?.nextKey?.minus(1)
        }
}
