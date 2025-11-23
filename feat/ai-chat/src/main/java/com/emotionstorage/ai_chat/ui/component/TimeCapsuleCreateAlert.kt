package com.emotionstorage.ai_chat.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.theme.MooiTheme
// TODO : Change To Toast
@Composable
fun TimeCapsuleCreateAlert(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = 50.dp)
                .background(
                    color = MooiTheme.colorScheme.bottomBarBackground.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(100.dp),
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "감정이 충분히 수집되어, 타임캡슐을 만들 수 있어요.",
            style = MooiTheme.typography.body7,
            color = Color.White,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun TimeCapsuleCreateAlertPreview() {
    MooiTheme {
        TimeCapsuleCreateAlert()
    }
}
