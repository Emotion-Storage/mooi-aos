package com.emotionstorage.domain.model

data class EmotionConversationStartInfo(
    val roomId: Long,
    val isTempSave: Boolean,
    val isFirstChatOfDay: Boolean,
)
