package com.emotionstorage.auth.data.dataSource

import com.emotionstorage.common.DataResource

interface KakaoRemoteDataSource {
    suspend fun getIdToken(): DataResource<String>
}