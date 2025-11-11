package com.emotionstorage.my.ui

import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.emotionstorage.my.presentation.NotificationSettingState
import com.emotionstorage.my.presentation.NotificationSettingViewModel
import com.emotionstorage.my.ui.component.DayOfWeekSelector
import com.emotionstorage.my.ui.component.ReminderTimeComponent
import com.emotionstorage.my.ui.component.ToggleRow
import com.emotionstorage.ui.component.appBar.TopAppBar
import com.emotionstorage.ui.component.bottomSheet.BottomSheet
import com.emotionstorage.ui.component.bottomSheet.TimePickerBottomSheet
import com.emotionstorage.ui.theme.MooiTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import java.time.DayOfWeek

enum class Sheet { None, Permission, TimePicker }

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun NotificationSettingScreen(
    viewModel: NotificationSettingViewModel = hiltViewModel(),
    navToBack: () -> Unit,
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val permissionState = rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)

    var systemEnabled by remember {
        mutableStateOf(
            androidx.core.app.NotificationManagerCompat
                .from(context).areNotificationsEnabled()
        )
    }

    DisposableEffect(lifecycleOwner) {
        val obs = androidx.lifecycle.LifecycleEventObserver { _, ev ->
            if (ev == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                systemEnabled = androidx.core.app.NotificationManagerCompat
                    .from(context).areNotificationsEnabled()
            }
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }

    val postGranted = if (android.os.Build.VERSION.SDK_INT >= 33)
        (permissionState.status is com.google.accompanist.permissions.PermissionStatus.Granted)
    else true
    val notificationsAllowed = systemEnabled && postGranted


    var activeSheet by remember {
        mutableStateOf(if (notificationsAllowed) Sheet.None else Sheet.Permission)
    }
    LaunchedEffect(notificationsAllowed) {
        activeSheet = if (notificationsAllowed) {
            if (activeSheet == Sheet.Permission) Sheet.None else activeSheet
        } else {
            Sheet.Permission
        }
    }

    val permissionSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val timePickerSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    StatelessNotificationSettingScreen(
        state = state.value,
        notificationsAllowed = notificationsAllowed,
        onToggleAppPush = { on ->
            if (!notificationsAllowed) {
                activeSheet = Sheet.Permission
            } else {
                viewModel.setAppPush(on)
            }
        },
        onToggleEmotionReminder = viewModel::setEmotionReminder,
        onToggleTimeCapsuleAndReport = viewModel::setTimeCapsule,
        onToggleMarketing = viewModel::setMarketing,
        onDayClick = viewModel::toggleDay,
        onClickTime = {
            if (notificationsAllowed && state.value.emotionReminderNotify) {
                activeSheet = Sheet.TimePicker
            }
        },
        navToBack = {
            viewModel.save()
            navToBack()
        },
    )

    when (activeSheet) {
        Sheet.Permission -> {
            BottomSheet(
                sheetState = permissionSheetState,
                onDismissRequest = { /* do-nothing */ },
                subTitle = "앗, 알림이 꺼져 있어요!",
                title = "설정에서 알림을 켜주시면\n감정 기록 시간과 리포트를\n제때 전해드릴게요.\uD83C\uDF19",
                confirmLabel = "설정으로 이동하기",
                onConfirm = {
                    context.startActivity(Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    })
                },
                hideDragHandle = true,
                sheetGesturesEnabled = false,

                )

            BackHandler(enabled = true) {
                activeSheet = Sheet.None
            }
        }

        Sheet.TimePicker -> {
            TimePickerBottomSheet(
                sheetState = timePickerSheetState,
                initialTime = state.value.emotionReminderTime,
                onDismissRequest = { activeSheet = Sheet.None },
                onTimeSelected = {
                    viewModel.setTime(it)
                    activeSheet = Sheet.None
                },
            )
        }

        Sheet.None -> {
            Unit
        }
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
                    isChecked = state.appPushNotify && notificationsAllowed,
                    onCheckedChange = onToggleAppPush,
                    enabled = notificationsAllowed,
                )

                if (state.appPushNotify && notificationsAllowed) {
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
                    }

                    Spacer(modifier = Modifier.size(30.dp))

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
