package com.emotionstorage.home.domain.repo

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.home.domain.model.Home
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getHome(): Flow<DataState<Home>>
}