package com.emotionstorage.remote.response.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseData(
    val accessToken: String,
)
