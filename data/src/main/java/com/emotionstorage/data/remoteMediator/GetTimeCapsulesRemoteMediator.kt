package com.emotionstorage.data.remoteMediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.emotionstorage.data.dataSource.local.TimeCapsuleLocalDataSource
import com.emotionstorage.data.dataSource.remote.TimeCapsuleRemoteDataSource
import com.emotionstorage.data.model.TimeCapsuleEntity
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
                    1
                }

                LoadType.PREPEND -> {
                    // prepend not supported, return end of page
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    // if null, no pages were loaded before, can load more
                    val lastPage = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = false)

                    val pageData = lastPage.pageData
                    if (pageData == null || !pageData.hasNextPage) {
                        // no more pages, return end of page
                        return MediatorResult.Success(endOfPaginationReached = true)
                    } else {
                        // load next page
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
