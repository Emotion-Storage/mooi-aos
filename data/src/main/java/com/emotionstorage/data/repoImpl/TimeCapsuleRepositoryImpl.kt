package com.emotionstorage.data.repoImpl

import com.emotionstorage.data.dataSource.remote.FavoriteResultEntity
import com.emotionstorage.data.dataSource.remote.TimeCapsuleRemoteDataSource
import com.emotionstorage.data.modelMapper.TimeCapsuleMapper
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.repo.FavoriteSortBy
import com.emotionstorage.domain.repo.FavoriteResult
import com.emotionstorage.domain.repo.TimeCapsuleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

private const val PAGE_LIMIT: Int = 30

class TimeCapsuleRepositoryImpl @Inject constructor(
    private val remoteDataSource: TimeCapsuleRemoteDataSource,
) : TimeCapsuleRepository {
    override suspend fun openArrivedTimeCapsule(id: Long): Flow<DataState<Unit>> =
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

    override suspend fun getFavoriteTimeCapsules(sortBy: FavoriteSortBy): Flow<DataState<List<TimeCapsule>>> =
        flow {
            emit(DataState.Loading(isLoading = true))
            try {
                val result =
                    remoteDataSource.getFavoriteTimeCapsules(
                        page = 1,
                        limit = PAGE_LIMIT,
                        sortBy =
                            when (sortBy) {
                                FavoriteSortBy.FAVORITE_AT -> "favorite"
                                FavoriteSortBy.NEWEST -> "latest"
                            },
                    )
                emit(DataState.Success(result.map { TimeCapsuleMapper.toDomain(it) }))
            } catch (e: Exception) {
                emit(DataState.Error(e))
            } finally {
                emit(DataState.Loading(isLoading = false))
            }
        }

    override suspend fun getTimeCapsules(
        startDate: LocalDate,
        endDate: LocalDate,
        page: Int,
        status: String,
    ): Flow<DataState<List<TimeCapsule>>> =
        flow {
            emit(DataState.Loading(isLoading = true))
            try {
                val result =
                    remoteDataSource.getTimeCapsules(
                        startDate = startDate,
                        endDate = endDate,
                        page = page,
                        limit = PAGE_LIMIT,
                        status = status,
                    )
                emit(DataState.Success(result.map { TimeCapsuleMapper.toDomain(it) }))
            } catch (e: Exception) {
                emit(DataState.Error(e))
            } finally {
                emit(DataState.Loading(isLoading = false))
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
