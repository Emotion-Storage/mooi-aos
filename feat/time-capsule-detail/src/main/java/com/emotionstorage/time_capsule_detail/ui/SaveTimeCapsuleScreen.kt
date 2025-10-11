package com.emotionstorage.time_capsule_detail.ui

import android.view.Gravity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.common.toKorDate
import com.emotionstorage.time_capsule_detail.presentation.SaveTimeCapsuleAction
import com.emotionstorage.time_capsule_detail.presentation.SaveTimeCapsuleSideEffect.SaveTimeCapsuleSuccess
import com.emotionstorage.time_capsule_detail.presentation.SaveTimeCapsuleSideEffect.ShowToast
import com.emotionstorage.time_capsule_detail.presentation.SaveTimeCapsuleState
import com.emotionstorage.time_capsule_detail.presentation.SaveTimeCapsuleState.ArriveAfter
import com.emotionstorage.time_capsule_detail.presentation.SaveTimeCapsuleViewModel
import com.emotionstorage.time_capsule_detail.ui.component.SaveTimeCapsuleButton
import com.emotionstorage.time_capsule_detail.ui.component.TimeCapsuleSpeechBubble
import com.emotionstorage.time_capsule_detail.ui.modal.CheckArriveDateModal
import com.emotionstorage.time_capsule_detail.ui.modal.TimeCapsuleExpiredModal
import com.emotionstorage.time_capsule_detail.ui.modal.TimeCapsuleSavedModal
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.AppSnackbarHost
import com.emotionstorage.ui.component.DatePickerBottomSheet
import com.emotionstorage.ui.component.FullLoadingScreen
import com.emotionstorage.ui.component.Toast
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.component.YearMonthPickerBottomSheet
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.subBackground
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun SaveTimeCapsuleScreen(
    id: String,
    modifier: Modifier = Modifier,
    isNewTimeCapsule: Boolean = true,
    viewModel: SaveTimeCapsuleViewModel = hiltViewModel(),
    navToMain: () -> Unit = {},
    navToPrevious: () -> Unit = {},
    navToBack: () -> Unit = {},
) {
    val state = viewModel.container.stateFlow.collectAsState()
    val snackState = remember { SnackbarHostState() }
    val (showSavedModal, setShowSavedModal) = remember { mutableStateOf(false) }

    LaunchedEffect(id, isNewTimeCapsule) {
        viewModel.onAction(SaveTimeCapsuleAction.Init(id, isNewTimeCapsule))

        viewModel.container.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is SaveTimeCapsuleSuccess -> {
                    setShowSavedModal(true)
                }

                is ShowToast -> {
                    snackState.currentSnackbarData?.dismiss()
                    snackState.showSnackbar(sideEffect.toast)
                }
            }
        }
    }

    if (state.value.isLoading) {
        FullLoadingScreen()
    } else {
        StatelessSaveTimeCapsuleScreen(
            modifier = modifier,
            snackbarHostState = snackState,
            state = state.value,
            onAction = viewModel::onAction,
            showSavedModal = showSavedModal,
            dismissSavedModal = { setShowSavedModal(false) },
            navToMain = navToMain,
            navToPrevious = navToPrevious,
            navToBack = navToBack,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatelessSaveTimeCapsuleScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    state: SaveTimeCapsuleState = SaveTimeCapsuleState(),
    onAction: (SaveTimeCapsuleAction) -> Unit = {},
    showSavedModal: Boolean = false,
    dismissSavedModal: () -> Unit = {},
    navToMain: () -> Unit = {},
    navToPrevious: () -> Unit = {},
    navToBack: () -> Unit = {},
) {
    val (showToolTip, setShowToolTip) = remember { mutableStateOf(false) }

    val (showExpiredModal, setShowExpiredModal) = remember { mutableStateOf(false) }
    val (showCheckOpenDateModal, setShowCheckOpenDateModal) = remember { mutableStateOf(false) }

    val (showYearMonthPicker, setShowYearMonthPicker) = remember { mutableStateOf(false) }
    val (showDatePicker, setShowDatePicker) = remember { mutableStateOf(false) }

    TimeCapsuleSavedModal(
        isModalOpen = showSavedModal,
        onConfirm = {
            dismissSavedModal()
            navToMain()
        },
    )

    if (!showSavedModal) {
        TimeCapsuleExpiredModal(
            isModalOpen = showExpiredModal,
            onConfirm = {
                setShowExpiredModal(false)
                navToPrevious()
            },
        )
    }

    if (state.arriveAt != null) {
        CheckArriveDateModal(
            createdAt = state.createdAt.toLocalDate(),
            arriveAt = state.arriveAt.toLocalDate(),
            isModalOpen = showCheckOpenDateModal,
            onDismissRequest = {
                setShowCheckOpenDateModal(false)
            },
            onSaveOpenDate = {
                onAction(SaveTimeCapsuleAction.SaveTimeCapsule)
            },
        )
    }

    Scaffold(
        modifier =
            modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background)
                .clickable {
                    setShowToolTip(false)
                },
        topBar = {
            TopAppBar(
                showBackground = false,
                showBackButton = true,
                onBackClick = navToBack,
            )
        },
        snackbarHost = {
            AppSnackbarHost(
                hostState = snackbarHostState,
                gravity = Gravity.TOP,
            ) { snackbarData ->
                // todo: change toast duration to 4s
                Toast(
                    message = snackbarData.visuals.message,
                    paddingValues = PaddingValues(horizontal = 25.dp, vertical = 13.dp),
                )
            }
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background)
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 39.67.dp),
        ) {
            if (showToolTip) {
                Image(
                    modifier =
                        Modifier
                            .align(Alignment.TopStart)
                            .zIndex(20f)
                            .offset(
                                x = 18.dp,
                                y = 73.dp,
                            ).size(310.dp, 144.dp),
                    painter =
                        painterResource(
                            com
                                .emotionstorage
                                .time_capsule_detail
                                .R
                                .drawable
                                .open_date_tooltip,
                        ),
                    contentDescription = "tooltip",
                )
            }

            // title & selection grid
            Column(
                modifier = Modifier.align(Alignment.TopStart),
                verticalArrangement = Arrangement.spacedBy(17.dp),
            ) {
                SaveTimeCapsuleTitle(
                    onToolTipClick = { setShowToolTip(true) },
                )
                SaveTimeCapsuleGrid(
                    arriveAt = state.arriveAt?.toLocalDate(),
                    arriveAfter = state.arriveAfter,
                    onSelectArriveAfter = {
                        onAction(SaveTimeCapsuleAction.SelectArriveAfter(it))
                    },
                    onOpenDatePicker = {
                        setShowDatePicker(true)
                    },
                )
            }

            // speech bubble & button
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(11.dp),
            ) {
                TimeCapsuleSpeechBubble(
                    isNewTimeCapsule = state.isNewTimeCapsule,
                    createdAt = state.createdAt,
                    saveAt = state.saveAt,
                    arriveAt = state.arriveAt,
                    emotions = state.emotions,
                )

                SaveTimeCapsuleButton(
                    isNewTimeCapsule = state.isNewTimeCapsule,
                    expireAt = state.expireAt,
                    enabled = state.arriveAt != null,
                    onSave = {
                        if (state.isNewTimeCapsule) {
                            onAction(SaveTimeCapsuleAction.SaveTimeCapsule)
                        } else {
                            setShowCheckOpenDateModal(true)
                        }
                    },
                    onExpire = {
                        setShowExpiredModal(true)
                    },
                )
            }
        }

        // bottom sheets
        if (showDatePicker && !showYearMonthPicker) {
            DatePickerBottomSheet(
                onDismissRequest = {
                    setShowDatePicker(false)
                    // reset calendar year month to now
                    onAction(SaveTimeCapsuleAction.SelectCalendarYearMonth(YearMonth.now()))
                },
                selectedDate = state.arriveAt?.toLocalDate(),
                onDateSelect = {
                    onAction(SaveTimeCapsuleAction.SelectArriveAt(it))
                    setShowDatePicker(false)
                },
                calendarYearMonth = state.calendarYearMonth,
                onYearMonthSelect = {
                    onAction(SaveTimeCapsuleAction.SelectCalendarYearMonth(it))
                },
                onYearMonthDropdownClick = {
                    // close current bottom sheet & open year month picker
                    setShowDatePicker(false)
                    setShowYearMonthPicker(true)
                },
                minDate = state.saveAt.toLocalDate(),
                maxDate = state.saveAt.plusYears(1).toLocalDate(),
            )
        }
        if (!showDatePicker && showYearMonthPicker) {
            YearMonthPickerBottomSheet(
                onDismissRequest = {
                    // close current bottom sheet & reopen date picker
                    setShowYearMonthPicker(false)
                    setShowDatePicker(true)
                },
                selectedYearMonth = state.calendarYearMonth,
                onYearMonthSelect = {
                    // select year month & reopen date picker
                    onAction(SaveTimeCapsuleAction.SelectCalendarYearMonth(it))
                    setShowYearMonthPicker(false)
                    setShowDatePicker(true)
                },
                minYearMonth = YearMonth.from(state.saveAt),
                maxYearMonth = YearMonth.from(state.saveAt).plusYears(1),
            )
        }
    }
}

