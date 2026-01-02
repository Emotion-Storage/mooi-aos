package com.emotionstorage.data.dataSource.local

import kotlinx.coroutines.flow.Flow

interface ChatTempSaveLocalDataSource {
    val tempSavedRoomId: Flow<Long?>

    suspend fun setTempSavedRoomId(roomId: Long)

    suspend fun clearTempSavedRoomId()
}
