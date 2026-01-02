package com.emotionstorage.remote.response.chat

import kotlinx.serialization.Serializable

@Serializable
data class ExitChatRoomResponse(
    val finished: Boolean,
)
