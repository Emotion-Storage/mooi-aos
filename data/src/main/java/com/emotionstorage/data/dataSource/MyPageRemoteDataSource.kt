package com.emotionstorage.data.dataSource

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.MyPage

interface MyPageRemoteDataSource {
    suspend fun getMyPageOverview(): DataState<MyPage>
}
