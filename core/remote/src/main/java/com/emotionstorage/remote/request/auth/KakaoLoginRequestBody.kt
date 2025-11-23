package com.emotionstorage.remote.request.auth

import kotlinx.serialization.Serializable

@Serializable
data class KakaoLoginRequestBody(
    val accessToken: String,
)
