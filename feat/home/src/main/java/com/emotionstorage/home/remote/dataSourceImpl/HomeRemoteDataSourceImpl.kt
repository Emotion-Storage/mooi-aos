package com.emotionstorage.home.remote.dataSourceImpl

import com.emotionstorage.home.data.dataSource.HomeRemoteDataSource
import com.emotionstorage.home.data.model.HomeEntity
import com.emotionstorage.home.remote.api.HomeApiService
import com.emotionstorage.home.remote.modelMapper.HomeMapper
import com.orhanobut.logger.Logger
import javax.inject.Inject

class HomeRemoteDataSourceImpl @Inject constructor(
    private val homeApi: HomeApiService
) : HomeRemoteDataSource {
    override suspend fun getHome(): HomeEntity {
        try {
            val homeResponse = homeApi.getHome()
            if (homeResponse.data != null)
                return HomeMapper.toData(homeResponse.data!!)
            else throw Throwable("Response data is null!, ${homeResponse}")
        } catch (e: Exception) {
            throw e
        }
    }
}