package com.emotionstorage.data.remoteMediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.emotionstorage.data.dataSource.local.TimeCapsuleLocalDataSource
import com.emotionstorage.data.dataSource.local.TimeCapsuleRemoteKeyLocalDataSource
import com.emotionstorage.data.dataSource.remote.TimeCapsuleRemoteDataSource
import com.emotionstorage.data.model.TimeCapsuleEntity
import com.emotionstorage.data.model.TimeCapsuleRemoteKeyEntity
import io.github.aakira.napier.Napier
import java.time.LocalDate
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class TimeCapsuleRemoteMediator(
    private val timeCapsuleRemote: TimeCapsuleRemoteDataSource,
    private val timeCapsuleLocal: TimeCapsuleLocalDataSource,
    private val remoteKeyLocal: TimeCapsuleRemoteKeyLocalDataSource,
    private val startDate: LocalDate,
    private val endDate: LocalDate,
    private val status: String,
) : RemoteMediator<Int, TimeCapsuleEntity>() {

    private val queryKey = TimeCapsuleRemoteKeyEntity.generateQueryKey(status, startDate, endDate)

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        val lastUpdated = remoteKeyLocal.lastUpdated(queryKey)

        return if (lastUpdated != null &&
            System.currentTimeMillis() - lastUpdated <= cacheTimeout
        ) {
            // Cached data is up-to-date, so there is no need to re-fetch
            // from the network.
            Napier.d("initialize - Cache timeout not reached; InitializeAction.SKIP_INITIAL_REFRESH")
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            // Need to refresh cached data from network; returning
            // LAUNCH_INITIAL_REFRESH here will also block RemoteMediator's
            // APPEND and PREPEND from running until REFRESH succeeds.
            Napier.d("initialize - Cache timeout reached; InitializeAction.LAUNCH_INITIAL_REFRESH")
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TimeCapsuleEntity>,
    ): MediatorResult {
        return try {
            val page =
                when (loadType) {
                    LoadType.REFRESH -> {
                        1
                    }

                    LoadType.PREPEND -> {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    LoadType.APPEND -> {
                        val lastItem = state.lastItemOrNull()
                            ?: return MediatorResult.Success(false)

                        val remoteKey = remoteKeyLocal.remoteKeyById(lastItem.id, queryKey)

                        remoteKey?.nextPage
                            ?: return MediatorResult.Success(true)
                    }
                }

            // get from remote
            val timeCapsules =
                timeCapsuleRemote.getTimeCapsules(
                    status = status,
                    startDate = startDate,
                    endDate = endDate,
                    page = page,
                    limit = state.config.pageSize,
                )

            val endOfPaginationReached = timeCapsules.isEmpty()

            // clear cache on refresh
            if (loadType == LoadType.REFRESH) {
                remoteKeyLocal.clearByQueryKey(queryKey)
                timeCapsuleLocal.clearByCondition(status, startDate, endDate)
            }

            // save to cache
            val now = System.currentTimeMillis()
            val keys = timeCapsules.map{
                TimeCapsuleRemoteKeyEntity(
                    id = it.id,
                    queryKey = queryKey,
                    prevPage = if (page == 1) null else page - 1,
                    nextPage = if (endOfPaginationReached) null else page + 1,
                    lastUpdated = now,
                )
            }
            remoteKeyLocal.insertAll(keys)
            timeCapsuleLocal.saveTimeCapsules(timeCapsules)

            MediatorResult.Success(endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
