package com.emotionstorage.remote.response.timeCapsule

import kotlinx.serialization.Serializable

@Serializable
data class GetTimeCapsuleDatesReponse(
    val totalDates: Int,
    val dates: List<String>,
)
