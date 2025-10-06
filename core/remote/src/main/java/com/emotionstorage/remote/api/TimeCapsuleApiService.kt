package com.emotionstorage.remote.api

import com.emotionstorage.remote.request.timeCapsule.PatchTimeCapsuleFavoriteRequest
import com.emotionstorage.remote.request.timeCapsule.PatchTimeCapsuleNoteRequest
import com.emotionstorage.remote.response.ResponseDto
import com.emotionstorage.remote.response.timeCapsule.GetFavoriteTimeCapsulesResponse
import com.emotionstorage.remote.response.timeCapsule.PatchTimeCapsuleFavoriteResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface TimeCapsuleApiService {
    @PATCH("api/v1/time-capsule/{capsuleId}/open")
    suspend fun patchTimeCapsuleOpen(
        @Path(value = "capsuleId") id: String,
    ): ResponseDto<Unit>

    @PATCH("api/v1/time-capsule/{capsuleId}/note")
    suspend fun patchTimeCapsuleNote(
        @Path(value = "capsuleId") id: String,
        @Body requestBody: PatchTimeCapsuleNoteRequest,
    ): ResponseDto<Unit>

    @GET("api/v1/time-capsule/favorites")
    suspend fun getFavoriteTimeCapsules(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("sort") sortBy: String,
    ): ResponseDto<GetFavoriteTimeCapsulesResponse>

    @PATCH("api/v1/time-capsule/{capsuleId}/favorite")
    suspend fun patchTimeCapsuleFavorite(
        @Path(value = "capsuleId") id: String,
        @Body requestBody: PatchTimeCapsuleFavoriteRequest,
    ): ResponseDto<PatchTimeCapsuleFavoriteResponse>
}
