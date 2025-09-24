package com.emotionstorage.auth.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseData(
    val accessToken: String
)
