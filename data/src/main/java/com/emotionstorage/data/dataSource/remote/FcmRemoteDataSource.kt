package com.emotionstorage.data.dataSource.remote

interface FcmRemoteDataSource {
    suspend fun registerToken(token: String): Boolean

    suspend fun deleteToken(token: String): Boolean
}
