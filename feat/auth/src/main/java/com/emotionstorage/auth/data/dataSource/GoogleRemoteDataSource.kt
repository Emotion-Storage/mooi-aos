package com.emotionstorage.auth.data.dataSource

interface GoogleRemoteDataSource {
    suspend fun getIdToken(): String
}
