package com.emotionstorage.data.remoteMediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.emotionstorage.data.dataSource.local.TimeCapsuleLocalDataSource
import com.emotionstorage.data.dataSource.remote.TimeCapsuleRemoteDataSource
import com.emotionstorage.data.model.TimeCapsuleEntity
import io.github.aakira.napier.Napier
import java.time.LocalDate

@OptIn(ExperimentalPagingApi::class)
class GetTimeCapsulesRemoteMediator(
    private val remoteDataSource: TimeCapsuleRemoteDataSource,
    private val localDataSource: TimeCapsuleLocalDataSource,
    private val startDate: LocalDate,
    private val endDate: LocalDate,
    private val status: String,
) : RemoteMediator<Int, TimeCapsuleEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TimeCapsuleEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    // load first page
                    Napier.d("LoadType.REFRESH, page: 1")
                    1
                }

                LoadType.PREPEND -> {
                    // prepend not supported, return end of page
                    Napier.d("LoadType.PREPEND, Success(endOfPaginationReached = true)")
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val lastPage = state.lastItemOrNull()
                    if (lastPage == null) {
                        // no pages were loaded before, can load more
                        Napier.d("LoadType.APPEND; no pages were loaded before & can load more, Success(endOfPaginationReached = false)")
                        return MediatorResult.Success(endOfPaginationReached = false)
                    }

                    val pageData = lastPage.pageData
                    if (pageData == null || !pageData.hasNextPage) {
                        // no more pages, return end of page
                        Napier.d("LoadType.APPEND; end of page, Success(endOfPaginationReached = true)")
                        return MediatorResult.Success(endOfPaginationReached = true)
                    } else {
                        // load next page
                        Napier.d("LoadType.APPEND; load next page, page: ${pageData.page + 1}")
                        pageData.page + 1
                    }
                }
            }

            // get from remote
            val timeCapsules = remoteDataSource.getTimeCapsules(
                startDate = startDate,
                endDate = endDate,
                page = page,
                limit = state.config.pageSize,
                status = status
            )
            val endOfPaginationReached = timeCapsules.last().pageData?.hasNextPage?.not() ?: true
            Napier.d("new timeCapsules.size: ${timeCapsules.size}, endOfPaginationReached: $endOfPaginationReached")

            // save to local
            if (loadType == LoadType.REFRESH) {
                localDataSource.clearAll()
            }
            localDataSource.saveTimeCapsules(timeCapsules)

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
