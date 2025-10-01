package com.emotionstorage.time_capsule_detail.ui

import SpeechBubble
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailState
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.DeleteTimeCapsuleSuccess
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowDeleteModal
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowExpiredModal
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowToast
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowToast.TimeCapsuleDetailToast
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowUnlockModal
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowUnlockModal.UnlockModalState
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailViewModel
import com.emotionstorage.time_capsule_detail.ui.component.TimeCapsuleDeleteModal
import com.emotionstorage.time_capsule_detail.ui.component.TimeCapsuleExpiredModal
import com.emotionstorage.time_capsule_detail.ui.component.TimeCapsuleUnlockModal
import com.emotionstorage.ui.component.CtaButton
import com.emotionstorage.ui.component.RoundedToggleButton
import com.emotionstorage.ui.component.TextBoxInput
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.LinearGradient
import com.emotionstorage.ui.util.getIconResId
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.CountDownTimer
import com.emotionstorage.ui.component.SuccessToast
import com.emotionstorage.ui.component.Toast

@Composable
fun TimeCapsuleDetailScreen(
    id: String,
    modifier: Modifier = Modifier,
    viewModel: TimeCapsuleDetailViewModel = hiltViewModel(),
    navToBack: () -> Unit = {},
    navToSaveTimeCapsule: (id: String) -> Unit = {},
) {
    val state = viewModel.container.stateFlow.collectAsState()
    LaunchedEffect(id) {
        viewModel.onAction(TimeCapsuleDetailAction.Init(id))
    }

    val snackState = remember { SnackbarHostState() }
    val (isUnlockModalOpen, setUnlockModalOpen) = remember { mutableStateOf(false) }
    val (unlockModalState, setUnlockModalState) =
        remember {
            mutableStateOf(UnlockModalState())
        }
    val (isExpiredModalOpen, setExpiredModalOpen) = remember { mutableStateOf(false) }
    val (isDeleteModalOpen, setDeleteModalOpen) = remember { mutableStateOf(false) }

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

                is ShowExpiredModal -> {
                    // dismiss other modals before showing expired modal
                    setUnlockModalOpen(false)
                    setDeleteModalOpen(false)
                    setExpiredModalOpen(true)
                }

                is ShowDeleteModal -> {
                    setDeleteModalOpen(true)
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
        unlockModalState = unlockModalState,
        isUnlockModalOpen = isUnlockModalOpen,
        dismissUnlockModal = { setUnlockModalOpen(false) },
        isDeleteModalOpen = isDeleteModalOpen,
        dismissDeleteModal = { setDeleteModalOpen(false) },
        isExpiredModalOpen = isExpiredModalOpen,
        dismissExpiredModal = { setExpiredModalOpen(false) },
        state = state.value,
        onAction = viewModel::onAction,
        navToBack = navToBack,
        navToSaveTimeCapsule = navToSaveTimeCapsule,
    )
}

