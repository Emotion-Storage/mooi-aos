package com.emotionstorage.auth.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestBody (
    val idToken: String
)