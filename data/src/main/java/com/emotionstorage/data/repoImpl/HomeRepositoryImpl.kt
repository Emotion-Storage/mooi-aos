package com.emotionstorage.data.repoImpl

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.data.dataSource.remote.HomeRemoteDataSource
import com.emotionstorage.data.modelMapper.HomeMapper
import com.emotionstorage.domain.model.Home
import com.emotionstorage.domain.repo.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepositoryImpl
    @Inject
    constructor(
        private val homeRemoteDataSource: HomeRemoteDataSource,
    ) : HomeRepository {
        override suspend fun getHome(): Flow<DataState<Home>> =
            flow {
                emit(DataState.Loading(isLoading = true))
                try {
                    val homeResult = HomeMapper.toDomain(homeRemoteDataSource.getHome())
                    emit(DataState.Success(homeResult))
                } catch (e: Exception) {
                    emit(DataState.Error(e))
                } finally {
                    emit(DataState.Loading(isLoading = false))
                }
            }
    }
