package com.emotionstorage.alarm.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun NotificationInfo(modifier: Modifier = Modifier) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Icon(
            modifier =
                Modifier
                    .size(18.dp),
            painter = painterResource(R.drawable.ic_alarm),
            contentDescription = "알림 아이콘",
            tint = MooiTheme.colorScheme.gray600,
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = "최근 3주간의 알림이 표시됩니다.",
            style = MooiTheme.typography.caption7,
            color = MooiTheme.colorScheme.gray500,
        )
    }
}

@Preview
@Composable
private fun NotificationInfoPreview() {
    MooiTheme {
        NotificationInfo()
    }
}
