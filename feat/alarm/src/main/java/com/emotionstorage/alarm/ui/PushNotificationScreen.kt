package com.emotionstorage.alarm.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.R

@Composable
fun PushNotificationScreen(
    navToBack: () -> Unit = {},
) {

}

@Composable
private fun StatelessPushNotificationScreen(
    navToBack: () -> Unit,
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = "알림 내역",
                showBackButton = true,
                showBackground = true,
                handleBackPress = true,
                onBackClick = navToBack,
                onHandleBackPress = navToBack,
            )
        }
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(color = MooiTheme.colorScheme.background)
                    .consumeWindowInsets(WindowInsets.navigationBars)
        ) {
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .height(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .size(18.dp),
                        painter = painterResource(R.drawable.alarm),
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
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PushNotificationScreenPreview() {
    MooiTheme {
        StatelessPushNotificationScreen(
            navToBack = {},
        )
    }
}
