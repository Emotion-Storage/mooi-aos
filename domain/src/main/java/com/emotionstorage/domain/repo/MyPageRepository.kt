package com.emotionstorage.domain.repo

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.AccountInfo
import com.emotionstorage.domain.model.MyPage
import kotlinx.coroutines.flow.Flow

interface MyPageRepository {
    suspend fun getMyPageOverview(): Flow<DataState<MyPage>>
    suspend fun getAccountInfo() : Flow<DataState<AccountInfo>>
}
