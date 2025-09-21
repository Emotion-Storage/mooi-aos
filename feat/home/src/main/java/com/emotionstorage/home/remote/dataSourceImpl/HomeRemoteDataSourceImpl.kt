package com.emotionstorage.home.remote.dataSourceImpl

import com.emotionstorage.home.data.dataSource.HomeRemoteDataSource
import com.emotionstorage.home.data.model.HomeEntity
import com.emotionstorage.home.remote.api.HomeApiService
import com.emotionstorage.home.remote.modelMapper.HomeMapper
import javax.inject.Inject

class HomeRemoteDataSourceImpl @Inject constructor(
    private val homeApi: HomeApiService
) : HomeRemoteDataSource {
    override suspend fun getHome(): HomeEntity {
        try {
            val homeResult = homeApi.getHome()
            if (homeResult.status == 200 && homeResult.data != null)
                return HomeMapper.toData(homeResult.data!!)
            else throw Throwable("Response data is null!, ${homeResult}")
        } catch (e: Exception) {
            throw e
        }
    }
}