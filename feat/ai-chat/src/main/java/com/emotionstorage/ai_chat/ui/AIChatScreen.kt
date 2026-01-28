package com.emotionstorage.ai_chat.ui

import android.view.Gravity
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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.ai_chat.presentation.AIChatAction
import com.emotionstorage.ai_chat.presentation.AIChatSideEffect
import com.emotionstorage.ai_chat.presentation.AIChatState
import com.emotionstorage.ai_chat.presentation.AIChatViewModel
import com.emotionstorage.ai_chat.ui.bottomSheet.ForceQuitChatBottomSheet
import com.emotionstorage.ai_chat.ui.bottomSheet.ProposeQuitChatBottomSheet
import com.emotionstorage.ai_chat.ui.component.ChatMessageInputBox
import com.emotionstorage.ai_chat.ui.component.ChatMessageList
import com.emotionstorage.ai_chat.ui.component.ChatProgressBar
import com.emotionstorage.ai_chat.ui.component.EmptyChatScreen
import com.emotionstorage.ai_chat.ui.component.TimeCapsuleCreateTopbarContent
import com.emotionstorage.ai_chat.ui.modal.AIChatExitModal
import com.emotionstorage.ai_chat.ui.modal.TimeCapsuleCreateLoadingModal
import com.emotionstorage.presentation.BaseSideEffect
import com.emotionstorage.ui.component.HideKeyboard
import com.emotionstorage.ui.component.appBar.TopAppBar
import com.emotionstorage.ui.component.loading.LoadingOverlay
import com.emotionstorage.ui.component.modal.LoginSessionExpiredModal
import com.emotionstorage.ui.component.modal.TempErrorModal
import com.emotionstorage.ui.component.toast.AppSnackbarController
import com.emotionstorage.ui.component.toast.AppSnackbarHost
import com.emotionstorage.ui.theme.MooiTheme

private enum class AIModalState{None,TempError,LoginSessionExpired}

