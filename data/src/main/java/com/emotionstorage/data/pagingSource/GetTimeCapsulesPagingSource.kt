package com.emotionstorage.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.emotionstorage.data.dataSource.remote.TimeCapsuleRemoteDataSource
import com.emotionstorage.data.model.TimeCapsuleEntity
import io.github.aakira.napier.Napier
import java.time.LocalDate
import javax.inject.Inject

class GetTimeCapsulesPagingSource @Inject constructor(
    private val remoteDataSource: TimeCapsuleRemoteDataSource,
    private val startDate: LocalDate,
    private val endDate: LocalDate,
    private val status: String,
) : PagingSource<Int, TimeCapsuleEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TimeCapsuleEntity> {
        Napier.d("load params key: ${params.key}")

        val pageIndex = params.key ?: 1
        val pageSize = params.loadSize
        return try {
            val timeCapsules =
                remoteDataSource.getTimeCapsules(
                    startDate = startDate,
                    endDate = endDate,
                    page = pageIndex,
                    limit = pageSize,
                    status = status,
                )

            LoadResult.Page(
                data = timeCapsules,
                // only load page forwards
                prevKey = null,
                nextKey =
                    timeCapsules.lastOrNull()?.pageData?.let {
                        if (it.hasNextPage) it.page + 1 else null
                    },
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TimeCapsuleEntity>): Int? =
        state.anchorPosition?.let { anchor ->
            Napier.d("getRefreshKey anchor: $anchor")
            val page = state.closestPageToPosition(anchor)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
}
