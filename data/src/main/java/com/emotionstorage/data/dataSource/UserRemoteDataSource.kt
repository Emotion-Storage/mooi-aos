package com.emotionstorage.data.dataSource

import com.emotionstorage.domain.common.DataState

interface UserRemoteDataSource {
    suspend fun updateUserNickname(nickname: String): DataState<Unit?>
}
