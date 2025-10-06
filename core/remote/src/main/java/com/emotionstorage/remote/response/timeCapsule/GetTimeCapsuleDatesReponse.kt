package com.emotionstorage.remote.response.timeCapsule

import com.emotionstorage.common.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class GetTimeCapsuleDatesReponse (
    val totalDates: Int,
    val dates: List<String>
)
