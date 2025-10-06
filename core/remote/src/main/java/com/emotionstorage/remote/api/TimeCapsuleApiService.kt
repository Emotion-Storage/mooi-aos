package com.emotionstorage.remote.api

import com.emotionstorage.remote.request.timeCapsule.PatchTimeCapsuleFavoriteRequest
import com.emotionstorage.remote.request.timeCapsule.PatchTimeCapsuleNoteRequest
import com.emotionstorage.remote.response.ResponseDto
import com.emotionstorage.remote.response.timeCapsule.PatchTimeCapsuleFavoriteResponse
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path

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

    @PATCH("api/v1/time-capsule/{capsuleId}/favorite")
    suspend fun patchTimeCapsuleFavorite(
        @Path(value = "capsuleId") id: String,
        @Body requestBody: PatchTimeCapsuleFavoriteRequest,
    ): ResponseDto<PatchTimeCapsuleFavoriteResponse>
}
