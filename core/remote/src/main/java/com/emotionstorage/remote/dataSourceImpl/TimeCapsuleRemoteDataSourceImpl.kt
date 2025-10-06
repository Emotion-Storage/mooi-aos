package com.emotionstorage.remote.dataSourceImpl

import com.emotionstorage.data.dataSource.TimeCapsuleRemoteDataSource
import com.emotionstorage.remote.api.TimeCapsuleApiService
import com.emotionstorage.remote.response.ResponseStatus
import javax.inject.Inject

class TimeCapsuleRemoteDataSourceImpl @Inject constructor(
    private val timeCapsuleApiService: TimeCapsuleApiService,
) : TimeCapsuleRemoteDataSource {
    override suspend fun patchTimeCapsuleOpen(id: String): Boolean {
        try {
            val response = timeCapsuleApiService.patchTimeCapsuleOpen(id)
            if (response.status == ResponseStatus.OK.code) {
                return true
            } else {
                throw Exception("patchTimeCapsuleOpen api fail, $response")
            }
        } catch (e: Exception) {
            throw Exception("patchTimeCapsuleOpen api fail, $e")
        }
    }

}
