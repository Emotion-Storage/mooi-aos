package com.emotionstorage.home.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.emotionstorage.domain.model.AttendanceSummary
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.button.CtaButton
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun AttendanceRewardDialog(
    summary: AttendanceSummary,
    onConfirm: () -> Unit,
) {
    Dialog(
        properties =
            DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
            ),
        onDismissRequest = {},
    ) {
        Column(
            modifier =
                Modifier
                    .background(
                        color = MooiTheme.colorScheme.backgroundDefault,
                        shape = RoundedCornerShape(15.dp),
                    ).fillMaxWidth()
                    .widthIn(min = 328.dp)
                    .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.size(26.dp))

            Row(
                modifier =
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .height(31.dp),
            ) {
                Image(
                    modifier =
                        Modifier
                            .size(24.dp)
                            .align(Alignment.CenterVertically),
                    painter = painterResource(R.drawable.ic_gift),
                    contentDescription = "선물",
                )

                Spacer(modifier = Modifier.size(4.dp))

                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = "출석 보상 도착!",
                    style = MooiTheme.typography.head2,
                    color = Color.White,
                )
            }

            Spacer(modifier = Modifier.size(8.dp))

            Text(
                modifier = Modifier.height(48.dp),
                text = "당신의 오늘을 함께해서 기뻐요.\n매일 출석하면, 열쇠 보상이 가득 쌓여요!",
                style = MooiTheme.typography.body6,
                color = MooiTheme.colorScheme.gray500,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.size(18.dp))

            Box(
                modifier = Modifier.padding(horizontal = 10.dp),
            ) {
                Column {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(9.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        summary
                            .days
                            .slice(0..2)
                            .forEach { DayCircle(day = it, modifier = Modifier.weight(1f)) }
                    }

                    Spacer(modifier = Modifier.size(11.dp))

                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(9.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        summary
                            .days
                            .slice(3..5)
                            .forEach { DayCircle(day = it, modifier = Modifier.weight(1f)) }
                    }

                    Spacer(modifier = Modifier.size(11.dp))

                    DayCircle(
                        day = summary.days[6],
                        modifier =
                            Modifier
                                .fillMaxWidth(),
                    )
                }
            }

            Spacer(modifier = Modifier.size(21.dp))

            Row {
                Text(
                    modifier =
                        Modifier.padding(
                            top = 14.dp,
                        ),
                    text = "*",
                    style = MooiTheme.typography.caption7,
                    color = MooiTheme.colorScheme.primaryBlue500,
                )
                Text(
                    modifier =
                        Modifier.padding(
                            start = 2.dp,
                            top = 16.dp,
                        ),
                    text = "출석 보상은 매일 자정 초기화 돼요.",
                    style = MooiTheme.typography.caption7,
                    color = MooiTheme.colorScheme.primaryBlue500,
                )
            }

            Spacer(modifier = Modifier.size(8.dp))

            CtaButton(
                modifier =
                    Modifier
                        .fillMaxWidth(0.65f)
                        .height(50.dp)
                        .align(Alignment.CenterHorizontally),
                onClick = onConfirm,
                radius = 10,
                isDefaultWidth = false,
                isDefaultHeight = false,
            ) {
                Text(
                    text = "보상 받기",
                    style = MooiTheme.typography.mainButton,
                    color = Color.White,
                )
            }

            Spacer(modifier = Modifier.size(26.dp))
        }
    }
}

@Preview
@Composable
private fun AttendanceRewardDialogPreview() {
    MooiTheme {
        AttendanceRewardDialog(
            summary =
                AttendanceSummary(
                    days =
                        listOf(
                            AttendanceSummary.Attendance(
                                1,
                                1,
                                AttendanceSummary.AttendanceStatus.ATTENDED,
                            ),
                            AttendanceSummary.Attendance(
                                2,
                                1,
                                AttendanceSummary.AttendanceStatus.ATTENDED,
                            ),
                            AttendanceSummary.Attendance(
                                3,
                                1,
                                AttendanceSummary.AttendanceStatus.TODAY,
                            ),
                            AttendanceSummary.Attendance(
                                4,
                                1,
                                AttendanceSummary.AttendanceStatus.UPCOMING,
                            ),
                            AttendanceSummary.Attendance(
                                5,
                                1,
                                AttendanceSummary.AttendanceStatus.UPCOMING,
                            ),
                            AttendanceSummary.Attendance(
                                6,
                                1,
                                AttendanceSummary.AttendanceStatus.UPCOMING,
                            ),
                            AttendanceSummary.Attendance(
                                7,
                                3,
                                AttendanceSummary.AttendanceStatus.UPCOMING,
                            ),
                        ),
                    true,
                ),
            onConfirm = {},
        )
    }
}
