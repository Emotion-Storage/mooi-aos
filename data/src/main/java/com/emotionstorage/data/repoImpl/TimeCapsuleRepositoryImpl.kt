package com.emotionstorage.data.repoImpl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.emotionstorage.data.dataSource.local.FavoriteTimeCapsuleRemoteKeyLocalDataSource
import com.emotionstorage.data.dataSource.local.TimeCapsuleLocalDataSource
import com.emotionstorage.data.dataSource.local.TimeCapsuleRemoteKeyLocalDataSource
import com.emotionstorage.data.dataSource.remote.TimeCapsuleRemoteDataSource
import com.emotionstorage.data.modelMapper.TimeCapsuleMapper
import com.emotionstorage.data.remoteMediator.FavoriteTimeCapsuleRemoteMediator
import com.emotionstorage.data.remoteMediator.TimeCapsuleRemoteMediator
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.repo.FavoriteSortBy
import com.emotionstorage.domain.repo.TimeCapsuleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import javax.inject.Inject

private const val PAGE_SIZE = 15

class TimeCapsuleRepositoryImpl @Inject constructor(
    private val timeCapsuleRemote: TimeCapsuleRemoteDataSource,
    private val timeCapsuleLocal: TimeCapsuleLocalDataSource,
    private val remoteKeyLocal: TimeCapsuleRemoteKeyLocalDataSource,
    private val favoriteRemoteKeyLocal: FavoriteTimeCapsuleRemoteKeyLocalDataSource,
) : TimeCapsuleRepository {
    override suspend fun setTimeCapsuleOpenAt(
        id: Long,
        openAt: LocalDateTime,
    ): DataState<Unit> =
        try {
            if (timeCapsuleRemote.postTimeCapsuleOpenAt(id, openAt)) {
                DataState.Success(Unit)
            } else {
                DataState.Error(Throwable("Failed to set time capsule open at"))
            }
        } catch (e: Exception) {
            DataState.Error(e)
        }

    override suspend fun openTimeCapsule(id: Long): Flow<DataState<Unit>> =
        flow {
            emit(DataState.Loading(isLoading = true))
            try {
                if (timeCapsuleRemote.patchTimeCapsuleOpen(id)) {
                    emit(DataState.Success(Unit))
                } else {
                    emit(DataState.Error(Throwable("Failed to open time capsule")))
                }
            } catch (e: Exception) {
                emit(DataState.Error(e))
            } finally {
                emit(DataState.Loading(isLoading = false))
            }
        }

    override suspend fun saveTimeCapsuleNote(
        id: Long,
        note: String,
    ): Flow<DataState<Boolean>> =
        flow {
            emit(DataState.Loading(isLoading = true))
            try {
                emit(DataState.Success(timeCapsuleRemote.patchTimeCapsuleNote(id, note)))
            } catch (e: Exception) {
                emit(DataState.Error(e))
            } finally {
                emit(DataState.Loading(isLoading = false))
            }
        }

    override suspend fun setFavoriteTimeCapsule(
        id: Long,
        isFavorite: Boolean,
    ): DataState<Boolean> =
        try {
            timeCapsuleRemote.patchTimeCapsuleFavorite(id, isFavorite)
        } catch (e: Exception) {
            DataState.Error(e)
        }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagedFavoriteTimeCapsules(sortBy: FavoriteSortBy): Flow<PagingData<TimeCapsule>> =
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
                timeCapsuleLocal.getFavoritePagingSource(sortBy.value)
            },
        ).flow.map {
            it.map { entity ->
                TimeCapsuleMapper.toDomain(entity)
            }
        }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagedTimeCapsules(
        status: String,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Flow<PagingData<TimeCapsule>> =
        Pager(
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
                timeCapsuleLocal.getPagingSource(status, startDate, endDate)
            },
        ).flow.map {
            it.map { entity ->
                TimeCapsuleMapper.toDomain(entity)
            }
        }

    override suspend fun getTimeCapsuleById(id: Long): Flow<DataState<TimeCapsule>> =
        flow {
            emit(DataState.Loading(isLoading = true))
            try {
                val result = timeCapsuleRemote.getTimeCapsuleDetail(id)
                emit(DataState.Success(TimeCapsuleMapper.toDomain(result)))
            } catch (e: Exception) {
                emit(DataState.Error(e))
            } finally {
                emit(DataState.Loading(isLoading = false))
            }
        }

    override suspend fun getTimeCapsuleDates(yearMonth: YearMonth): Flow<DataState<List<LocalDate>>> =
        flow {
            emit(DataState.Loading(isLoading = true))
            try {
                emit(DataState.Success(timeCapsuleRemote.getTimeCapsuleDates(yearMonth)))
            } catch (e: Exception) {
                emit(DataState.Error(e))
            } finally {
                emit(DataState.Loading(isLoading = false))
            }
        }

    override suspend fun deleteTimeCapsule(id: Long): Flow<DataState<Boolean>> =
        flow {
            emit(DataState.Loading(isLoading = true))
            try {
                emit(DataState.Success(timeCapsuleRemote.deleteTimeCapsule(id)))
            } catch (e: Exception) {
                emit(DataState.Error(e))
            } finally {
                emit(DataState.Loading(isLoading = false))
            }
        }

    override suspend fun createTimeCapsule(id: Long): DataState<Long> =
        try {
            val timeCapsuleId = timeCapsuleRemote.createTimeCapsule(id)
            DataState.Success(timeCapsuleId)
        } catch (e: Exception) {
            DataState.Error(e)
        }
}
