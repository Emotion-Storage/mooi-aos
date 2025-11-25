package com.emotionstorage.ai_chat.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageResponse(
    val content: String? = null,
    val gauge: GaugeDto? = null,
    val sender: String? = null,
    val timestamp: String? = null,
    @SerialName("message_type")
    val messageType: String? = null,
)

@Serializable
data class GaugeDto(
    val summary: String? = null,
    @SerialName("gauge_score")
    val gaugeScore: Int? = null,
    @SerialName("turn_count_score")
    val turnCountScore: Int? = null,
)
