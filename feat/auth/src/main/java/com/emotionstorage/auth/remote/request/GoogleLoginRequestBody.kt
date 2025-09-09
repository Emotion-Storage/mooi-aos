package com.emotionstorage.auth.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class GoogleLoginRequestBody (
    val idToken: String
)