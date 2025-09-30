package com.emotionstorage.ai_chat.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.ai_chat.presentation.AIChatAction
import com.emotionstorage.ai_chat.presentation.AIChatSideEffect
import com.emotionstorage.ai_chat.presentation.AIChatState
import com.emotionstorage.ai_chat.presentation.AIChatViewModel
import com.emotionstorage.ai_chat.ui.component.ChatMessageInputBox
import com.emotionstorage.ai_chat.ui.component.ChatMessageList
import com.emotionstorage.ai_chat.ui.component.ChatProgressBar
import com.emotionstorage.ai_chat.ui.component.EmptyChatScreen
import com.emotionstorage.ui.component.Modal
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun AIChatScreen(
    roomId: String,
    modifier: Modifier = Modifier,
    viewModel: AIChatViewModel = hiltViewModel(),
    navToBack: () -> Unit = {},
    navToTimeCapsuleDetail: (capsuleId: String) -> Unit = {},
) {
    val state = viewModel.container.stateFlow.collectAsState()
    LaunchedEffect(roomId) {
        viewModel.onAction(AIChatAction.ConnectChatRoom(roomId))
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is AIChatSideEffect.ToastMessage -> {
                    // toast message for debugging
                    Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
                }

                is AIChatSideEffect.CreateTimeCapsuleSuccess -> {
                    navToTimeCapsuleDetail(sideEffect.capsuleId)
                }

                is AIChatSideEffect.CanCreateTimesCapsule -> {
                    // todo: show bottom sheet
                }
            }
        }
    }

    StatelessAIChatScreen(
        modifier = modifier,
        state = state.value,
        onAction = viewModel::onAction,
        navToBack = navToBack,
    )
}

@Composable
private fun StatelessAIChatScreen(
    modifier: Modifier = Modifier,
    state: AIChatState = AIChatState(),
    onAction: (action: AIChatAction) -> Unit = {},
    navToBack: () -> Unit = {},
) {
    val (isExitModalOpen, setExitModalOpen) = remember { mutableStateOf(false) }
    var draft by remember { mutableStateOf("") }
    var isInputFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val showEmptyScreen = state.messages.isEmpty() && !isInputFocused

    AIChatExitModal(
        isModalOpen = isExitModalOpen,
        onDismissRequest = { setExitModalOpen(false) },
        onExit = navToBack,
    )

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MooiTheme.colorScheme.background)
                    .padding(innerPadding)
                    .consumeWindowInsets(WindowInsets.navigationBars)
                    .imePadding(),
        ) {
            TopAppBar(
                showBackButton = true,
                onBackClick = {
                    setExitModalOpen(true)
                },
            )

            ChatProgressBar(
                progress = state.chatProgress,
                modifier =
                    Modifier
                        .fillMaxWidth(),
            )

            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                ChatMessageList(
                    modifier = Modifier.fillMaxSize(),
                    chatMessages = state.messages,
                )

                if (showEmptyScreen) {
                    EmptyChatScreen(
                        modifier = Modifier.offset(y = (-60).dp),
                    )
                }
            }

            ChatMessageInputBox(
                text = draft,
                modifier =
                    Modifier.fillMaxWidth().onFocusChanged { focusState ->
                        isInputFocused = focusState.isFocused
                    },
                onTextChange = { draft = it },
                focusRequester = focusRequester,
                onSendMessage = {
                    val msg = draft.trim()
                    if (msg.isNotEmpty()) {
                        onAction(AIChatAction.SendChatMessage(msg))
                        draft = ""
                    }
                },
            )
        }
    }
}

@Composable
private fun AIChatExitModal(
    isModalOpen: Boolean = false,
    onDismissRequest: () -> Unit = {},
    onExit: () -> Unit = {},
) {
    if (isModalOpen) {
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
    MooiTheme {
        StatelessAIChatScreen()
    }
}
