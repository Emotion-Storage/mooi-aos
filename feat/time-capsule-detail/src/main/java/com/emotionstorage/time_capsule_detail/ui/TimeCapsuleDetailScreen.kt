package com.emotionstorage.time_capsule_detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalContext
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
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.OnOpenTimeCapsule
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.OnSaveChangeTrigger
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.OnSaveNote
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.DeleteTimeCapsuleSuccess
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.GetTimeCapsuleFail
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.OpenTimeCapsuleFail
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowDeleteModal
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowExitModal
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowExpiredModal
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowSaveChangesModal
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowUnlockModal
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowUnlockModal.UnlockModalState
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailViewModel
import com.emotionstorage.time_capsule_detail.presentation.ToggleSingleFavoriteAction
import com.emotionstorage.time_capsule_detail.presentation.ToggleSingleFavoriteSideEffect.ShowFavoriteFailToast
import com.emotionstorage.time_capsule_detail.presentation.ToggleSingleFavoriteSideEffect.ShowFavoriteSuccessToast
import com.emotionstorage.time_capsule_detail.presentation.ToggleSingleFavoriteState
import com.emotionstorage.time_capsule_detail.presentation.ToggleSingleFavoriteViewModel
import com.emotionstorage.time_capsule_detail.ui.component.DecorativeDots
import com.emotionstorage.time_capsule_detail.ui.component.TimeCapsuleDetailActionButtons
import com.emotionstorage.time_capsule_detail.ui.component.TimeCapsuleEmotionComments
import com.emotionstorage.time_capsule_detail.ui.component.TimeCapsuleNote
import com.emotionstorage.time_capsule_detail.ui.component.TimeCapsuleSummary
import com.emotionstorage.time_capsule_detail.ui.modal.SaveChangesModal
import com.emotionstorage.time_capsule_detail.ui.modal.DeleteTimeCapsuleModal
import com.emotionstorage.time_capsule_detail.ui.modal.ExitTimeCapsuleModal
import com.emotionstorage.time_capsule_detail.ui.modal.TimeCapsuleExpiredModal
import com.emotionstorage.time_capsule_detail.ui.modal.UnlockTimeCapsuleModal
import com.emotionstorage.ui.R
import com.emotionstorage.ui.annotation.PreviewScreenRatios
import com.emotionstorage.ui.component.toast.AppSnackbarHost
import com.emotionstorage.ui.component.loading.LoadingScreen
import com.emotionstorage.ui.component.button.RoundedToggleButton
import com.emotionstorage.ui.component.appBar.TopAppBar
import com.emotionstorage.ui.component.toast.AppSnackbarController
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private enum class TimeCapsuleDetailModal {
    NONE,
    EXIT,
    UNLOCK,
    EXPIRED,
    DELETE,
    SAVE_CHANGES,
}

