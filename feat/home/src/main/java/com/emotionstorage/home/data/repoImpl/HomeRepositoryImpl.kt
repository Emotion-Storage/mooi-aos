package com.emotionstorage.home.data.repoImpl

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.home.data.dataSource.HomeRemoteDataSource
import com.emotionstorage.home.data.modelMapper.HomeMapper
import com.emotionstorage.home.domain.model.Home
import com.emotionstorage.home.domain.repo.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeRemoteDataSource: HomeRemoteDataSource
) : HomeRepository {
    override suspend fun getHome(): Flow<DataState<Home>> = flow {
        emit(DataState.Loading(isLoading = true))
        try {
            DataState.Success(
                HomeMapper.toDomain(homeRemoteDataSource.getHome())
            )
        } catch (e: Exception) {
            emit(DataState.Error(e))
        } finally {
            emit(DataState.Loading(isLoading = false))
        }
    }
}