@Composable
private fun SaveTimeCapsuleTitle(
    modifier: Modifier = Modifier,
    onToolTipClick: () -> Unit = {},
) {
    Column(modifier = modifier) {
        Text(
            text = "이 감정을",
            style = MooiTheme.typography.head1,
            color = Color.White,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text =
                    buildAnnotatedString {
                        withStyle(SpanStyle(color = MooiTheme.colorScheme.primary)) {
                            append("언제 다시 ")
                        }
                        append("꺼내볼까요?")
                    },
                style = MooiTheme.typography.head1,
                color = Color.White,
            )
            Image(
                modifier =
                    Modifier
                        .size(26.dp)
                        .clickable(
                            onClick = onToolTipClick,
                        ),
                painter = painterResource(R.drawable.info),
                contentDescription = "tool tip",
            )
        }
    }
}

@Composable
fun SaveTimeCapsuleGrid(
    modifier: Modifier = Modifier,
    arriveAt: LocalDate? = null,
    arriveAfter: ArriveAfter? = null,
    onSelectArriveAfter: (ArriveAfter?) -> Unit = {},
    onOpenDatePicker: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "타임캡슐 오픈일",
            style = MooiTheme.typography.body1,
            color = Color.White,
        )
        Text(
            modifier = Modifier.padding(top = 4.dp, bottom = 15.dp),
            text = "* 감정 회고일을 선택하세요. 한 번 더 탭하면 해제돼요.",
            style = MooiTheme.typography.caption7,
            color = MooiTheme.colorScheme.gray500,
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            for (arriveAfters in ArriveAfter.entries.chunked(3)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    for (it in arriveAfters) {
                        ArriveAfterGridItem(
                            arriveAfter = it,
                            isSelected = it == arriveAfter,
                            onSelect = {
                                if (it == arriveAfter) {
                                    // remove selection on double click
                                    onSelectArriveAfter(null)
                                } else {
                                    onSelectArriveAfter(it)
                                }
                            },
                            arriveAt = arriveAt,
                            onDatePickerClick = {
                                onOpenDatePicker()
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.ArriveAfterGridItem(
    arriveAfter: ArriveAfter,
    modifier: Modifier = Modifier,
    onSelect: () -> Unit = {},
    isSelected: Boolean = false,
    arriveAt: LocalDate? = null,
    onDatePickerClick: (() -> Unit)? = null,
) {
    Box(
        modifier =
            modifier
                .size(95.dp, 54.dp)
                .subBackground(
                    enabled = isSelected,
                    defaultBackground = Color.Black,
                    shape = RoundedCornerShape(10.dp),
                ).clickable {
                    onSelect()
                },
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = arriveAfter.label,
            style = MooiTheme.typography.body8,
            color = if (isSelected) MooiTheme.colorScheme.primary else Color.White,
        )
    }

    if (arriveAfter == ArriveAfter.AFTER_CUSTOM && isSelected) {
        Row(
            modifier =
                Modifier
                    .size(198.dp, 54.dp)
                    .subBackground(enabled = true, shape = RoundedCornerShape(10.dp))
                    .clickable {
                        onDatePickerClick?.invoke()
                    }.padding(
                        start = 17.dp,
                        end = 20.dp,
                    ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (arriveAt == null) {
                Text(
                    "날짜를 선택해주세요",
                    style = MooiTheme.typography.body8,
                    color = MooiTheme.colorScheme.gray600,
                )
            } else {
                Text(
                    arriveAt.toKorDate(),
                    style = MooiTheme.typography.body8,
                    color = Color.White,
                )
            }
            Image(
                modifier = Modifier.size(16.dp),
                painter = painterResource(R.drawable.calendar),
                contentDescription = "calendar date picker",
            )
        }
    }
}

@PreviewScreenSizes
@Composable
private fun SaveTimeCapsuleScreenPreview() {
    MooiTheme {
        StatelessSaveTimeCapsuleScreen(
            state =
                SaveTimeCapsuleState(
                    isLoading = false,
                    emotions = listOf("\uD83D\uDE14 서운함", "\uD83D\uDE0A 고마움", "\uD83E\uDD70 안정감"),
                ),
        )
    }
}
