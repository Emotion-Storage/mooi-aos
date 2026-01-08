package com.emotionstorage.data.model

data class EmotionChatSessionEntity(
    val roomId: Long,
    val isTempSave: Boolean,
    val isFirstChatOfDay: Boolean,
)
