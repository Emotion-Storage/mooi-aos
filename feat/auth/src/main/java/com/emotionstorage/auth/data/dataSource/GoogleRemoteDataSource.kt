package com.emotionstorage.auth.data.dataSource

import com.emotionstorage.common.DataResource

interface GoogleRemoteDataSource {
    suspend fun getIdToken(): DataResource<String>
}