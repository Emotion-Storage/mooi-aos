package com.emotionstorage.home.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.emotionstorage.home.ui.model.Attendance
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun AttendanceRewardDialog(
    days: List<Attendance>,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier =
                Modifier
                    .background(
                        color = MooiTheme.colorScheme.background,
                        shape = RoundedCornerShape(15.dp),
                    ).fillMaxWidth()
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
                    painter = painterResource(R.drawable.gift),
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

            Spacer(modifier = Modifier.size(5.dp))

            Text(
                text = "당신의 오늘을 함께해서 기뻐요.\n매일 출석하면, 열쇠 보상이 가득 쌓여요!",
                style = MooiTheme.typography.body6,
                color = MooiTheme.colorScheme.gray500,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.size(18.dp))
        }
    }
}

@Preview
@Composable
private fun AttendanceRewardDialogPreview() {
    MooiTheme {
        AttendanceRewardDialog(
            days = listOf(),
            onConfirm = {},
            onDismiss = {},
        )
    }
}
