package com.emotionstorage.ai_chat.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.ai_chat.domain.model.ChatMessage
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class AIChatState(
    val roomId: String = "",
    val isChatRoomConnected: Boolean = false,

    val messages: List<ChatMessage> = emptyList(),
    val canCreateTimesCapsule: Boolean = false,
)

sealed class AIChatAction {
    data class ConnectChatRoom(val roomId: String) : AIChatAction()
    data class SendMessage(val message: String) : AIChatAction()
    object ExitChatRoom : AIChatAction()

    object CreateTimeCapsule : AIChatAction()
}

sealed class AIChatSideEffect {
    object CanCreateTimesCapsule : AIChatSideEffect()
    data class CreateTimeCapsuleSuccess(val capsuleId: String) : AIChatSideEffect()
}

@HiltViewModel
class AIChatViewModel @Inject constructor() : ViewModel(), ContainerHost<AIChatState, AIChatSideEffect> {
    override val container = container<AIChatState, AIChatSideEffect>(AIChatState())

    fun onAction(action: AIChatAction) {
        when (action) {
            is AIChatAction.ConnectChatRoom -> {
                handleConnectChatRoom(action.roomId)
            }
            is AIChatAction.SendMessage -> {
                handleSendMessage(action.message)
            }
            is AIChatAction.ExitChatRoom -> {
                handleExitChatRoom()
            }
            is AIChatAction.CreateTimeCapsule -> {
                handleCreateTimeCapsule()
            }
        }
    }

    private fun handleConnectChatRoom(roomId: String) = intent {
        reduce {
            state.copy(roomId = roomId)
        }
        // todo: start chat connection
        reduce {
            state.copy(isChatRoomConnected = true)
        }
    }

    private fun handleSendMessage(message: String) = intent {
        // todo: send message to server
        reduce {
            state.copy(
                messages = state.messages + ChatMessage(
                    roomId = state.roomId,
                    source = ChatMessage.MessageSource.CLIENT,
                    content = message
                )
            )
        }
    }

    private fun handleExitChatRoom() = intent{
        // todo: stop chat connection
        reduce {
            state.copy(isChatRoomConnected = false)
        }
    }

    private fun handleCreateTimeCapsule() = intent{
        if(!state.canCreateTimesCapsule) {
            Logger.w("Can't create time capsule yet")
            return@intent
        }

        // todo: stop chat connection
        reduce{
            state.copy(isChatRoomConnected = false)
        }
        // todo: get time capsule id from server
        postSideEffect(AIChatSideEffect.CreateTimeCapsuleSuccess("123"))
    }
}