package com.emotionstorage.data.dataSource.remote

import com.emotionstorage.data.model.HomeEntity

interface HomeRemoteDataSource {
    suspend fun getHome(): HomeEntity
}
