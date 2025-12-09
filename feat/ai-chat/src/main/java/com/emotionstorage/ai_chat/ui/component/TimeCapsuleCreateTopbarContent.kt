package com.emotionstorage.ai_chat.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun TimeCapsuleCreateTopbarContent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .height(24.dp)
                .background(color = Color.Transparent)
                .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_time_capsule_primary),
            contentDescription = "create time capsule",
        )

        Spacer(modifier = Modifier.size(6.dp))

        Text(
            text = "타임캡슐 만들기",
            style = MooiTheme.typography.caption2,
            color = MooiTheme.colorScheme.primaryBlue500,
        )
    }
}

@Preview
@Composable
private fun ChattingFinishButtonPreview() {
    MooiTheme {
        TimeCapsuleCreateTopbarContent()
    }
}
