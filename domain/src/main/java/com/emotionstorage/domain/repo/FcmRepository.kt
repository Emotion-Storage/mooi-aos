package com.emotionstorage.domain.repo

import com.emotionstorage.domain.common.DataState

interface FcmRepository {
    suspend fun registerToken(token: String): DataState<Unit>
    suspend fun saveToken(token: String): DataState<Unit>
    suspend fun getToken(): DataState<String>
    suspend fun deleteToken(): DataState<Unit>
}
