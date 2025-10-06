package com.emotionstorage.domain.repo

import com.emotionstorage.domain.common.DataState
import kotlinx.coroutines.flow.Flow

interface TimeCapsuleRepository {
    suspend fun openArrivedTimeCapsule(id: String): Flow<DataState<Unit>>
    suspend fun saveTimeCapsuleNote(id:String, note: String): Flow<DataState<Boolean>>
}
