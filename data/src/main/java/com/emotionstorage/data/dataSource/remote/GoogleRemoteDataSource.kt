package com.emotionstorage.data.dataSource.remote

interface GoogleRemoteDataSource {
    suspend fun getIdToken(): String
}
