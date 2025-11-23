package com.emotionstorage.remote.response.auth

import kotlinx.serialization.Serializable

@Serializable
data class SignupResponseData(
    val success: Boolean,
)
