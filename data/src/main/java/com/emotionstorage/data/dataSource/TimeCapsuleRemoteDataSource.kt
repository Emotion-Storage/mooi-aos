package com.emotionstorage.data.dataSource

interface TimeCapsuleRemoteDataSource {
    suspend fun patchTimeCapsuleOpen(id: String): Boolean

    suspend fun patchTimeCapsuleNote(
        id: String,
        note: String,
    ): Boolean
}
