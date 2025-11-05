package com.emotionstorage.remote.response.myPage

import kotlinx.serialization.Serializable

@Serializable
data class GetKeysResponse(
    val keyCount: Int,
)
