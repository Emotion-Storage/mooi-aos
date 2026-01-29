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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.emotionstorage.presentation.BaseSideEffect
import com.emotionstorage.time_capsule_detail.presentation.SaveTimeCapsuleAction
import com.emotionstorage.time_capsule_detail.presentation.SaveTimeCapsuleSideEffect.SaveTimeCapsuleSuccess
import com.emotionstorage.time_capsule_detail.presentation.SaveTimeCapsuleSideEffect.ShowToast
import com.emotionstorage.time_capsule_detail.presentation.SaveTimeCapsuleState
import com.emotionstorage.time_capsule_detail.presentation.SaveTimeCapsuleState.OpenAfter
import com.emotionstorage.time_capsule_detail.presentation.SaveTimeCapsuleViewModel
import com.emotionstorage.time_capsule_detail.ui.component.SaveTimeCapsuleButton
import com.emotionstorage.time_capsule_detail.ui.component.TimeCapsuleSpeechBubble
import com.emotionstorage.time_capsule_detail.ui.modal.CheckOpenDateModal
import com.emotionstorage.time_capsule_detail.ui.modal.TimeCapsuleExpiredModal
import com.emotionstorage.time_capsule_detail.ui.modal.TimeCapsuleSavedModal
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.toast.AppSnackbarHost
import com.emotionstorage.ui.component.bottomSheet.DatePickerBottomSheet
import com.emotionstorage.ui.component.toast.Toast
import com.emotionstorage.ui.component.appBar.TopAppBar
import com.emotionstorage.ui.component.bottomSheet.YearMonthPickerBottomSheet
import com.emotionstorage.ui.component.loading.LoadingOverlay
import com.emotionstorage.ui.component.modal.LoginSessionExpiredModal
import com.emotionstorage.ui.component.modal.TempErrorModal
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.subBackground
import java.time.LocalDate
import java.time.YearMonth

private sealed class ModalState {
    object None : ModalState()

    data class CheckOpenDate(
        val createdAt: LocalDate,
        val openAt: LocalDate,
    ) : ModalState()

    object Expired : ModalState()

    object SaveSuccess : ModalState()

    object TempError : ModalState()

    object LoginSessionExpired : ModalState()
}

