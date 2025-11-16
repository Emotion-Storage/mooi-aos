package com.emotionstorage.ai_chat.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
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
import com.emotionstorage.ai_chat.ui.component.ChattingFinishButton
import com.emotionstorage.ai_chat.ui.component.EmptyChatScreen
import com.emotionstorage.ai_chat.ui.component.TimeCapsuleCreateAlert
import com.emotionstorage.ui.component.Modal
import com.emotionstorage.ui.component.appBar.TopAppBar
import com.emotionstorage.ui.component.bottomSheet.BottomSheet
import com.emotionstorage.ui.theme.MooiTheme
import kotlinx.coroutines.delay

@Composable
fun AIChatScreen(
    roomId: Long,
    modifier: Modifier = Modifier,
    viewModel: AIChatViewModel = hiltViewModel(),
    navToBack: () -> Unit = {},
    navToTimeCapsuleDetail: (capsuleId: Long) -> Unit = {},
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

@OptIn(ExperimentalMaterial3Api::class)
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

    val listState = remember { LazyListState() }

    val canMakeTimeCapsule = state.chatProgress == 1f
    var showTimeCapsuleCreateAlert by remember { mutableStateOf(false) }
    var showFinishBottomSheet by rememberSaveable { mutableStateOf(false) }

    val density = LocalDensity.current
    val isKeyboardVisible = WindowInsets.ime.getBottom(density) > 0

    // ✅ 메시지가 하나라도 있으면 이후부터는 Empty 화면 안 보이게
    val hasMessage = state.messages.isNotEmpty()
    val showEmptyScreen = !hasMessage

    LaunchedEffect(state.messages.size) {
        val last = state.messages.lastIndex
        if (last >= 0) {
            listState.animateScrollToItem(last)
        }
    }

    LaunchedEffect(state.chatProgress) {
        if (canMakeTimeCapsule) {
            showTimeCapsuleCreateAlert = true
            delay(3000L)
            showTimeCapsuleCreateAlert = false
        }
    }

    AIChatExitModal(
        isModalOpen = isExitModalOpen,
        onDismissRequest = { setExitModalOpen(false) },
        onExit = navToBack,
    )

    Scaffold(
        topBar = {
            TopAppBar(
                showBackButton = true,
                onBackClick = {
                    setExitModalOpen(true)
                },
                handleBackPress = true,
                onHandleBackPress = {
                    setExitModalOpen(true)
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                modifier
                    .fillMaxWidth()
                    .background(MooiTheme.colorScheme.background)
                    .padding(innerPadding)
                    .consumeWindowInsets(WindowInsets.navigationBars)
                    .imePadding(),
        ) {
            ChatProgressBar(
                progress = state.chatProgress,
                modifier =
                    Modifier
                        .fillMaxWidth(),
            )

            if (showTimeCapsuleCreateAlert) {
                TimeCapsuleCreateAlert(
                    modifier = Modifier.padding(start = 13.dp, end = 13.dp, top = 18.dp),
                )
            }

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
                    listState = listState,
                    isMooiTyping = state.isMooiTyping,
                )

                if (showEmptyScreen) {
                    EmptyChatScreen(
                        modifier =
                            Modifier.offset(
                                y = if (isKeyboardVisible) 45.dp else (-60).dp,
                            ),
                        isKeyboardVisible = isKeyboardVisible,
                    )
                }

                if (canMakeTimeCapsule) {
                    ChattingFinishButton(
                        modifier =
                            Modifier
                                .padding(bottom = 13.dp)
                                .align(Alignment.BottomCenter),
                        onClick = {
                            showFinishBottomSheet = true
                        },
                    )
                }
            }

            if (canMakeTimeCapsule && showFinishBottomSheet) {
                val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                BottomSheet(
                    onDismissRequest = { showFinishBottomSheet = false },
                    sheetState = sheetState,
                    hideDragHandle = true,
                    subTitle = "감정을 충분히 이야기했어요.",
                    title = "대화를 종료하고,\n지금까지의 감정을 정리해볼까요?",
                    confirmLabel = "네, 종료할래요.",
                    dismissLabel = "아니요, 더 이야기할래요.",
                    onDismiss = { showFinishBottomSheet = false },
                    onConfirm = {
                        showFinishBottomSheet = false
                        onAction(AIChatAction.CreateTimeCapsule)
                    },
                )
            }

            ChatMessageInputBox(
                text = draft,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp),
                onFocusChanged = { focused -> isInputFocused = focused },
                onTextChange = { draft = it },
                focusRequester = focusRequester,
                enabled = true,
                sendEnabled = !state.isWaitingReply,
                showStop = state.isMooiTyping,
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
            title = "잠시 감정 대화를\n 이대로 중지할까요?",
            bottomDescription = "으늘의 감정 대화는\n오늘까지만 임시저장돼요!",
            bottomDescriptionHighlights = listOf("오늘까지만"),
            confirmLabel = "대화를 계속 진행할게요.",
            dismissLabel = "메인 화면으로 나갈래요",
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
