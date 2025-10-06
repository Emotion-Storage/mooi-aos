package com.emotionstorage.data.repoImpl

import com.emotionstorage.data.dataSource.TimeCapsuleRemoteDataSource
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.repo.TimeCapsuleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TimeCapsuleRepositoryImpl @Inject constructor(
    private val timeCapsuleRemoteDataSource: TimeCapsuleRemoteDataSource,
) : TimeCapsuleRepository {
    override suspend fun openArrivedTimeCapsule(id: String): Flow<DataState<Unit>> = flow {
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

}
