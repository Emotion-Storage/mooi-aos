package com.emotionstorage.remote.dataSourceImpl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.emotionstorage.data.dataSource.local.FavoriteTimeCapsuleRemoteKeyLocalDataSource
import com.emotionstorage.data.dataSource.local.PagedTimeCapsuleDataSource
import com.emotionstorage.data.dataSource.local.TimeCapsuleLocalDataSource
import com.emotionstorage.data.dataSource.local.TimeCapsuleRemoteKeyLocalDataSource
import com.emotionstorage.data.dataSource.remote.TimeCapsuleRemoteDataSource
import com.emotionstorage.data.model.TimeCapsuleEntity
import com.emotionstorage.domain.repo.FavoriteSortBy
import com.emotionstorage.local.model.TimeCapsuleLocal
import com.emotionstorage.local.modelMapper.TimeCapsuleLocalMapper
import com.emotionstorage.local.room.dao.TimeCapsuleDao
import com.emotionstorage.remote.remoteMediator.FavoriteTimeCapsuleRemoteMediator
import com.emotionstorage.remote.remoteMediator.TimeCapsuleRemoteMediator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

private const val PAGE_SIZE = 15

class PagedTimeCapsuleDataSourceImpl @Inject constructor(
    private val timeCapsuleDao: TimeCapsuleDao,
    private val timeCapsuleRemote: TimeCapsuleRemoteDataSource,
    private val timeCapsuleLocal: TimeCapsuleLocalDataSource,
    private val remoteKeyLocal: TimeCapsuleRemoteKeyLocalDataSource,
    private val favoriteRemoteKeyLocal: FavoriteTimeCapsuleRemoteKeyLocalDataSource,
) : PagedTimeCapsuleDataSource {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagedTimeCapsules(
        status: String,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Flow<PagingData<TimeCapsuleEntity>> =
        Pager<Int, TimeCapsuleLocal>(
            config =
                PagingConfig(
                    pageSize = PAGE_SIZE,
                    enablePlaceholders = false,
                ),
            remoteMediator =
                TimeCapsuleRemoteMediator(
                    timeCapsuleRemote = timeCapsuleRemote,
                    timeCapsuleLocal = timeCapsuleLocal,
                    remoteKeyLocal = remoteKeyLocal,
                    status = status,
                    startDate = startDate,
                    endDate = endDate,
                ),
            pagingSourceFactory = {
                timeCapsuleDao.pagingSource(
                    status,
                    startDate = startDate.atStartOfDay(),
                    endDate = endDate.atTime(LocalTime.MAX),
                )
            },
        ).flow.map {
            it.map { local ->
                TimeCapsuleLocalMapper.toData(local)
            }
        }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagedFavoriteTimeCapsules(sortBy: FavoriteSortBy): Flow<PagingData<TimeCapsuleEntity>> =
        Pager(
            config =
                PagingConfig(
                    pageSize = PAGE_SIZE,
                    enablePlaceholders = false,
                ),
            remoteMediator =
                FavoriteTimeCapsuleRemoteMediator(
                    timeCapsuleRemote = timeCapsuleRemote,
                    timeCapsuleLocal = timeCapsuleLocal,
                    favoriteRemoteKeyLocal = favoriteRemoteKeyLocal,
                    sortBy = sortBy.value,
                ),
            pagingSourceFactory = {
                timeCapsuleDao.favoritePagingSource(sortBy.value)
            },
        ).flow.map {
            it.map { local ->
                TimeCapsuleLocalMapper.toData(local)
            }
        }
}