@Composable
private fun StatelessTimeCapsuleDetailScreen(
    id: String,
    modifier: Modifier = Modifier,
    snackState: SnackbarHostState = SnackbarHostState(),
    unlockModalState: UnlockModalState = UnlockModalState(),
    isUnlockModalOpen: Boolean = false,
    dismissUnlockModal: () -> Unit = {},
    isDeleteModalOpen: Boolean = false,
    dismissDeleteModal: () -> Unit = {},
    isExpiredModalOpen: Boolean = false,
    dismissExpiredModal: () -> Unit = {},
    state: TimeCapsuleDetailState = TimeCapsuleDetailState(),
    onAction: (TimeCapsuleDetailAction) -> Unit = {},
    navToBack: () -> Unit = {},
    navToSaveTimeCapsule: (id: String) -> Unit = {},
) {
    val scrollState = rememberScrollState()

    TimeCapsuleUnlockModal(
        keyCount = unlockModalState.keyCount,
        requiredKeyCount = unlockModalState.requiredKeyCount,
        arriveAt = unlockModalState.arriveAt,
        isModalOpen = isUnlockModalOpen,
        onUnlock = {
            // todo: unlock time capsule
            dismissUnlockModal()
        },
    )
    TimeCapsuleDeleteModal(
        isModalOpen = isDeleteModalOpen,
        onDismissRequest = dismissDeleteModal,
        onDelete = {
            onAction(TimeCapsuleDetailAction.OnDeleteTimeCapsule(id))
        },
    )
    TimeCapsuleExpiredModal(
        isModalOpen = isExpiredModalOpen,
        onConfirm = {
            dismissExpiredModal()
            navToBack()
        },
    )

    if (state.timeCapsule == null) {
        // loading ui
        Scaffold(
            modifier =
                modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background),
            topBar = {
                TopAppBar(
                    showBackButton = true,
                    onBackClick = navToBack,
                )
            },
        ) { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(MooiTheme.colorScheme.background)
                        .padding(innerPadding),
            ) {
                // empty screen
            }
        }
    } else {
        Scaffold(
            modifier =
                modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background),
            topBar = {
                TopAppBar(
                    title = state.timeCapsule.createdAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm")),
                    showBackButton = true,
                    onBackClick = navToBack,
                    rightComponent = {
                        if (state.timeCapsule.status == TimeCapsule.STATUS.OPENED) {
                            RoundedToggleButton(
                                isSelected = state.timeCapsule.isFavorite,
                                onSelect = {
                                    onAction(TimeCapsuleDetailAction.OnToggleFavorite(id))
                                },
                            )
                        }
                    },
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackState) { snackbarData ->
                    if (snackbarData.visuals.message == TimeCapsuleDetailToast.FAVORITE_FULL.message) {
                        Toast(message = snackbarData.visuals.message)
                    } else {
                        SuccessToast(message = snackbarData.visuals.message)
                    }
                }
            },
        ) { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(MooiTheme.colorScheme.background)
                        .run {
                            // blur all content if locked
                            if (state.timeCapsule.status == TimeCapsule.STATUS.LOCKED) {
                                this.blur(4.dp)
                            } else {
                                this
                            }
                        }.padding(innerPadding)
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
                        note = state.timeCapsule.note,
                        onNoteChange = {
                            // todo: save note content
                        },
                    )
                }

                TimeCapsuleDetailActionButtons(
                    createdAt = state.timeCapsule.createdAt,
                    status = state.timeCapsule.status,
                    onSaveTimeCapsule = {
                        navToSaveTimeCapsule(id)
                    },
                    onTimeCapsuleExpired = {
                        onAction(TimeCapsuleDetailAction.OnExpireTrigger)
                    },
                    onSaveMindNote = {
                        // todo: save mind note content
                    },
                    onDeleteTimeCapsule = {
                        onAction(TimeCapsuleDetailAction.OnDeleteTrigger)
                    },
                )
            }
        }
    }
}

@Composable
private fun TimeCapsuleSummary(
    title: String,
    summary: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = title,
            style = MooiTheme.typography.body1,
            color = Color.White,
        )
        Box(
            modifier =
                Modifier
                    .background(Color(0x1AAECBFA), RoundedCornerShape(15.dp))
                    .padding(18.dp),
        ) {
            Text(
                text = summary,
                style = MooiTheme.typography.caption3.copy(lineHeight = 24.sp),
                color = Color.White,
            )
        }
    }
}

@Composable
private fun DecorativeDots(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        for (alpha in listOf(0.1f, 0.3f, 0.7f)) {
            Box(
                modifier =
                    Modifier
                        .size(8.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    MooiTheme.colorScheme.primary.copy(alpha = alpha),
                                    Color(0xFF9AB4F2).copy(alpha = alpha),
                                    MooiTheme.colorScheme.tertiary.copy(alpha = alpha),
                                ),
                            ),
                            CircleShape,
                        ),
            )
        }
    }
}

@Composable
private fun TimeCapsuleEmotionComments(
    modifier: Modifier = Modifier,
    emotions: List<TimeCapsule.Emotion> = emptyList(),
    comments: List<String> = emptyList(),
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(27.dp),
    ) {
        Text(
            text = "내가 느낀 감정은\n아래와 같이 분석할 수 있어요.",
            style = MooiTheme.typography.body1.copy(lineHeight = 24.sp),
            textAlign = TextAlign.Center,
            color = MooiTheme.colorScheme.primary,
        )
        Emotions(emotions = emotions)
        Comments(comments = comments)
    }
}

