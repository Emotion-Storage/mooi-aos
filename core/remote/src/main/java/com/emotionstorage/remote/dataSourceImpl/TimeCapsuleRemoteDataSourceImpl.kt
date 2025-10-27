package com.emotionstorage.remote.dataSourceImpl

import com.emotionstorage.data.dataSource.TimeCapsuleRemoteDataSource
import com.emotionstorage.data.model.TimeCapsuleEntity
import com.emotionstorage.remote.api.TimeCapsuleApiService
import com.emotionstorage.remote.modelMapper.TimeCapsuleResponseMapper
import com.emotionstorage.remote.request.timeCapsule.PatchTimeCapsuleFavoriteRequest
import com.emotionstorage.remote.request.timeCapsule.PatchTimeCapsuleNoteRequest
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class TimeCapsuleRemoteDataSourceImpl @Inject constructor(
    private val apiService: TimeCapsuleApiService,
) : TimeCapsuleRemoteDataSource {
    override suspend fun patchTimeCapsuleOpen(id: Long): Boolean {
        try {
            apiService.patchTimeCapsuleOpen(id)
            return true
        } catch (e: Exception) {
            throw Exception("patchTimeCapsuleOpen api fail, $e")
        }
    }

    override suspend fun patchTimeCapsuleNote(
        id: Long,
        note: String,
    ): Boolean {
        try {
            apiService.patchTimeCapsuleNote(
                id,
                PatchTimeCapsuleNoteRequest(note),
            )
            return true
        } catch (e: Exception) {
            throw Exception("patchTimeCapsuleNote api fail, $e")
        }
    }

    override suspend fun patchTimeCapsuleFavorite(
        id: Long,
        isFavorite: Boolean,
    ): Boolean {
        try {
            val response =
                apiService.patchTimeCapsuleFavorite(
                    id,
                    PatchTimeCapsuleFavoriteRequest(isFavorite),
                )

            // todo: handle time capsule favorite fail - list is full
            if (response.data != null) {
                return response.data!!.isFavorite
            } else {
                throw Exception("patchTimeCapsuleFavorite response data is empty, $response")
            }
        } catch (e: Exception) {
            throw Exception("patchTimeCapsuleFavorite api fail, $e")
        }
    }

    override suspend fun getFavoriteTimeCapsules(
        page: Int,
        limit: Int,
        sortBy: String,
    ): List<TimeCapsuleEntity> {
        try {
            val response =
                apiService.getFavoriteTimeCapsules(
                    page = page,
                    limit = limit,
                    sortBy = sortBy,
                )
            if (response.data != null) {
                return TimeCapsuleResponseMapper.toData(response.data!!)
            } else {
                throw Exception("getFavoriteTimeCapsules response data is empty, $response")
            }
        } catch (e: Exception) {
            throw Exception("getFavoriteTimeCapsules api fail, $e")
        }
    }

    override suspend fun getTimeCapsules(
        startDate: LocalDate,
        endDate: LocalDate,
        page: Int,
        limit: Int,
        status: String,
    ): List<TimeCapsuleEntity> {
        try {
            val response =
                apiService.getTimeCapsules(
                    startDate = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    endDate = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    page = page,
                    limit = limit,
                    status = status,
                )
            if (response.data != null) {
                return TimeCapsuleResponseMapper.toData(response.data!!)
            } else {
                throw Exception("getTimeCapsules response data is empty, $response")
            }
        } catch (e: Exception) {
            throw Exception("getTimeCapsules api fail, $e")
        }
    }

    override suspend fun getTimeCapsuleDetail(id: Long): TimeCapsuleEntity {
        try {
            val response = apiService.getTimeCapsuleDetail(id)
            if (response.data != null) {
                return TimeCapsuleResponseMapper.toData(response.data!!)
            } else {
                throw Exception("getTimeCapsuleDetail response data is empty, $response")
            }
        } catch (e: Exception) {
            throw Exception("getTimeCapsuleDetail api fail, $e")
        }
    }

    override suspend fun getTimeCapsuleDates(yearMonth: YearMonth): List<LocalDate> {
        try {
            val response =
                apiService.getTimeCapsuleDates(yearMonth.year, yearMonth.monthValue)
            if (response.data != null) {
                return response.data!!.dates.map { LocalDate.parse(it) }
            } else {
                throw Exception("getTimeCapsuleDates response data is empty, $response")
            }
        } catch (e: Exception) {
            throw Exception("getTimeCapsuleDates api fail, $e")
        }
    }

    override suspend fun deleteTimeCapsule(id: Long): Boolean {
        try {
            apiService.deleteTimeCapsule(id)
            return true
        } catch (e: Exception) {
            throw Exception("deleteTimeCapsule api fail, $e")
        }
    }
}
