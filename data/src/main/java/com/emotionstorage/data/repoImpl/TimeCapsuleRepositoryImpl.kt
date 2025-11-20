package com.emotionstorage.data.repoImpl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.emotionstorage.data.dataSource.local.TimeCapsuleLocalDataSource
import com.emotionstorage.data.dataSource.remote.FavoriteResultEntity
import com.emotionstorage.data.dataSource.remote.TimeCapsuleRemoteDataSource
import com.emotionstorage.data.modelMapper.TimeCapsuleMapper
import com.emotionstorage.data.pagingSource.GetFavoriteTimeCapsulesPagingSource
import com.emotionstorage.data.pagingSource.GetTimeCapsulesPagingSource
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.repo.FavoriteSortBy
import com.emotionstorage.domain.repo.FavoriteResult
import com.emotionstorage.domain.repo.TimeCapsuleRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import javax.inject.Inject

private const val PAGE_SIZE = 15

class TimeCapsuleRepositoryImpl @Inject constructor(
    private val remoteDataSource: TimeCapsuleRemoteDataSource,
    private val localDataSource: TimeCapsuleLocalDataSource,
) : TimeCapsuleRepository {
    override suspend fun setTimeCapsuleOpenAt(
        id: Long,
        openAt: LocalDateTime,
    ): DataState<Unit> =
        try {
            if (remoteDataSource.postTimeCapsuleOpenAt(id, openAt)) {
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
                remoteDataSource.patchTimeCapsuleOpen(id)
                emit(DataState.Success(Unit))
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
                emit(DataState.Success(remoteDataSource.patchTimeCapsuleNote(id, note)))
            } catch (e: Exception) {
                emit(DataState.Error(e))
            } finally {
                emit(DataState.Loading(isLoading = false))
            }
        }

    override suspend fun setFavoriteTimeCapsule(
        id: Long,
        isFavorite: Boolean,
    ): Flow<DataState<FavoriteResult>> =
        flow {
            emit(DataState.Loading(isLoading = true))
            try {
                val result = remoteDataSource.patchTimeCapsuleFavorite(id, isFavorite)
                emit(
                    DataState.Success(
                        when (result) {
                            FavoriteResultEntity.ADDED -> FavoriteResult.ADDED
                            FavoriteResultEntity.REMOVED -> FavoriteResult.REMOVED
                            FavoriteResultEntity.FULL -> FavoriteResult.FULL
                        },
                    ),
                )
            } catch (e: Exception) {
                emit(DataState.Error(e))
            } finally {
                emit(DataState.Loading(isLoading = false))
            }
        }

    override fun getPagedFavoriteTimeCapsules(sortBy: FavoriteSortBy): Flow<PagingData<TimeCapsule>> {
        Napier.d("getPagedFavoriteTimeCapsules: sortBy: $sortBy")
        return Pager(
            config =
                PagingConfig(
                    pageSize = PAGE_SIZE,
                    enablePlaceholders = false,
                ),
            pagingSourceFactory =
                {
                    GetFavoriteTimeCapsulesPagingSource(
                        remoteDataSource = remoteDataSource,
                        sortBy =
                            when (sortBy) {
                                FavoriteSortBy.FAVORITE_AT -> "favorite"
                                FavoriteSortBy.NEWEST -> "latest"
                            },
                    )
                },
        ).flow.map {
            it.map { entity ->
                TimeCapsuleMapper.toDomain(entity)
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagedTimeCapsules(
        startDate: LocalDate,
        endDate: LocalDate,
        status: String,
    ): Flow<PagingData<TimeCapsule>> {
        Napier.d("getPagedTimeCapsules: startDate: $startDate, endDate: $endDate, status: $status")
        return Pager(
            config =
                PagingConfig(
                    pageSize = PAGE_SIZE,
                    enablePlaceholders = false,
                ),
            pagingSourceFactory = {
                GetTimeCapsulesPagingSource(
                    remoteDataSource = remoteDataSource,
                    startDate = startDate,
                    endDate = endDate,
                    status = status,
                )
            },
        ).flow.map {
            it.map { entity ->
                TimeCapsuleMapper.toDomain(entity)
            }
        }
    }

    override suspend fun getTimeCapsuleById(id: Long): Flow<DataState<TimeCapsule>> =
        flow {
            emit(DataState.Loading(isLoading = true))
            try {
                val result = remoteDataSource.getTimeCapsuleDetail(id)
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
                emit(DataState.Success(remoteDataSource.getTimeCapsuleDates(yearMonth)))
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
                emit(DataState.Success(remoteDataSource.deleteTimeCapsule(id)))
            } catch (e: Exception) {
                emit(DataState.Error(e))
            } finally {
                emit(DataState.Loading(isLoading = false))
            }
        }
}