@Composable
fun AIChatScreen(
    roomId: Long,
    navToBack: () -> Unit,
    navToTimeCapsuleDetail: (capsuleId: Long) -> Unit,
    navToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AIChatViewModel = hiltViewModel(),
) {
    val state = viewModel.container.stateFlow.collectAsState()
    val (modalState, setModalState) = remember{mutableStateOf(AIModalState.None)}
    val snackState = remember { SnackbarHostState() }
    val snackbarController = remember { AppSnackbarController(snackState) }

    LaunchedEffect(roomId) {
        viewModel.onAction(AIChatAction.ConnectChatRoom(roomId))
    }

    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is AIChatSideEffect.CreateTimeCapsuleSuccess -> {
                    navToTimeCapsuleDetail(sideEffect.capsuleId)
                }

                is AIChatSideEffect.CanCreateTimesCapsule -> {
                    snackState.showSnackbar(
                        "감정이 충분히 수집되어, 타임캡슐을 만들 수 있어요.",
                    )
                }

                is AIChatSideEffect.NavigateBack -> {
                    navToBack()
                }

                is BaseSideEffect.TemporalError -> {
                    setModalState(AIModalState.TempError)
                }

                is BaseSideEffect.SessionExpired -> {
                    setModalState(AIModalState.LoginSessionExpired)
                }
            }
        }
    }

    StatelessAIChatScreen(
        modifier = modifier,
        snackState = snackState,
        snackbarController = snackbarController,
        state = state.value,
        onAction = viewModel::onAction,
    )

    when(modalState){
        AIModalState.None -> {
            // no modal
        }

        AIModalState.TempError -> {
            TempErrorModal(
                onDismissRequest = { setModalState(AIModalState.None) },
            )
        }

        AIModalState.LoginSessionExpired -> {
            LoginSessionExpiredModal(
                onDismissRequest = { setModalState(AIModalState.None) },
                navToLogin = navToLogin,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatelessAIChatScreen(
    modifier: Modifier = Modifier,
    snackState: SnackbarHostState = SnackbarHostState(),
    snackbarController: AppSnackbarController? = null,
    state: AIChatState = AIChatState(),
    onAction: (action: AIChatAction) -> Unit = {},
) {
    val (isExitModalOpen, setExitModalOpen) = remember { mutableStateOf(false) }
    var draft by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    val listState = remember { LazyListState() }

    var showFinishBottomSheet by rememberSaveable { mutableStateOf(false) }

    val density = LocalDensity.current
    val imeBottomPx = WindowInsets.ime.getBottom(density)
    val isKeyboardVisible = imeBottomPx > 0

    val hasMessage = state.messages.isNotEmpty()
    val showEmptyScreen = !hasMessage && !state.isLoadingHistory

    var prevKeyboardVisible by remember { mutableStateOf(isKeyboardVisible) }

    LaunchedEffect(state.messages.size) {
        val last = state.messages.lastIndex
        if (last >= 0) {
            listState.animateScrollToItem(last)
        }
    }

    LaunchedEffect(isKeyboardVisible) {
        if (!prevKeyboardVisible && isKeyboardVisible) {
            var last = -1
            var stableCount = 0

            // 키보드가 올라오는 Frame이 맞아 떨어져야 완전히 채팅이 올라옴
            repeat(20) {
                withFrameNanos { }
                val now = imeBottomPx
                if (now == last) stableCount++ else stableCount = 0
                last = now

                if (stableCount >= 2) return@repeat
            }

            val anchorIndex = listState.layoutInfo.totalItemsCount - 1
            if (anchorIndex >= 0) {
                listState.animateScrollToItem(
                    index = anchorIndex,
                )
            }
        }
        prevKeyboardVisible = isKeyboardVisible
    }

    AIChatExitModal(
        isModalOpen = isExitModalOpen,
        onDismissRequest = { setExitModalOpen(false) },
        onExit = {
            setExitModalOpen(false)
            onAction(AIChatAction.TempSaveChatRoom)
        },
        onContinue = {
            setExitModalOpen(false)
        },
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
                rightComponent = {
                    if (state.canCreateTimesCapsule) {
                        TimeCapsuleCreateTopbarContent(
                            onClick = {
                                showFinishBottomSheet = true
                            },
                        )
                    }
                },
            )
        },
        snackbarHost = {
            AppSnackbarHost(
                hostState = snackState,
                gravity = Gravity.TOP,
                customDataFlow = snackbarController?.currentData,
            )
        },
    ) { innerPadding ->
        HideKeyboard {
            Column(
                modifier =
                    modifier
                        .fillMaxWidth()
                        .background(MooiTheme.colorScheme.backgroundDefault)
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

                    if (state.isLoadingHistory) {
                        LoadingOverlay()
                    }
                }

                if (state.canCreateTimesCapsule && showFinishBottomSheet) {
                    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ProposeQuitChatBottomSheet(
                        onDismissRequest = { showFinishBottomSheet = false },
                        sheetState = sheetState,
                        onConfirm = {
                            onAction(AIChatAction.CreateTimeCapsule)
                        },
                    )
                }

                if (state.showForceQuitBottomSheet) {
                    ForceQuitChatBottomSheet(
                        onConfirm = {
                            onAction(AIChatAction.DismissForceQuitSheet)
                            onAction(AIChatAction.CreateTimeCapsule)
                        },
                    )
                }

                if (state.isCreatingTimeCapsule) {
                    TimeCapsuleCreateLoadingModal()
                }
                ChatMessageInputBox(
                    text = draft,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                    onFocusChanged = { focused -> },
                    onTextChange = { draft = it },
                    focusRequester = focusRequester,
                    enabled = true,
                    sendEnabled = !state.isWaitingReply,
                    showSendingDisabled = state.isMooiTyping,
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
}

@Preview
@Composable
private fun ChatScreenPreview() {
    MooiTheme {
        StatelessAIChatScreen()
    }
}
