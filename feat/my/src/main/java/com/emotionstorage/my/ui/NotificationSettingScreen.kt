package com.emotionstorage.my.ui

import android.Manifest
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.emotionstorage.my.presentation.NotificationSettingState
import com.emotionstorage.my.presentation.NotificationSettingViewModel
import com.emotionstorage.my.ui.component.DayOfWeekSelector
import com.emotionstorage.my.ui.component.ReminderTimeComponent
import com.emotionstorage.my.ui.component.ToggleRow
import com.emotionstorage.ui.component.bottomSheet.TimePickerBottomSheet
import com.emotionstorage.ui.component.appBar.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun NotificationSettingScreen(
    viewModel: NotificationSettingViewModel = hiltViewModel(),
    navToBack: () -> Unit,
) {
    val state = viewModel.state.collectAsState()
    val context = LocalContext.current

    var showTimeSelectSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(state.value.emotionReminderNotify) {
        if (!state.value.emotionReminderNotify) showTimeSelectSheet = false
    }

    val postPermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            null
        }

    var systemEnabled by remember { mutableStateOf(NotificationManagerCompat.from(context).areNotificationsEnabled()) }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val obs =
            LifecycleEventObserver { _, ev ->
                if (ev == Lifecycle.Event.ON_RESUME) {
                    systemEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()
                }
            }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }

    val postGranted =
        remember(postPermission?.status) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                postPermission?.status is PermissionStatus.Granted
            } else {
                true
            }
        }
    val notificationsAllowed = systemEnabled && postGranted

    // TODO : 알림 허용 유도 Bottom Sheet 등장 용 값 -> var 제거 필요
    var showPermissionSheet by remember { mutableStateOf(!notificationsAllowed) }
    LaunchedEffect(notificationsAllowed) { showPermissionSheet = !notificationsAllowed }

    StatelessNotificationSettingScreen(
        state = state.value,
        notificationsAllowed = notificationsAllowed,
        onToggleAppPush = viewModel::setAppPush,
        onToggleEmotionReminder = viewModel::setEmotionReminder,
        onToggleTimeCapsuleAndReport = viewModel::setTimeCapsule,
        onToggleMarketing = viewModel::setMarketing,
        onDayClick = viewModel::toggleDay,
        onClickTime = { if (state.value.emotionReminderNotify) showTimeSelectSheet = true },
        navToBack = {
            viewModel.save()
            navToBack()
        },
    )

    if (showTimeSelectSheet) {
        TimePickerBottomSheet(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .heightIn(120.dp),
            sheetState = sheetState,
            initialTime = state.value.emotionReminderTime,
            onDismissRequest = { showTimeSelectSheet = false },
            onTimeSelected = { selectedTime ->
                viewModel.setTime(selectedTime)
                showTimeSelectSheet = false
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatelessNotificationSettingScreen(
    state: NotificationSettingState,
    notificationsAllowed: Boolean,
    onToggleAppPush: (Boolean) -> Unit = {},
    onToggleEmotionReminder: (Boolean) -> Unit = {},
    onToggleTimeCapsuleAndReport: (Boolean) -> Unit = {},
    onToggleMarketing: (Boolean) -> Unit = {},
    onDayClick: (DayOfWeek) -> Unit = {},
    onClickTime: () -> Unit = {},
    navToBack: () -> Unit,
) {
    // 알림이 허용 되었을 때 사용할 값
    val timeSelectedEnabled = true

    Scaffold(
        topBar = {
            TopAppBar(
                title = "알림 설정",
                onBackClick = navToBack,
                showBackButton = true,
            )
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = MooiTheme.colorScheme.background),
        ) {
            Column(
                modifier = Modifier.padding(innerPadding),
            ) {
                ToggleRow(
                    modifier = Modifier.padding(top = 24.dp),
                    title = "MOOI 앱 푸시 알림",
                    isChecked = state.appPushNotify,
                    onCheckedChange = onToggleAppPush,
                    enabled = notificationsAllowed,
                )

                if (notificationsAllowed) {
                    if (state.appPushNotify) {
                        ToggleRow(
                            modifier = Modifier.padding(top = 29.dp),
                            title = "감정 기록 알림",
                            isChecked = state.emotionReminderNotify,
                            onCheckedChange = onToggleEmotionReminder,
                            enabled = true,
                        )

                        Spacer(modifier = Modifier.size(30.dp))

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MooiTheme.colorScheme.gray800,
                            thickness = 1.5.dp,
                        )

                        Spacer(modifier = Modifier.size(29.dp))

                        if (state.emotionReminderNotify) {
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                text = "감정 기록 알림 시간",
                                style = MooiTheme.typography.body7,
                                color = Color.White,
                            )

                            Row {
                                Text(
                                    modifier =
                                        Modifier.padding(
                                            start = 16.dp,
                                            top = 8.dp,
                                        ),
                                    text = "*",
                                    style = MooiTheme.typography.caption7,
                                    color = MooiTheme.colorScheme.gray500,
                                )
                                Text(
                                    modifier =
                                        Modifier.padding(
                                            start = 2.dp,
                                            top = 10.dp,
                                        ),
                                    text = "원하는 요일을 모두 선택해주세요. 한 번 더 탭하면 해제돼요.",
                                    style = MooiTheme.typography.caption7,
                                    color = MooiTheme.colorScheme.gray500,
                                )
                            }

                            Spacer(modifier = Modifier.size(14.dp))

                            // TODO : 실 기기에서는 잘 보이는데 Preview에서는 가운데로 몰리는 것 같음
                            DayOfWeekSelector(
                                modifier = Modifier.fillMaxWidth(),
                                selected = state.emotionReminderDays,
                                enabled = state.emotionReminderNotify,
                                onToggle = onDayClick,
                            )

                            Spacer(modifier = Modifier.size(17.dp))

                            ReminderTimeComponent(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                time = state.emotionReminderTime,
                                enabled = state.emotionReminderNotify,
                                onClick = onClickTime,
                            )

                            Spacer(modifier = Modifier.size(29.dp))

                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = MooiTheme.colorScheme.gray800,
                                thickness = 1.5.dp,
                            )

                            Spacer(modifier = Modifier.size(30.dp))
                        } else {
                            Spacer(modifier = Modifier.size(0.dp))
                        }

                        ToggleRow(
                            title = "타임캡슐 및 일일리포트\n업데이트 알림",
                            isChecked = state.timeCapsuleReportNotify,
                            onCheckedChange = onToggleTimeCapsuleAndReport,
                            enabled = true,
                            minHeight = 48.dp,
                        )

                        Spacer(modifier = Modifier.size(32.dp))

                        ToggleRow(
                            title = "마케팅 정보 알림",
                            isChecked = state.marketingInfoNotify,
                            onCheckedChange = onToggleMarketing,
                            enabled = true,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun NotificationSettingScreenPreview() {
    MooiTheme {
        StatelessNotificationSettingScreen(
            state = NotificationSettingState(),
            notificationsAllowed = true,
            onToggleAppPush = { },
            onToggleEmotionReminder = {},
            onToggleTimeCapsuleAndReport = {},
            onToggleMarketing = {},
            onDayClick = {},
            onClickTime = {},
            navToBack = {},
        )
    }
}