@Composable
fun TimeCapsuleDetailScreen(
    id: Long,
    modifier: Modifier = Modifier,
    viewModel: TimeCapsuleDetailViewModel = hiltViewModel(),
    favoriteViewModel: ToggleSingleFavoriteViewModel = hiltViewModel(),
    // is new, if navigated from ai chat
    isNewTimeCapsule: Boolean = false,
    navToHome: () -> Unit = {},
    navToSaveTimeCapsule: () -> Unit = {},
    navToBack: () -> Unit = {},
) {
    val context = LocalContext.current

    val state = viewModel.container.stateFlow.collectAsState()
    val favoriteState = favoriteViewModel.container.stateFlow.collectAsState()

    val snackState = remember { SnackbarHostState() }
    val snackbarController = remember { AppSnackbarController(snackState) }

    val (modalState, setModalState) = remember { mutableStateOf(TimeCapsuleDetailModal.NONE) }
    val (unlockModalState, setUnlockModalState) =
        remember {
            mutableStateOf(UnlockModalState())
        }

    // init states
    LaunchedEffect(id) {
        viewModel.onAction(TimeCapsuleDetailAction.Init(id))
    }
    LaunchedEffect(state.value.timeCapsule?.isFavorite) {
        state.value.timeCapsule?.isFavorite?.let {
            favoriteViewModel.onAction(ToggleSingleFavoriteAction.Init(it))
        }
    }

    // collect side effect
    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is GetTimeCapsuleFail -> {
                    // todo: show deleted time capsule modal
                    navToBack()
                }

                is OpenTimeCapsuleFail -> {
                    // todo: show error modal
                    navToBack()
                }

                is DeleteTimeCapsuleSuccess -> {
                    navToBack()
                }

                is ShowUnlockModal -> {
                    setUnlockModalState(sideEffect.modalState)
                    setModalState(TimeCapsuleDetailModal.UNLOCK)
                }

                is ShowExitModal -> {
                    if (isNewTimeCapsule) {
                        setModalState(TimeCapsuleDetailModal.EXIT)
                    }
                }

                is ShowExpiredModal -> {
                    setModalState(TimeCapsuleDetailModal.EXPIRED)
                }

                is ShowDeleteModal -> {
                    setModalState(TimeCapsuleDetailModal.DELETE)
                }

                is ShowSaveChangesModal -> {
                    setModalState(TimeCapsuleDetailModal.SAVE_CHANGES)
                }
            }
        }
    }

    // collect favorite view model's side effect
    LaunchedEffect(Unit) {
        favoriteViewModel.container.sideEffectFlow.collect {
            when (it) {
                is ShowFavoriteSuccessToast -> {
                    snackState.currentSnackbarData?.dismiss()
                    snackbarController.showSnackbar(
                        message =
                            if (it.isFavorite) {
                                context.getString(R.string.toast_favorite_added)
                            } else {
                                context.getString(R.string.toast_favorite_removed)
                            },
                        iconResId = R.drawable.ic_success_filled,
                    )
                }

                is ShowFavoriteFailToast -> {
                    snackState.currentSnackbarData?.dismiss()
                    snackbarController.showSnackbar(
                        message = context.getString(R.string.toast_favorite_full),
                    )
                }
            }
        }
    }

    StatelessTimeCapsuleDetailScreen(
        id = id,
        modifier = modifier,
        snackState = snackState,
        snackbarController = snackbarController,
        modalState = modalState,
        dismissModal = { setModalState(TimeCapsuleDetailModal.NONE) },
        isNewTimeCapsule = isNewTimeCapsule,
        unlockModalState = unlockModalState,
        state = state.value,
        favoriteState = favoriteState.value,
        onAction = viewModel::onAction,
        onFavoriteAction = favoriteViewModel::onAction,
        navToHome = navToHome,
        navToSaveTimeCapsule = navToSaveTimeCapsule,
        navToBack = navToBack,
    )
}

