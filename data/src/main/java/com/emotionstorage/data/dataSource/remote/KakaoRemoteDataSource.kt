package com.emotionstorage.data.dataSource.remote

interface KakaoRemoteDataSource {
    suspend fun getIdToken(): String
}
