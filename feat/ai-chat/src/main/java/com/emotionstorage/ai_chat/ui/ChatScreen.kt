package com.emotionstorage.ai_chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.ai_chat.presentation.ChatAction
import com.emotionstorage.ai_chat.presentation.ChatSideEffect
import com.emotionstorage.ai_chat.presentation.ChatState
import com.emotionstorage.ai_chat.presentation.ChatViewModel
import com.emotionstorage.ai_chat.ui.component.ChatMessageInputBox
import com.emotionstorage.ai_chat.ui.component.ChatMessageList
import com.emotionstorage.ui.component.Modal
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun ChatScreen(
    roomId: String,
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = hiltViewModel(),
    navToBack: () -> Unit = {},
    navToTimeCapsuleDetail: (capsuleId: String) -> Unit = {}
) {
    val state = viewModel.container.stateFlow.collectAsState()
    LaunchedEffect(roomId) {
        viewModel.onAction(ChatAction.InitiateAndStartChat(roomId))
    }

    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is ChatSideEffect.CreateTimeCapsuleSuccess -> {
                    navToTimeCapsuleDetail(sideEffect.capsuleId)
                }
                is ChatSideEffect.CanCreateTimesCapsule -> {
                    // todo: show bottom sheet
                }
            }
        }
    }

    StatelessChatScreen(
        modifier = modifier,
        state = state.value,
        onAction = viewModel::onAction,
        navToBack = navToBack
    )
}

@Composable
private fun StatelessChatScreen(
    modifier: Modifier = Modifier,
    state: ChatState = ChatState(),
    onAction: (action: ChatAction) -> Unit = {},
    navToBack: () -> Unit = {},
){
    val (isExitModalOpen, setExitModalOpen) = remember { mutableStateOf(false) }
    ChatExitModel(
        isModelOpen = isExitModalOpen,
        onDismissRequest = { setExitModalOpen(false) },
        onExit = navToBack
    )

    Scaffold(
        modifier = modifier
            .fillMaxWidth()
            .background(MooiTheme.colorScheme.background),
        topBar = {
            TopAppBar(
                showBackButton = true,
                onBackClick = {
                    setExitModalOpen(true)
                })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MooiTheme.colorScheme.background)
                .padding(innerPadding)
                .imePadding()
        ) {
            ChatMessageList(
                modifier = Modifier.weight(1f),
                chatMessages = state.messages
            )
            ChatMessageInputBox(
                modifier = Modifier.fillMaxWidth(),
                onSendMessage = {
                    onAction(ChatAction.SendMessage(it))
                }
            )
        }
    }
}

@Composable
private fun ChatExitModel(
    isModelOpen: Boolean = false,
    onDismissRequest: () -> Unit = {},
    onExit: () -> Unit = {},
) {
    if (isModelOpen) {
        Modal(
            title = "잠시 감정 대화를 중지할까요?\n오늘의 감정 대화는\n오늘까지만 임시저장돼요!",
            confirmLabel = "대화 계속하기",
            dismissLabel = "그만하기",
            onDismissRequest = onDismissRequest,
            onDismiss = onExit,
        )
    }
}

@Preview
@Composable
private fun ChatScreenPreview() {
    ChatScreen("roomId")
}