@Composable
fun SaveTimeCapsuleScreen(
    id: Long,
    navToMain: () -> Unit,
    navToPrevious: () -> Unit,
    navToBack: () -> Unit,
    navToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    isNewTimeCapsule: Boolean = true,
    viewModel: SaveTimeCapsuleViewModel = hiltViewModel(),
) {
    val state = viewModel.container.stateFlow.collectAsState()
    val snackState = remember { SnackbarHostState() }
    val (modalState, setModalState) = remember { mutableStateOf<ModalState>(ModalState.None) }

    LaunchedEffect(id, isNewTimeCapsule) {
        viewModel.onAction(SaveTimeCapsuleAction.Init(id, isNewTimeCapsule))

        viewModel.container.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is SaveTimeCapsuleSuccess -> {
                    setModalState(ModalState.SaveSuccess)
                }

                is ShowToast -> {
                    snackState.currentSnackbarData?.dismiss()
                    snackState.showSnackbar(sideEffect.toast)
                }

                is BaseSideEffect.TemporalError -> {
                    setModalState(ModalState.TempError)
                }

                is BaseSideEffect.SessionExpired -> {
                    setModalState(ModalState.LoginSessionExpired)
                }
            }
        }
    }

    if (state.value.isLoading) {
        LoadingOverlay()
    }
    StatelessSaveTimeCapsuleScreen(
        modifier = modifier,
        snackbarState = snackState,
        setModalState = setModalState,
        state = state.value,
        onAction = viewModel::onAction,
        navToBack = navToBack,
    )

    when (modalState) {
        is ModalState.None -> {
            // no modal
        }

        is ModalState.CheckOpenDate -> {
            val snapShot = modalState
            CheckOpenDateModal(
                onDismissRequest = {
                    setModalState(ModalState.None)
                },
                createdAt = snapShot.createdAt,
                openAt = snapShot.openAt,
                onSaveOpenDate = {
                    viewModel.onAction(SaveTimeCapsuleAction.SaveTimeCapsule)
                },
            )
        }

        ModalState.Expired -> {
            TimeCapsuleExpiredModal(
                onDismissRequest = {
                    setModalState(ModalState.None)
                },
                onConfirm = {
                    navToPrevious()
                },
            )
        }

        ModalState.SaveSuccess -> {
            TimeCapsuleSavedModal(
                onConfirm = {
                    setModalState(ModalState.None)
                    navToMain()
                },
            )
        }

        ModalState.TempError -> {
            TempErrorModal(
                onDismissRequest = {
                    setModalState(ModalState.None)
                },
            )
        }

        ModalState.LoginSessionExpired -> {
            LoginSessionExpiredModal(
                onDismissRequest = {
                    setModalState(ModalState.None)
                },
                navToLogin = navToLogin,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatelessSaveTimeCapsuleScreen(
    modifier: Modifier = Modifier,
    snackbarState: SnackbarHostState = remember { SnackbarHostState() },
    setModalState: (ModalState) -> Unit = {},
    state: SaveTimeCapsuleState = SaveTimeCapsuleState(),
    onAction: (SaveTimeCapsuleAction) -> Unit = {},
    navToBack: () -> Unit = {},
) {
    val (showToolTip, setShowToolTip) = remember { mutableStateOf(false) }
    val (showYearMonthPicker, setShowYearMonthPicker) = remember { mutableStateOf(false) }
    val (showDatePicker, setShowDatePicker) = remember { mutableStateOf(false) }

    Scaffold(
        modifier =
            modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.backgroundDefault)
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
                hostState = snackbarState,
                gravity = Gravity.TOP,
            ) { message, iconId ->
                Toast(
                    message = message,
                    iconId = iconId,
                    paddingValues = PaddingValues(horizontal = 25.dp, vertical = 13.dp),
                )
            }
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.backgroundDefault)
                    .padding(innerPadding)
                    .padding(bottom = 39.67.dp),
        ) {
            if (showToolTip) {
                Image(
                    modifier =
                        Modifier
                            .align(Alignment.TopStart)
                            .zIndex(10f)
                            .offset(
                                x = 34.dp,
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
                modifier =
                    Modifier
                        .align(Alignment.TopStart)
                        .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(17.dp),
            ) {
                SaveTimeCapsuleTitle(
                    onToolTipClick = { setShowToolTip(true) },
                )
                SaveTimeCapsuleGrid(
                    openAt = state.openDateTime?.toLocalDate(),
                    openAfter = state.openAfter,
                    onSelectOpenAfter = {
                        onAction(SaveTimeCapsuleAction.SelectOpenAfter(it))
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
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(11.dp),
            ) {
                TimeCapsuleSpeechBubble(
                    isNewTimeCapsule = state.isNewTimeCapsule,
                    createdAt = state.createdAt,
                    saveAt = state.saveAt,
                    openAt = state.openDateTime,
                    emotions = state.emotions,
                )

                SaveTimeCapsuleButton(
                    isNewTimeCapsule = state.isNewTimeCapsule,
                    expireAt = state.expireAt,
                    enabled = state.openDateTime != null,
                    onSave = {
                        if (state.isNewTimeCapsule) {
                            onAction(SaveTimeCapsuleAction.SaveTimeCapsule)
                        } else if (state.openDateTime != null) {
                            setModalState(
                                ModalState.CheckOpenDate(
                                    createdAt = state.createdAt.toLocalDate(),
                                    openAt = state.openDateTime.toLocalDate(),
                                ),
                            )
                        }
                    },
                    onExpire = {
                        setModalState(ModalState.Expired)
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
                selectedDate = state.openDateTime?.toLocalDate(),
                onDateSelect = {
                    onAction(SaveTimeCapsuleAction.SelectOpenDate(it))
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
                minDate = state.saveAt.toLocalDate().plusDays(1),
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
                        withStyle(SpanStyle(color = MooiTheme.colorScheme.primaryBlue500)) {
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
                painter = painterResource(R.drawable.ic_info),
                contentDescription = "tool tip",
            )
        }
    }
}

@Composable
fun SaveTimeCapsuleGrid(
    modifier: Modifier = Modifier,
    openAt: LocalDate? = null,
    openAfter: OpenAfter? = null,
    onSelectOpenAfter: (OpenAfter?) -> Unit = {},
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
            for (arriveAfters in OpenAfter.entries.chunked(3)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    for (it in arriveAfters) {
                        OpenAfterGridItem(
                            openAfter = it,
                            isSelected = it == openAfter,
                            onSelect = {
                                if (it == openAfter) {
                                    // remove selection on double click
                                    onSelectOpenAfter(null)
                                } else {
                                    onSelectOpenAfter(it)
                                }
                            },
                            openAt = openAt,
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
private fun RowScope.OpenAfterGridItem(
    openAfter: OpenAfter,
    modifier: Modifier = Modifier,
    onSelect: () -> Unit = {},
    isSelected: Boolean = false,
    openAt: LocalDate? = null,
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
            text = openAfter.label,
            style = MooiTheme.typography.body8,
            color = if (isSelected) MooiTheme.colorScheme.primaryBlue500 else Color.White,
        )
    }

    if (openAfter == OpenAfter.AFTER_CUSTOM && isSelected) {
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
            if (openAt == null) {
                Text(
                    "날짜를 선택해주세요",
                    style = MooiTheme.typography.body8,
                    color = MooiTheme.colorScheme.gray600,
                )
            } else {
                Text(
                    openAt.toKorDate(),
                    style = MooiTheme.typography.body8,
                    color = Color.White,
                )
            }
            Image(
                modifier = Modifier.size(16.dp),
                painter = painterResource(R.drawable.ic_calendar),
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
