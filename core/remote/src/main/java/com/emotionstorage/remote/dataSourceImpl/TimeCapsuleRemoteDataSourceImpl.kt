package com.emotionstorage.remote.dataSourceImpl

import com.emotionstorage.data.dataSource.remote.TimeCapsuleRemoteDataSource
import com.emotionstorage.data.model.TimeCapsuleEntity
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.remote.api.TimeCapsuleApiService
import com.emotionstorage.remote.modelMapper.TimeCapsuleResponseMapper
import com.emotionstorage.remote.modelMapper.toPatchFavoriteRequest
import com.emotionstorage.remote.modelMapper.toPatchNoteRequest
import com.emotionstorage.remote.modelMapper.toPostOpenAtRequest
import com.emotionstorage.remote.response.CustomHttpException
import com.emotionstorage.remote.request.timeCapsule.CreateTimeCapsuleRequest
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import javax.inject.Inject

class TimeCapsuleRemoteDataSourceImpl @Inject constructor(
    private val apiService: TimeCapsuleApiService,
) : TimeCapsuleRemoteDataSource {
    override suspend fun patchTimeCapsuleOpen(id: Long): Boolean {
        try {
            apiService.patchTimeCapsuleOpen(id)
            return true
        } catch (e: Exception) {
            throw Exception("patchTimeCapsuleOpen api fail, ${e.message}", e)
        }
    }

    override suspend fun postTimeCapsuleOpenAt(
        id: Long,
        openAt: LocalDateTime,
    ): Boolean {
        try {
            apiService.postTimeCapsuleOpenAt(
                id = id,
                requestBody = openAt.toPostOpenAtRequest(id),
            )
            return true
        } catch (e: Exception) {
            throw Exception("postTimeCapsuleOpenAt api fail, ${e.message}", e)
        }
    }

    override suspend fun patchTimeCapsuleNote(
        id: Long,
        note: String,
    ): Boolean {
        try {
            apiService.patchTimeCapsuleNote(
                id,
                note.toPatchNoteRequest(),
            )
            return true
        } catch (e: Exception) {
            throw Exception("patchTimeCapsuleNote api fail, ${e.message}", e)
        }
    }

    override suspend fun patchTimeCapsuleFavorite(
        id: Long,
        isFavorite: Boolean,
    ): DataState<Boolean> =
        try {
            val response =
                apiService.patchTimeCapsuleFavorite(
                    id,
                    isFavorite.toPatchFavoriteRequest(),
                )
            if (response.data != null) {
                DataState.Success(response.data.isFavorite)
            } else {
                DataState.Error(
                    Exception("patchTimeCapsuleFavorite response data is empty, $response"),
                )
            }
        } catch (e: CustomHttpException) {
            DataState.Error(e, code = ErrorCode.toErrorCode(e.code ?: ""))
        } catch (e: Exception) {
            DataState.Error(Exception("patchTimeCapsuleFavorite api fail, ${e.message}", e))
        }

    override suspend fun getFavoriteTimeCapsules(
        page: Int,
        limit: Int,
        sortBy: String,
    ): List<TimeCapsuleEntity> {
        try {
//            val response =
//                apiService.getFavoriteTimeCapsules(
//                    page = page,
//                    limit = limit,
//                    sortBy = sortBy,
//                )
//            if (response.data != null) {
//                return TimeCapsuleResponseMapper.toData(response.data)
//            } else {
//                throw Exception("getFavoriteTimeCapsules response data is empty, $response")
//            }

            // mock response for paging test
            return (0..limit).map { i ->
                TimeCapsuleEntity(
                    id = (page * 100 + i).toLong(),
                    title = "TimeCapsule page-$page index-$i",
                    status = "OPEN",
                    isFavorite = true,
                    favoriteAt = LocalDateTime.now(),
                    historyDate = LocalDateTime.now(),
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now(),
                )
            }
        } catch (e: Exception) {
            throw Exception("getFavoriteTimeCapsules api fail, ${e.message}", e)
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
//            val response =
//                apiService.getTimeCapsules(
//                    startDate = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
//                    endDate = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
//                    page = page,
//                    limit = limit,
//                    status = status,
//                )
//            if (response.data != null) {
//                return TimeCapsuleResponseMapper.toData(response.data)
//            } else {
//                throw Exception("getTimeCapsules response data is empty, $response")
//            }

            // mock response for paging test
            return (0..limit).map { i ->
                TimeCapsuleEntity(
                    id = (page * 100 + i).toLong(),
                    title = "TimeCapsule page-$page index-$i",
                    status = "OPEN",
                    historyDate = startDate.atStartOfDay(),
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now(),
                )
            }
        } catch (e: Exception) {
            throw Exception("getTimeCapsules api fail, ${e.message}", e)
        }
    }

    override suspend fun getTimeCapsuleDetail(id: Long): TimeCapsuleEntity {
        try {
            val response = apiService.getTimeCapsuleDetail(id)
            if (response.data != null) {
                return TimeCapsuleResponseMapper.toData(response.data)
            } else {
                throw Exception("getTimeCapsuleDetail response data is empty, $response")
            }
        } catch (e: Exception) {
            throw Exception("getTimeCapsuleDetail api fail, ${e.message}", e)
        }
    }

    override suspend fun getTimeCapsuleDates(yearMonth: YearMonth): List<LocalDate> {
        try {
//            val response = apiService.getTimeCapsuleDates(yearMonth.year, yearMonth.monthValue)
//            if (response.data != null) {
//                return response.data.dates.map { LocalDate.parse(it) }
//            } else {
//                throw Exception("getTimeCapsuleDates response data is empty, $response")
//            }

            // mock response for paging test
            return (1..28).map { i ->
                LocalDate.of(yearMonth.year, yearMonth.month, i)
            }
        } catch (e: Exception) {
            throw Exception("getTimeCapsuleDates api fail, ${e.message}", e)
        }
    }

    override suspend fun deleteTimeCapsule(id: Long): Boolean {
        try {
            apiService.deleteTimeCapsule(id)
            return true
        } catch (e: Exception) {
            throw Exception("deleteTimeCapsule api fail, ${e.message}", e)
        }
    }

    override suspend fun createTimeCapsule(id: Long): Long {
        try {
            val response = apiService.postTimeCapsuleCreate(CreateTimeCapsuleRequest(id))
            if (response.data != null) {
                return response.data.timeCapsuleId
            } else {
                throw Exception("createTimeCapsule response data is empty, $response")
            }
        } catch (e: Exception) {
            throw Exception("createTimeCapsule api fail, ${e.message}", e)
        }
    }
}
