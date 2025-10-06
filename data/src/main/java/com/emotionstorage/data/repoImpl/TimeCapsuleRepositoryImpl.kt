package com.emotionstorage.data.repoImpl

import com.emotionstorage.data.dataSource.TimeCapsuleRemoteDataSource
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.repo.FavoriteSortBy
import com.emotionstorage.domain.repo.TimeCapsuleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TimeCapsuleRepositoryImpl @Inject constructor(
    private val timeCapsuleRemoteDataSource: TimeCapsuleRemoteDataSource,
) : TimeCapsuleRepository {
    override suspend fun openArrivedTimeCapsule(id: String): Flow<DataState<Unit>> =
        flow {
            emit(DataState.Loading(isLoading = true))
            try {
                timeCapsuleRemoteDataSource.patchTimeCapsuleOpen(id)
                emit(DataState.Success(Unit))
            } catch (e: Exception) {
                emit(DataState.Error(e))
            } finally {
                emit(DataState.Loading(isLoading = false))
            }
        }

    override suspend fun saveTimeCapsuleNote(
        id: String,
        note: String,
    ): Flow<DataState<Boolean>> =
        flow {
            emit(DataState.Loading(isLoading = true))
            try {
                emit(DataState.Success(timeCapsuleRemoteDataSource.patchTimeCapsuleNote(id, note)))
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
                // todo: implement function
//            val result = timeCapsuleRemoteDataSource.getFavoriteTimeCapsules(sortBy.label)
//            emit(DataState.Success(result))
            } catch (e: Exception) {
                emit(DataState.Error(e))
            } finally {
                emit(DataState.Loading(isLoading = false))
            }
        }
}
