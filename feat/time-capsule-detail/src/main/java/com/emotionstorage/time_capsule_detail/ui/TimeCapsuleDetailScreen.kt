package com.emotionstorage.time_capsule_detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailState
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.OnDeleteTimeCapsule
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.OnDeleteTrigger
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.OnExitTrigger
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.OnExpireTrigger
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.OnNoteChanged
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.OnSaveChangeTrigger
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.OnSaveNote
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.OnToggleFavorite
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.OnUnlockTimeCapsule
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.DeleteTimeCapsuleSuccess
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowDeleteModal
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowExitModal
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowExpiredModal
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowSaveChangesModal
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowToast
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowToast.TimeCapsuleDetailToast
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowUnlockModal
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowUnlockModal.UnlockModalState
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailViewModel
import com.emotionstorage.time_capsule_detail.ui.component.DecorativeDots
import com.emotionstorage.time_capsule_detail.ui.component.TimeCapsuleDetailActionButtons
import com.emotionstorage.time_capsule_detail.ui.component.TimeCapsuleEmotionComments
import com.emotionstorage.time_capsule_detail.ui.component.TimeCapsuleNote
import com.emotionstorage.time_capsule_detail.ui.component.TimeCapsuleSummary
import com.emotionstorage.time_capsule_detail.ui.modal.SaveChangesModal
import com.emotionstorage.time_capsule_detail.ui.modal.TimeCapsuleDeleteModal
import com.emotionstorage.time_capsule_detail.ui.modal.TimeCapsuleExitModal
import com.emotionstorage.time_capsule_detail.ui.modal.TimeCapsuleExpiredModal
import com.emotionstorage.time_capsule_detail.ui.modal.TimeCapsuleUnlockModal
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.AppSnackbarHost
import com.emotionstorage.ui.component.RoundedToggleButton
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.emotionstorage.ui.component.Toast

@Composable
fun TimeCapsuleDetailScreen(
    id: String,
    modifier: Modifier = Modifier,
    viewModel: TimeCapsuleDetailViewModel = hiltViewModel(),
    // is new, if navigated from ai chat
    isNewTimeCapsule: Boolean = false,
    navToHome: () -> Unit = {},
    navToSaveTimeCapsule: () -> Unit = {},
    navToBack: () -> Unit = {},
) {
    val state = viewModel.container.stateFlow.collectAsState()
    LaunchedEffect(id) {
        viewModel.onAction(TimeCapsuleDetailAction.Init(id))
    }

    val snackState = remember { SnackbarHostState() }
    val (isExitModalOpen, setExitModalOpen) = remember { mutableStateOf(false) }
    val (isUnlockModalOpen, setUnlockModalOpen) = remember { mutableStateOf(false) }
    val (unlockModalState, setUnlockModalState) =
        remember {
            mutableStateOf(UnlockModalState())
        }
    val (isExpiredModalOpen, setExpiredModalOpen) = remember { mutableStateOf(false) }
    val (isDeleteModalOpen, setDeleteModalOpen) = remember { mutableStateOf(false) }
    val (isSaveChangesModalOpen, setSaveChangesModalOpen) = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is DeleteTimeCapsuleSuccess -> {
                    navToBack()
                }

                is ShowUnlockModal -> {
                    setUnlockModalState(sideEffect.modalState)
                    setUnlockModalOpen(true)
                }

                is ShowExitModal -> {
                    setExitModalOpen(true)
                }

                is ShowExpiredModal -> {
                    // dismiss other modals before showing expired modal
                    setUnlockModalOpen(false)
                    setDeleteModalOpen(false)
                    setExpiredModalOpen(true)
                }

                is ShowDeleteModal -> {
                    setDeleteModalOpen(true)
                }

                is ShowSaveChangesModal -> {
                    setSaveChangesModalOpen(true)
                }

                is ShowToast -> {
                    // dismiss current snackbar if exists
                    snackState.currentSnackbarData?.dismiss()
                    snackState.showSnackbar(sideEffect.toast.message)
                }
            }
        }
    }

    StatelessTimeCapsuleDetailScreen(
        id = id,
        modifier = modifier,
        snackState = snackState,
        isNewTimeCapsule = isNewTimeCapsule,
        isExitModalOpen = isExitModalOpen,
        dismissExitModal = { setExitModalOpen(false) },
        unlockModalState = unlockModalState,
        isUnlockModalOpen = isUnlockModalOpen,
        dismissUnlockModal = { setUnlockModalOpen(false) },
        isDeleteModalOpen = isDeleteModalOpen,
        dismissDeleteModal = { setDeleteModalOpen(false) },
        isExpiredModalOpen = isExpiredModalOpen,
        dismissExpiredModal = { setExpiredModalOpen(false) },
        isSaveChangesModalOpen = isSaveChangesModalOpen,
        dismissSaveChangesModal = { setSaveChangesModalOpen(false) },
        state = state.value,
        onAction = viewModel::onAction,
        navToHome = navToHome,
        navToSaveTimeCapsule = navToSaveTimeCapsule,
        navToBack = navToBack,
    )
}

