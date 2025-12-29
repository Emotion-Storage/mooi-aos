package com.emotionstorage.data.dataSource.remote

import com.emotionstorage.domain.common.DataState

interface ReissueRemoteDataSource {
    suspend fun reissueAccessToken(): DataState<String>
}
