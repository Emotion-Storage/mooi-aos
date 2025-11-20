package com.emotionstorage.remote.api

import com.emotionstorage.remote.request.timeCapsule.PatchTimeCapsuleFavoriteRequest
import com.emotionstorage.remote.request.timeCapsule.PatchTimeCapsuleNoteRequest
import com.emotionstorage.remote.request.timeCapsule.PostTimeCapsuleOpenAtRequest
import com.emotionstorage.remote.response.ResponseDto
import com.emotionstorage.remote.response.timeCapsule.GetTimeCapsulesResponse
import com.emotionstorage.remote.response.timeCapsule.GetTimeCapsuleDatesReponse
import com.emotionstorage.remote.response.timeCapsule.GetTimeCapsuleDetailResponse
import com.emotionstorage.remote.response.timeCapsule.PatchTimeCapsuleFavoriteResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TimeCapsuleApiService {
    /**
     * 타임캡슐 목록 조회
     */
    @GET("api/v1/time-capsule")
    suspend fun getTimeCapsules(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("status") status: String = "all",
    ): ResponseDto<GetTimeCapsulesResponse>

    /**
     * 타임캡슐 존재 날짜 조회
     */
    @GET("api/v1/time-capsule/date")
    suspend fun getTimeCapsuleDates(
        @Query("year") year: Int,
        @Query("month") month: Int,
    ): ResponseDto<GetTimeCapsuleDatesReponse>

    /**
     * 즐겨찾기한 타임캡슐 목록 조회
     */
    @GET("api/v1/time-capsule/favorites")
    suspend fun getFavoriteTimeCapsules(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("sort") sortBy: String,
    ): ResponseDto<GetTimeCapsulesResponse>

    /**
     * 타임캡슐 상세 조회
     */
    @GET("api/v1/time-capsule/{capsuleId}")
    suspend fun getTimeCapsuleDetail(
        @Path(value = "capsuleId") id: Long,
    ): ResponseDto<GetTimeCapsuleDetailResponse>

    /**
     * 타임캡슐 오픈일 지정
     */
    @POST("api/v1/time-capsule/{capsuleId}/openAt")
    suspend fun postTimeCapsuleOpenAt(
        @Path(value = "capsuleId") id: Long,
        @Body requestBody: PostTimeCapsuleOpenAtRequest,
    ): ResponseDto<Unit>

    /**
     * 타임캡슐 열람
     * - 도착한 타임캡슐 열람
     * - 잠긴 타임캡슐 열쇠 사용하여 열람
     */
    @PATCH("api/v1/time-capsule/{capsuleId}/open")
    suspend fun patchTimeCapsuleOpen(
        @Path(value = "capsuleId") id: Long,
    ): ResponseDto<Unit>

    /**
     * 타임캡슐 노트 수정
     */
    @PATCH("api/v1/time-capsule/{capsuleId}/note")
    suspend fun patchTimeCapsuleNote(
        @Path(value = "capsuleId") id: Long,
        @Body requestBody: PatchTimeCapsuleNoteRequest,
    ): ResponseDto<Unit>

    /**
     * 타임캡슐 즐겨찾기
     */
    @PATCH("api/v1/time-capsule/{capsuleId}/favorite")
    suspend fun patchTimeCapsuleFavorite(
        @Path(value = "capsuleId") id: Long,
        @Body requestBody: PatchTimeCapsuleFavoriteRequest,
    ): ResponseDto<PatchTimeCapsuleFavoriteResponse>

    /**
     * 타임캡슐 삭제
     */
    @DELETE("api/v1/time-capsule/{capsuleId}")
    suspend fun deleteTimeCapsule(
        @Path(value = "capsuleId") id: Long,
    ): ResponseDto<Unit>
}
