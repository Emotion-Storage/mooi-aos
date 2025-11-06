package com.emotionstorage.remote.dataSourceImpl

import com.emotionstorage.data.dataSource.remote.FcmRemoteDataSource
import com.emotionstorage.remote.api.FcmApiService
import com.emotionstorage.remote.request.fcm.FcmTokenRequest
import javax.inject.Inject

class FcmRemoteDataSourceImpl @Inject constructor(
    private val apiService: FcmApiService,
) : FcmRemoteDataSource {
    override suspend fun registerToken(token: String): Boolean {
        apiService.postFcmToken(FcmTokenRequest(token))
        return true
    }

    override suspend fun deleteToken(token: String): Boolean {
        apiService.deleteFcmToken(FcmTokenRequest(token))
        return true
    }
}
