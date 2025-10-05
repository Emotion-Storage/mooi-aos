package com.emotionstorage.domain.repo

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.Home
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getHome(): Flow<DataState<Home>>
}
