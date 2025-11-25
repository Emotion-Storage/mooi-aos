package com.emotionstorage.ai_chat.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class StartEmotionConversationResponse(
    val roomId: Long,
    // TODO : 값을 사용하는 부분이 있는지 체크 (없다면 제거하기)
    val isTempSave: Boolean,
    val isFirstChatOfDay: Boolean,
)
