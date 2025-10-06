package com.emotionstorage.remote.dataSourceImpl

import com.emotionstorage.data.dataSource.TimeCapsuleRemoteDataSource
import com.emotionstorage.data.model.TimeCapsuleEntity
import com.emotionstorage.remote.api.TimeCapsuleApiService
import com.emotionstorage.remote.modelMapper.TimeCapsuleResponseMapper
import com.emotionstorage.remote.request.timeCapsule.PatchTimeCapsuleNoteRequest
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

class TimeCapsuleRemoteDataSourceImpl @Inject constructor(
    private val timeCapsuleApiService: TimeCapsuleApiService,
) : TimeCapsuleRemoteDataSource {
    override suspend fun patchTimeCapsuleOpen(id: String): Boolean {
        try {
            timeCapsuleApiService.patchTimeCapsuleOpen(id)
            return true
        } catch (e: Exception) {
            throw Exception("patchTimeCapsuleOpen api fail, $e")
        }
    }

    override suspend fun patchTimeCapsuleNote(
        id: String,
        note: String,
    ): Boolean {
        try {
            timeCapsuleApiService.patchTimeCapsuleNote(
                id,
                PatchTimeCapsuleNoteRequest(note),
            )
            return true
        } catch (e: Exception) {
            throw Exception("patchTimeCapsuleNote api fail, $e")
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
            if (response.data != null) {
                return TimeCapsuleResponseMapper.toData(response.data!!)
            } else {
                throw Exception("getFavoriteTimeCapsules reponse data is empty, $response")
            }
        } catch (e: Exception) {
            throw Exception("getFavoriteTimeCapsules api fail, $e")
        }
    }

    override suspend fun getTimeCapsuleDates(yearMonth: YearMonth): List<LocalDate> {
        try {
            val response =
                timeCapsuleApiService.getTimeCapsuleDates(yearMonth.year, yearMonth.monthValue)
            if (response.data != null) {
                return response.data!!.dates.map { LocalDate.parse(it) }
            } else {
                throw Exception("getTimeCapsuleDates reponse data is empty, $response")
            }
        } catch (e: Exception) {
            throw Exception("getTimeCapsuleDates api fail, $e")
        }
    }

    override suspend fun deleteTimeCapsule(id: String): Boolean {
        try {
            timeCapsuleApiService.deleteTimeCapsule(id)
            return true
        } catch (e: Exception) {
            throw Exception("deleteTimeCapsule api fail, $e")
        }
    }
}
