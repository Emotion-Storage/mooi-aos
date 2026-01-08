package com.emotionstorage.domain.model

data class EmotionChatSession(
    val roomId: Long,
    val isTempSave: Boolean,
    val isFirstChatOfDay: Boolean,
)