@Composable
private fun StatelessTimeCapsuleDetailScreen(
    id: String,
    modifier: Modifier = Modifier,
    snackState: SnackbarHostState = SnackbarHostState(),
    isNewTimeCapsule: Boolean = false,
    isExitModalOpen: Boolean = false,
    dismissExitModal: () -> Unit = {},
    unlockModalState: UnlockModalState = UnlockModalState(),
    isUnlockModalOpen: Boolean = false,
    dismissUnlockModal: () -> Unit = {},
    isDeleteModalOpen: Boolean = false,
    dismissDeleteModal: () -> Unit = {},
    isExpiredModalOpen: Boolean = false,
    dismissExpiredModal: () -> Unit = {},
    isSaveChangesModalOpen: Boolean = false,
    dismissSaveChangesModal: () -> Unit = {},
    state: TimeCapsuleDetailState = TimeCapsuleDetailState(),
    onAction: (TimeCapsuleDetailAction) -> Unit = {},
    navToHome: () -> Unit = {},
    navToSaveTimeCapsule: () -> Unit = {},
    navToBack: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    TimeCapsuleExitModal(
        isModalOpen = isNewTimeCapsule && isExitModalOpen,
        onDismissRequest = dismissExitModal,
        onContinue = dismissExitModal,
        onExit = {
            dismissExitModal()
            navToHome()
        },
    )
    TimeCapsuleUnlockModal(
        keyCount = unlockModalState.keyCount,
        requiredKeyCount = unlockModalState.requiredKeyCount,
        arriveAt = unlockModalState.arriveAt,
        isModalOpen = isUnlockModalOpen,
        onUnlock = {
            onAction(OnUnlockTimeCapsule(id))
            dismissUnlockModal()
        },
    )
    TimeCapsuleDeleteModal(
        isModalOpen = isDeleteModalOpen,
        onDismissRequest = dismissDeleteModal,
        onDelete = {
            onAction(OnDeleteTimeCapsule(id))
        },
    )
    TimeCapsuleExpiredModal(
        isModalOpen = isExpiredModalOpen,
        onConfirm = {
            dismissExpiredModal()
            navToBack()
        },
    )
    SaveChangesModal(
        isModalOpen = isSaveChangesModalOpen,
        onDismissRequest = dismissSaveChangesModal,
        onSave = {
            onAction(OnSaveNote)
            dismissSaveChangesModal()
            navToBack()
        },
        onDismiss = {
            dismissSaveChangesModal()
            navToBack()
        },
    )

    if (state.timeCapsule == null) {
        TimeCapsuleDetailLoadingScreen()
    } else {
        Scaffold(
            modifier =
                modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background)
                    .run {
                        // blur whole screen if locked
                        if (state.timeCapsule.status == TimeCapsule.STATUS.LOCKED) {
                            this.blur(8.dp)
                        } else {
                            this
                        }
                    },
            topBar = {
                val onTimeCapsuleExit = {
                    if (state.isNoteChanged) {
                        onAction(OnSaveChangeTrigger)
                    } else {
                        navToBack()
                    }
                }
                val onNewTimeCapsuleExit = {
                    onAction(OnExitTrigger)
                }

                TopAppBar(
                    title = state.timeCapsule.createdAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")),
                    showBackButton = !isNewTimeCapsule,
                    onBackClick = onTimeCapsuleExit,
                    showCloseButton = isNewTimeCapsule,
                    onCloseClick = onNewTimeCapsuleExit,
                    handleBackPress = true,
                    onHandleBackPress = {
                        if (isNewTimeCapsule) {
                            onNewTimeCapsuleExit()
                        } else {
                            onTimeCapsuleExit()
                        }
                    },
                    rightComponent = {
                        if (!isNewTimeCapsule && state.timeCapsule.status == TimeCapsule.STATUS.OPENED) {
                            RoundedToggleButton(
                                isSelected = state.timeCapsule.isFavorite,
                                onSelect = {
                                    onAction(OnToggleFavorite(id))
                                },
                            )
                        }
                    },
                )
            },
            snackbarHost = {
                AppSnackbarHost(hostState = snackState) { snackbarData ->
                    Toast(
                        message = snackbarData.visuals.message,
                        iconId =
                            if (snackbarData.visuals.message ==
                                TimeCapsuleDetailToast.FAVORITE_FULL.message
                            ) {
                                R.drawable.success_filled
                            } else {
                                null
                            },
                    )
                }
            },
        ) { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(MooiTheme.colorScheme.background)
                        .padding(innerPadding)
                        .padding(top = 31.dp, bottom = 55.dp)
                        .padding(horizontal = 16.dp)
                        .verticalScroll(scrollState),
            ) {
                TimeCapsuleSummary(
                    title = state.timeCapsule.title,
                    summary = state.timeCapsule.summary,
                )

                DecorativeDots(modifier = Modifier.padding(vertical = 31.dp))

                TimeCapsuleEmotionComments(
                    emotions = state.timeCapsule.emotions,
                    comments = state.timeCapsule.comments,
                )

                if (state.timeCapsule.status == TimeCapsule.STATUS.OPENED) {
                    TimeCapsuleNote(
                        modifier = Modifier.padding(top = 53.dp),
                        note = state.timeCapsule.note ?: "",
                        onNoteChange = {
                            onAction(OnNoteChanged(it))
                        },
                    )
                }

                TimeCapsuleDetailActionButtons(
                    expireAt = state.timeCapsule.expireAt,
                    status = state.timeCapsule.status,
                    isNewTimeCapsule = isNewTimeCapsule,
                    onSaveTimeCapsule = {
                        navToSaveTimeCapsule()
                    },
                    onTimeCapsuleExpired = {
                        onAction(OnExpireTrigger)
                    },
                    onSaveMindNote = {
                        onAction(OnSaveNote)
                    },
                    onDeleteTimeCapsule = {
                        onAction(OnDeleteTrigger)
                    },
                )
            }
        }
    }
}