@Composable
private fun Emotions(
    modifier: Modifier = Modifier,
    emotions: List<TimeCapsule.Emotion> = emptyList(),
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(11.dp),
    ) {
        for (emotion in emotions) {
            Box(
                modifier =
                    Modifier
                        .height(92.dp)
                        .weight(1f)
                        .background(
                            LinearGradient(
                                listOf(
                                    // alpha = 0.5 * 0.2
                                    Color(0xFF849BEA).copy(alpha = 0.1f),
                                    // alpha = 0.08 * 0.2
                                    Color(0xFF849BEA).copy(alpha = 0.016f),
                                ),
                                angleInDegrees = -18f,
                            ),
                            RoundedCornerShape(10.dp),
                        ),
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        if (emotion.getIconResId() == null) {
                            Box(
                                modifier =
                                    Modifier
                                        .size(22.dp)
                                        .background(Color.Gray, CircleShape),
                            )
                        } else {
                            Image(
                                painter = painterResource(id = emotion.getIconResId()!!),
                                modifier = Modifier.size(22.dp),
                                contentDescription = emotion.label,
                            )
                        }
                        Text(
                            text = emotion.label,
                            style = MooiTheme.typography.body8,
                            color = MooiTheme.colorScheme.primary,
                        )
                    }
                    Text(
                        text = "${emotion.percentage?.toInt() ?: "- "}%",
                        style = MooiTheme.typography.head3.copy(lineHeight = 29.sp),
                        color = Color.White,
                    )
                }
            }
        }
    }
}

@Composable
private fun Comments(
    modifier: Modifier = Modifier,
    comments: List<String> = emptyList(),
) {
    Column(
        modifier =
            modifier
                .background(Color(0x0AAECBFA), RoundedCornerShape(15.dp))
                .border(1.dp, Color(0x33849BEA), RoundedCornerShape(15.dp))
                .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        for (comment in comments) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(11.dp),
            ) {
                Box(
                    modifier = Modifier.padding(top = 5.dp),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .size(5.dp)
                                .background(Color.White, CircleShape),
                    )
                }
                Text(
                    text = comment,
                    style = MooiTheme.typography.caption3.copy(lineHeight = 22.sp),
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
private fun TimeCapsuleNote(
    modifier: Modifier = Modifier,
    note: String? = null,
    onNoteChange: (note: String) -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(17.dp),
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = "나의 회고 일기",
                style = MooiTheme.typography.body1.copy(lineHeight = 24.sp),
                color = Color.White,
            )
            Text(
                text = "그때의 감정과 지금 달라진것이 있나요?\n감정을 회고하며 노트를 작성해보세요.",
                style = MooiTheme.typography.caption7.copy(lineHeight = 20.sp),
                textAlign = TextAlign.Center,
                color = MooiTheme.colorScheme.gray400,
            )
        }

        TextBoxInput(
            modifier = Modifier.fillMaxWidth(),
            value = note ?: "",
            onValueChange = onNoteChange,
            placeHolder = "지금 내 마음은...",
            showCharCount = true,
            maxCharCount = 1000,
        )
    }
}

@Composable
private fun TimeCapsuleDetailActionButtons(
    createdAt: LocalDateTime,
    status: TimeCapsule.STATUS,
    modifier: Modifier = Modifier,
    onSaveTimeCapsule: () -> Unit = {},
    onTimeCapsuleExpired: () -> Unit = {},
    onSaveMindNote: () -> Unit = {},
    onDeleteTimeCapsule: () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.7.dp),
        horizontalAlignment = Alignment.End,
    ) {
        if (status == TimeCapsule.STATUS.TEMPORARY) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CountDownTimer(
                    deadline = createdAt.plusHours(25),
                ) { hours, minutes, seconds ->
                    LaunchedEffect(hours, minutes, seconds) {
                        if (hours == 0L && minutes == 0L && seconds == 0L) {
                            onTimeCapsuleExpired()
                        }
                    }

                    val timerString = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                    SpeechBubble(
                        text = "이 캡슐을 보관할 수 있는 시간이\n$timerString 남았어요!",
                        tail = BubbleTail.BottomCenter,
                        sizeParam = DpSize(265.dp, 84.dp),
                        textColor = MooiTheme.colorScheme.errorRed,
                    )
                }

                CtaButton(
                    modifier = Modifier.fillMaxWidth(),
                    labelString = "타임캡슐 보관하기",
                    onClick = {
                        onSaveTimeCapsule()
                    },
                    isDefaultWidth = false,
                )
            }
        } else {
            // todo: change action button according to status
            CtaButton(
                modifier = Modifier.fillMaxWidth(),
                labelString = "타임캡슐 저장하기",
                isDefaultWidth = false,
            )
        }

        // delete button
        Row(
            modifier =
                Modifier
                    .height(20.dp)
                    .clickable(onClick = { onDeleteTimeCapsule() }),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Image(
                modifier = Modifier.size(13.dp, 14.dp),
                painter = painterResource(id = R.drawable.trash),
                contentDescription = "delete",
            )
            Text(
                text = "타임캡슐 삭제하기",
                style = MooiTheme.typography.caption6,
                color = MooiTheme.colorScheme.gray600,
            )
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
