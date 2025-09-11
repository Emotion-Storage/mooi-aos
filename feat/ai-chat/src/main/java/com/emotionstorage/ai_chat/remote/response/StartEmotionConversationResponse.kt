package com.emotionstorage.ai_chat.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class StartEmotionConversationResponse (
    val roomId:String
)