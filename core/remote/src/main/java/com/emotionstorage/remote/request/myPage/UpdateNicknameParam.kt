package com.emotionstorage.remote.request.myPage

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import kotlinx.serialization.encodeToString

@Serializable
data class UpdateNicknameParam(
    val nickname: String,
)

fun UpdateNicknameParam.toRequestBody(): RequestBody = Json.encodeToString(this).toRequestBody()
