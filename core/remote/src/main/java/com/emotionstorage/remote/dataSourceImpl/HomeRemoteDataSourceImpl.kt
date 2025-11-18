package com.emotionstorage.remote.dataSourceImpl

import com.emotionstorage.data.dataSource.remote.HomeRemoteDataSource
import com.emotionstorage.data.model.HomeEntity
import com.emotionstorage.remote.api.HomeApiService
import com.emotionstorage.remote.modelMapper.HomeMapper
import javax.inject.Inject

class HomeRemoteDataSourceImpl
    @Inject
    constructor(
        private val homeApi: HomeApiService,
    ) : HomeRemoteDataSource {
        override suspend fun getHome(): HomeEntity {
            try {
                val homeResponse = homeApi.getHome()
                if (homeResponse.data != null) {
                    return HomeMapper.toData(homeResponse.data!!)
                } else {
                    throw Throwable("Response data is null!, $homeResponse")
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }
