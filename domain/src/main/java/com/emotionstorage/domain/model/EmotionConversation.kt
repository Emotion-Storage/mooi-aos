package com.emotionstorage.domain.model

data class EmotionConversation(
    val roomId: Long,
    val isTempSave: Boolean,
    val isFirstChatOfDay: Boolean,
)
