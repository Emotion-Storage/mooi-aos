package com.emotionstorage.remote.dataSourceImpl

import com.emotionstorage.data.dataSource.TimeCapsuleRemoteDataSource
import com.emotionstorage.data.model.TimeCapsuleEntity
import com.emotionstorage.remote.api.TimeCapsuleApiService
import com.emotionstorage.remote.modelMapper.TimeCapsuleResponseMapper
import com.emotionstorage.remote.request.timeCapsule.PatchTimeCapsuleNoteRequest
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

    override suspend fun patchTimeCapsuleNote(
        id: String,
        note: String,
    ): Boolean {
        try {
            val response =
                timeCapsuleApiService.patchTimeCapsuleNote(
                    id,
                    PatchTimeCapsuleNoteRequest(note),
                )
            if (response.status == ResponseStatus.Created.code) {
                return true
            } else {
                throw Exception("patchTimeCapsuleOpen api fail, $response")
            }
        } catch (e: Exception) {
            throw Exception("patchTimeCapsuleOpen api fail, $e")
        }
    }

    override suspend fun getFavoriteTimeCapsules(sortBy: String): List<TimeCapsuleEntity> {
        try {
            // todo: implement pagination
            val response =
                timeCapsuleApiService.getFavoriteTimeCapsules(
                    page = 1,
                    limit = 30,
                    sortBy = sortBy,
                )
            if (response.status == ResponseStatus.OK.code && response.data != null) {
                return TimeCapsuleResponseMapper.toData(response.data!!)
            } else {
                throw Exception("patchTimeCapsuleOpen api fail, $response")
            }
        } catch (e: Exception) {
            throw Exception("patchTimeCapsuleOpen api fail, $e")
        }
    }
}
