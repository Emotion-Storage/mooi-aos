package com.emotionstorage.remote.request.auth

import kotlinx.serialization.Serializable

@Serializable
data class GoogleLoginRequestBody(
    val idToken: String,
)
