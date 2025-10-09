package com.emotionstorage.data.dataSource

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.AccountInfo

interface UserRemoteDataSource {
    suspend fun updateUserNickname(nickname: String): DataState<Unit?>

    suspend fun getUserAccountInfo(): DataState<AccountInfo>
}