@Composable
private fun TimeCapsuleDetailLoadingScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier =
            modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background),
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background)
                    .padding(innerPadding),
        ) {
            // empty screen
        }
    }
}

// @PreviewScreenSizes
@Preview
@Composable
private fun TimeCapsuleDetailScreenPreview() {
    MooiTheme {
        StatelessTimeCapsuleDetailScreen(
            id = "id",
            state =
                TimeCapsuleDetailState(
                    timeCapsule =
                        TimeCapsule(
                            id = "id",
                            status = TimeCapsule.STATUS.LOCKED,
                            title = "오늘 아침에 친구를 만났는데, 친구가 늦었어..",
                            summary =
                                "오늘 친구를 만났는데 친구가 지각해놓고 미안하단 말을 하지 않아서 집에 갈 때 기분이 좋지 않았어." +
                                    "그렇지만 집에서 엄마가 해주신 맛있는 저녁을 먹고 기분이 좋아지더라. " +
                                    "나를 가장 생각해주는 건 가족밖에 없다는 생각이 들었어.",
                            emotions =
                                listOf(
                                    TimeCapsule.Emotion("서운함", icon = 0, 30.0f),
                                    TimeCapsule.Emotion("고마움", icon = 3, 30.0f),
                                    TimeCapsule.Emotion("안정감", icon = 4, 80.0f),
                                ),
                            comments =
                                listOf(
                                    "오늘은 조금 힘든 일이 있었지만, 가족과의 따뜻한 시간 덕분에 긍정적인 감정으로 마무리했어요.",
                                    "귀가 후 가족애와 안정감을 느끼면서, 부정적 감정을 회복할 수 있었어요.",
                                    "감정이 복잡하게 얽힌 하루였네요. 하지만 작은 부분에서 감사함을 느끼는 모습이 멋져요.",
                                ),
                            note =
                                "아침엔 기분이 좀 꿀꿀했는데, 가족이랑 저녁 먹으면서 마음이 따뜻하게 풀려버렸다. " +
                                    "사소한 일에 흔들렸지만 결국 웃으면서 하루를 마무리할 수 있어서 다행이야.",
                            logs = emptyList(),
                            createdAt = LocalDateTime.now(),
                            updatedAt = LocalDateTime.now(),
                        ),
                ),
        )
    }
}
