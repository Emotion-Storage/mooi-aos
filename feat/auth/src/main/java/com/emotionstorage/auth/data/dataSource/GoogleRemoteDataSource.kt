package com.emotionstorage.auth.data.dataSource

import com.emotionstorage.domain.common.DataState

interface GoogleRemoteDataSource {
    suspend fun getIdToken(): String
}