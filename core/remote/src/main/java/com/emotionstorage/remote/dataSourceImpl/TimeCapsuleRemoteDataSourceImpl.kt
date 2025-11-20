package com.emotionstorage.remote.dataSourceImpl

import com.emotionstorage.data.dataSource.remote.FavoriteResultEntity
import com.emotionstorage.data.dataSource.remote.TimeCapsuleRemoteDataSource
import com.emotionstorage.data.model.TimeCapsuleEntity
import com.emotionstorage.remote.api.TimeCapsuleApiService
import com.emotionstorage.remote.modelMapper.TimeCapsuleResponseMapper
import com.emotionstorage.remote.request.timeCapsule.PatchTimeCapsuleFavoriteRequest
import com.emotionstorage.remote.request.timeCapsule.PatchTimeCapsuleNoteRequest
import com.emotionstorage.remote.request.timeCapsule.PostTimeCapsuleOpenAtRequest
import com.emotionstorage.remote.response.ResponseDto
import com.orhanobut.logger.Logger
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.time.LocalDate
import java.time.LocalDateTime
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
                requestBody =
                    PostTimeCapsuleOpenAtRequest(
                        capsuleId = id,
                        storedAt = LocalDateTime.now().toString(),
                        openAt = openAt.toString(),
                    ),
            )
            return true
        } catch (e: Exception) {
            throw Exception("postTimeCapsuleOpenAt api fail, ${e.message}", e)
            // todo: handle 410 error

//             {
//                "status": 410,
//                "code": "TIME_CAPSULE_DRAFT_EXPIRED",
//                "message": "타임캡슐 임시저장 기간이 만료되었습니다.",
//                "timestamp": "2025-11-20T16:43:36.924675036"
//              }
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
            throw Exception("patchTimeCapsuleNote api fail, ${e.message}", e)
        }
    }

    override suspend fun patchTimeCapsuleFavorite(
        id: Long,
        isFavorite: Boolean,
    ): FavoriteResultEntity =
        try {
            val response =
                apiService.patchTimeCapsuleFavorite(
                    id,
                    PatchTimeCapsuleFavoriteRequest(isFavorite),
                )
            if (response.data != null) {
                if (isFavorite) FavoriteResultEntity.ADDED else FavoriteResultEntity.REMOVED
            } else {
                throw Exception("patchTimeCapsuleFavorite response data is empty, $response")
            }
        } catch (e: HttpException) {
            // parse error response body
            val errorResponse =
                try {
                    e.response()?.errorBody()?.string()?.let {
                        Json.decodeFromString<ResponseDto<Nothing>>(it)
                    }
                } catch (parseError: Exception) {
                    Logger.e("patchTimeCapsuleFavorite error body parse fail: $parseError")
                    null
                }

            if (errorResponse?.code == "TIME_CAPSULE_FAVORITE_LIMIT_EXCEEDED") {
                FavoriteResultEntity.FULL
            } else {
                throw e
            }
        } catch (e: Exception) {
            throw Exception("patchTimeCapsuleFavorite api fail, ${e.message}", e)
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
            throw Exception("getTimeCapsules api fail, ${e.message}", e)
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
            throw Exception("getTimeCapsuleDetail api fail, ${e.message}", e)
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
}
