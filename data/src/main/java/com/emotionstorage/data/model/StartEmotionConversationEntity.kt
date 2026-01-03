package com.emotionstorage.data.model

data class StartEmotionConversationEntity(
    val roomId: Long,
    val isTempSave: Boolean,
    val isFirstChatOfDay: Boolean,
)
