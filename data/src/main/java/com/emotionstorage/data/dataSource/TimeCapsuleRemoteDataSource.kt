package com.emotionstorage.data.dataSource

interface TimeCapsuleRemoteDataSource {
    suspend fun patchTimeCapsuleOpen(id: String): Boolean

}
