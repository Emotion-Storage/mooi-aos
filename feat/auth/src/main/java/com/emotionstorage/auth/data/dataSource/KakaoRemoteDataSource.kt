package com.emotionstorage.auth.data.dataSource

interface KakaoRemoteDataSource {
    suspend fun getIdToken(): String
}
