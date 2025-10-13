package com.emotionstorage.my.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

// TODO : 색상 확인하기
@Composable
fun ReminderTimeComponent(
    modifier: Modifier = Modifier,
    time: LocalTime,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            modifier
                .widthIn(126.dp)
                .heightIn(50.dp)
                .border(1.dp, brush = MooiTheme.brushScheme.subButtonBorder, shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(brush = MooiTheme.brushScheme.subButtonBackground)
                .clickable(
                    enabled = enabled,
                    onClick = onClick,
                ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = time.formatKoreanAmPm(),
            style = MooiTheme.typography.caption3,
            color = MooiTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.size(16.dp))

        Icon(
            painter = painterResource(R.drawable.clock),
            tint = MooiTheme.colorScheme.gray500,
            contentDescription = null,
        )
    }
}

private fun LocalTime.formatKoreanAmPm(): String {
    val pattern = DateTimeFormatter.ofPattern("a h : mm", Locale.KOREAN)
    return format(pattern)
}

@Preview
@Composable
fun ReminderTimeComponentPreview() {
    MooiTheme {
        ReminderTimeComponent(
            modifier = Modifier,
            time = LocalTime.now(),
            enabled = true,
            onClick = {
            },
        )
    }
}
