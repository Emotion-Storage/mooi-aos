package com.emotionstorage.data.remoteMediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.emotionstorage.data.dataSource.local.FavoriteTimeCapsuleRemoteKeyLocalDataSource
import com.emotionstorage.data.dataSource.local.TimeCapsuleLocalDataSource
import com.emotionstorage.data.dataSource.remote.TimeCapsuleRemoteDataSource
import com.emotionstorage.data.model.FavoriteTimeCapsuleRemoteKeyEntity
import com.emotionstorage.data.model.TimeCapsuleEntity
import io.github.aakira.napier.Napier
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class FavoriteTimeCapsuleRemoteMediator(
    private val timeCapsuleRemote: TimeCapsuleRemoteDataSource,
    private val timeCapsuleLocal: TimeCapsuleLocalDataSource,
    private val favoriteRemoteKeyLocal: FavoriteTimeCapsuleRemoteKeyLocalDataSource,
    private val sortBy: String,
) : RemoteMediator<Int, TimeCapsuleEntity>() {
    private val queryKey = FavoriteTimeCapsuleRemoteKeyEntity.generateQueryKey(sortBy)

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        val lastUpdated = favoriteRemoteKeyLocal.lastUpdated(queryKey)

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
                        val lastItem =
                            state.lastItemOrNull()
                                ?: return MediatorResult.Success(false)

                        val remoteKey = favoriteRemoteKeyLocal.remoteKeyById(lastItem.id, queryKey)

                        remoteKey?.nextPage
                            ?: return MediatorResult.Success(true)
                    }
                }

            // get from remote
            val favoriteTimeCapsules =
                timeCapsuleRemote.getFavoriteTimeCapsules(
                    sortBy = sortBy,
                    page = page,
                    limit = state.config.pageSize,
                )
            Napier.d("load - favorite timeCapsules: $favoriteTimeCapsules")

            val endOfPaginationReached = favoriteTimeCapsules.isEmpty()

            // clear cache on refresh
            if (loadType == LoadType.REFRESH) {
                favoriteRemoteKeyLocal.clearByQueryKey(queryKey)
                timeCapsuleLocal.clearFavorites()
            }

            // save to cache
            val now = System.currentTimeMillis()
            val keys =
                favoriteTimeCapsules.map {
                    FavoriteTimeCapsuleRemoteKeyEntity(
                        id = it.id,
                        queryKey = queryKey,
                        prevPage = if (page == 1) null else page - 1,
                        nextPage = if (endOfPaginationReached) null else page + 1,
                        lastUpdated = now,
                    )
                }
            favoriteRemoteKeyLocal.insertAll(keys)
            timeCapsuleLocal.saveTimeCapsules(favoriteTimeCapsules)

            MediatorResult.Success(endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
