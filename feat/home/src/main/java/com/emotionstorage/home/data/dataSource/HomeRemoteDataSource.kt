package com.emotionstorage.home.data.dataSource

import com.emotionstorage.home.data.model.HomeEntity

interface HomeRemoteDataSource {
    suspend fun getHome(): HomeEntity
}