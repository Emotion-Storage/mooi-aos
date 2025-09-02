package com.emotionstorage.ai_chat.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.ai_chat.domain.ChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class ChatState(
    val isNewChatRoom: Boolean = false,
    val isChatConnected: Boolean = false,
    val canCreateTimesCapsule: Boolean = false,
    val messages: List<ChatMessage> = emptyList(),
)

sealed class ChatAction {
    data class InitiateAndStartChat(val roomId: String) : ChatAction()
    data class SendMessage(val message: String) : ChatAction()
    object ExitChat : ChatAction()
    object CreateTimeCapsule : ChatAction()
}

sealed class ChatSideEffect {
    object CanCreateTimesCapsule : ChatSideEffect()
    data class CreateTimeCapsuleSuccess(val capsuleId: String) : ChatSideEffect()
}

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel(), ContainerHost<ChatState, ChatSideEffect> {
    override val container = container<ChatState, ChatSideEffect>(ChatState())

    fun onAction(action: ChatAction) {
        when (action) {
            is ChatAction.InitiateAndStartChat -> {}
            is ChatAction.SendMessage -> {}
            is ChatAction.ExitChat -> {}
            is ChatAction.CreateTimeCapsule -> {}
        }
    }
}