@Composable
private fun StatelessTimeCapsuleDetailScreen(
    id: Long,
    modifier: Modifier = Modifier,
    snackState: SnackbarHostState = SnackbarHostState(),
    snackbarController: AppSnackbarController? = null,
    modalState: TimeCapsuleDetailModal = TimeCapsuleDetailModal.NONE,
    dismissModal: () -> Unit = {},
    unlockModalState: UnlockModalState = UnlockModalState(),
    isNewTimeCapsule: Boolean = false,
    state: TimeCapsuleDetailState = TimeCapsuleDetailState(),
    favoriteState: ToggleSingleFavoriteState = ToggleSingleFavoriteState(),
    onAction: (TimeCapsuleDetailAction) -> Unit = {},
    onFavoriteAction: (ToggleSingleFavoriteAction) -> Unit = {},
    navToHome: () -> Unit = {},
    navToSaveTimeCapsule: () -> Unit = {},
    navToBack: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    ModalHandler(
        modalState = modalState,
        dismissModal = dismissModal,
        onExit = {
            dismissModal()
            navToHome()
        },
        unlockModalState = unlockModalState,
        onUnlock = {
            onAction(OnOpenTimeCapsule(id))
            dismissModal()
        },
        onDelete = {
            onAction(OnDeleteTimeCapsule(id))
        },
        onExpire = {
            dismissModal()
            navToBack()
        },
        onSaveConfirm = {
            onAction(OnSaveNote(id))
            dismissModal()
            navToBack()
        },
        onSaveDismiss = {
            dismissModal()
            navToBack()
        },
    )

    if (state.timeCapsule == null) {
        LoadingScreen()
    } else {
        Scaffold(
            modifier =
                modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.backgroundDefault)
                    .run {
                        // blur whole screen if locked
                        if (state.timeCapsule.status == TimeCapsule.Status.LOCKED) {
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
                    title = state.timeCapsule.createdAt.format(DateTimeFormatter.ofPattern("yyyy. MM. dd  HH:mm")),
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
                        if (!isNewTimeCapsule && state.timeCapsule.status == TimeCapsule.Status.OPENED) {
                            RoundedToggleButton(
                                isSelected = favoriteState.isFavorite,
                                onSelect = {
                                    onFavoriteAction(ToggleSingleFavoriteAction.OnToggle(id))
                                },
                            )
                        }
                    },
                )
            },
            snackbarHost = {
                AppSnackbarHost(
                    hostState = snackState,
                    customDataFlow = snackbarController?.currentData,
                )
            },
        ) { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(MooiTheme.colorScheme.backgroundDefault)
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                        .verticalScroll(scrollState),
            ) {
                Spacer(modifier = Modifier.size(31.dp))

                TimeCapsuleSummary(
                    title = state.timeCapsule.title,
                    summary = state.timeCapsule.summary,
                )

                DecorativeDots(modifier = Modifier.padding(vertical = 31.dp))

                TimeCapsuleEmotionComments(
                    emotions = state.timeCapsule.emotions,
                    comments = state.timeCapsule.comments,
                )

                if (state.timeCapsule.status == TimeCapsule.Status.OPENED) {
                    TimeCapsuleNote(
                        modifier = Modifier.padding(top = 53.dp),
                        note = state.timeCapsule.note ?: "",
                        onNoteChange = {
                            onAction(OnNoteChanged(it))
                        },
                    )
                }

                TimeCapsuleDetailActionButtons(
                    modifier = Modifier.padding(bottom = 55.dp),
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
                        onAction(OnSaveNote(id))
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
private fun ModalHandler(
    modalState: TimeCapsuleDetailModal,
    dismissModal: () -> Unit,
    onExit: () -> Unit,
    unlockModalState: UnlockModalState,
    onUnlock: () -> Unit,
    onDelete: () -> Unit,
    onExpire: () -> Unit,
    onSaveConfirm: () -> Unit,
    onSaveDismiss: () -> Unit,
) {
    when (modalState) {
        TimeCapsuleDetailModal.NONE -> {
            // no modal
        }

        TimeCapsuleDetailModal.EXIT -> {
            ExitTimeCapsuleModal(
                onDismissRequest = dismissModal,
                onContinue = dismissModal,
                onExit = onExit,
            )
        }

        TimeCapsuleDetailModal.UNLOCK -> {
            UnlockTimeCapsuleModal(
                onDismissRequest = dismissModal,
                keyCount = unlockModalState.keyCount,
                requiredKeyCount = unlockModalState.requiredKeyCount,
                openAt = unlockModalState.openAt,
                onConfirm = onUnlock,
            )
        }

        TimeCapsuleDetailModal.EXPIRED -> {
            TimeCapsuleExpiredModal(
                onConfirm = onExpire,
            )
        }

        TimeCapsuleDetailModal.DELETE -> {
            DeleteTimeCapsuleModal(
                onDismissRequest = dismissModal,
                onDelete = onDelete,
            )
        }

        TimeCapsuleDetailModal.SAVE_CHANGES -> {
            SaveChangesModal(
                onDismissRequest = dismissModal,
                onSave = onSaveConfirm,
                onDismiss = onSaveDismiss,
            )
        }
    }
}

@PreviewScreenRatios
@Composable
private fun TimeCapsuleDetailScreenPreview() {
    MooiTheme {
        StatelessTimeCapsuleDetailScreen(
            id = 123L,
            state =
                TimeCapsuleDetailState(
                    timeCapsule =
                        TimeCapsule(
                            id = 123L,
                            status = TimeCapsule.Status.OPENED,
                            title = "오늘 아침에 친구를 만났는데, 친구가 늦었어...",
                            summary =
                                "오늘 친구를 만났는데 친구가 지각해놓고 미안하단 말을 하지 않아서 집에 갈 때 기분이 좋지 않았어." +
                                    "그렇지만 집에서 엄마가 해주신 맛있는 저녁을 먹고 기분이 좋아지더라. " +
                                    "나를 가장 생각해주는 건 가족밖에 없다는 생각이 들었어.",
                            emotions =
                                listOf(
                                    TimeCapsule.Emotion(
                                        emoji = "\uD83D\uDE14",
                                        label = "서운함",
                                        percentage = 30.0f,
                                    ),
                                    TimeCapsule.Emotion(
                                        emoji = "\uD83D\uDE0A",
                                        label = "고마움",
                                        percentage = 30.0f,
                                    ),
                                    TimeCapsule.Emotion(
                                        emoji = "\uD83E\uDD70",
                                        label = "안정감",
                                        percentage = 80.0f,
                                    ),
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
                            historyDate = LocalDateTime.now(),
                            createdAt = LocalDateTime.now(),
                            updatedAt = LocalDateTime.now(),
                        ),
                ),
        )
    }
}
