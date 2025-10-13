package com.emotionstorage.my.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.my.ui.component.DayOfWeekSelector
import com.emotionstorage.my.ui.component.ReminderTimeComponent
import com.emotionstorage.my.ui.component.ToggleRow
import com.emotionstorage.ui.component.TimePickerBottomSheet
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme
import java.time.DayOfWeek
import java.time.LocalTime

@Composable
fun NotificationSettingScreen(navToBack: () -> Unit) {
    StatelessNotificationSettingScreen(
        navToBack = navToBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatelessNotificationSettingScreen(navToBack: () -> Unit) {

    var reminderTime by remember { mutableStateOf(LocalTime.of(21, 0)) }
    // 알림이 허용 되었을 때 사용할 값
    val timeSelectedEnabled = true

    var showTimeSelectSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)


    Scaffold(
        topBar = {
            TopAppBar(
                title = "알림 설정",
                onBackClick = navToBack,
                showBackButton = true,
                handleBackPress = true,
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
                    modifier = Modifier.padding(vertical = 24.dp),
                    title = "MOOI 앱 푸시 알림",
                    isChecked = true,
                    onCheckedChange = { value -> },
                    enabled = true,
                )

                ToggleRow(
                    title = "감정 기록 알림",
                    isChecked = false,
                    onCheckedChange = { value -> },
                    enabled = true,
                )

                Spacer(modifier = Modifier.size(24.dp))

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MooiTheme.colorScheme.gray800,
                    thickness = 1.5.dp,
                )

                Spacer(modifier = Modifier.size(28.dp))

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
                    selected = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
                    enabled = true,
                    onToggle = { dayOfWeek ->

                    },
                )

                Spacer(modifier = Modifier.size(16.dp))

                ReminderTimeComponent(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    time = reminderTime,
                    enabled = true,
                    onClick = { if (timeSelectedEnabled) showTimeSelectSheet = true },
                )

                Spacer(modifier = Modifier.size(28.dp))

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MooiTheme.colorScheme.gray800,
                    thickness = 1.5.dp,
                )

                Spacer(modifier = Modifier.size(28.dp))

                ToggleRow(
                    title = "타임캡슐 및 일일리포트\n업데이트 알림",
                    isChecked = false,
                    onCheckedChange = { value -> },
                    enabled = true,
                )

                Spacer(modifier = Modifier.size(24.dp))

                ToggleRow(
                    title = "마케팅 정보 알림",
                    isChecked = false,
                    onCheckedChange = { value -> },
                    enabled = true,
                )

                if (showTimeSelectSheet) {
                    TimePickerBottomSheet(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(120.dp),
                        sheetState = sheetState,
                        initialTime = reminderTime,
                        onDismissRequest = { showTimeSelectSheet = false },
                        onTimeSelected = { selectedTime ->
                            reminderTime = selectedTime
                            showTimeSelectSheet = false
                        },
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
            navToBack = {},
        )
    }
}
