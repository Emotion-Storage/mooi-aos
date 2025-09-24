package com.emotionstorage.ai_chat.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.ai_chat.presentation.AIChatAction
import com.emotionstorage.ai_chat.presentation.AIChatSideEffect
import com.emotionstorage.ai_chat.presentation.AIChatState
import com.emotionstorage.ai_chat.presentation.AIChatViewModel
import com.emotionstorage.ai_chat.ui.component.ChatMessageInputBox
import com.emotionstorage.ai_chat.ui.component.ChatMessageList
import com.emotionstorage.ai_chat.ui.component.ChatProgressBar
import com.emotionstorage.ai_chat.ui.component.DescriptionOverlayScreen
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

    var progressRect by remember { mutableStateOf(Rect.Zero) }
    var inputRect by remember { mutableStateOf(Rect.Zero) }
    var topbarRect by remember { mutableStateOf(Rect.Zero) }

    // // TODO : Datastore에 저장해야할 필요 o
    var showAgain by rememberSaveable { mutableStateOf(true) }
    var showCoach by rememberSaveable { mutableStateOf(showAgain) }

    StatelessAIChatScreen(
        state = state.value,
        onAction = viewModel::onAction,
        navToBack = navToBack,
        onProgressRect = { progressRect = it },
        onInputBoxRect = { inputRect = it },
        onTopbarRect = { topbarRect = it },
        showCoach = showCoach,
        progressBarBounds = progressRect,
        inputBoxBounds = inputRect,
        topbarBounds = topbarRect,
        onCoachComplete = { showCoach = false },
    )
}

@Composable
private fun StatelessAIChatScreen(
    state: AIChatState = AIChatState(),
    showCoach: Boolean = true,
    progressBarBounds: Rect = Rect.Zero,
    inputBoxBounds: Rect = Rect.Zero,
    topbarBounds: Rect = Rect.Zero,
    onCoachComplete: () -> Unit = {},
    onAction: (action: AIChatAction) -> Unit = {},
    navToBack: () -> Unit = {},
    onProgressRect: (Rect) -> Unit = {},
    onInputBoxRect: (Rect) -> Unit = {},
    onTopbarRect: (Rect) -> Unit = {},
) {
    val (isExitModalOpen, setExitModalOpen) = remember { mutableStateOf(false) }
    AIChatExitModel(
        isModelOpen = isExitModalOpen,
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
                    .imePadding(),
        ) {
            // TopAppbar 을 UI 컴포넌트로
            TopAppBar(
                modifier =
                    Modifier.onGloballyPositioned {
                        onTopbarRect(it.boundsInParent())
                    },
                showBackButton = true,
                onBackClick = {
                    setExitModalOpen(true)
                },
            )

            ChatProgressBar(
                progress = state.chatProgress,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        // ProgressBar 위치 추적
                        .onGloballyPositioned {
                            onProgressRect(it.boundsInParent())
                        },
            )

            ChatMessageList(
                modifier = Modifier.weight(1f),
                chatMessages = state.messages,
            )
            Button(onClick = {
                onAction(AIChatAction.ConnectChatRoom("test-roomId"))
            }) {
                Text(text = "채팅방 연결")
            }
            Button(onClick = {
                onAction(AIChatAction.ExitChatRoom)
            }) {
                Text(text = "채팅방 연결 끊기")
            }
            ChatMessageInputBox(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned {
                            onInputBoxRect(it.boundsInParent())
                        },
                onSendMessage = {
                    onAction(AIChatAction.SendChatMessage(it))
                },
            )
        }
    }

    if (showCoach) {
        DescriptionOverlayScreen(
            progressBarBounds = progressBarBounds,
            inputBoxBounds = inputBoxBounds,
            topbarBounds = topbarBounds,
            onComplete = onCoachComplete,
        )
    }
}

@Composable
private fun AIChatExitModel(
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
    MooiTheme {
        StatelessAIChatScreen()
    }
}
