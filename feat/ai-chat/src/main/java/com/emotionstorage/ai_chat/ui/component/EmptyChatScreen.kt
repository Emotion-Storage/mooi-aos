package com.emotionstorage.ai_chat.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
fun EmptyChatScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier.size(100.dp),
                painter = painterResource(R.drawable.emotion_0),
                contentDescription = null,
            )
            Spacer(modifier.size(18.dp))
            Text(
                text = "지금 나누고 싶은\n이야기가 있나요?",
                style = MooiTheme.typography.head1,
                color = Color.White,
            )
        }
    }
}

@Preview
@Composable
fun EmptyChatScreenPreview() {
    MooiTheme {
        EmptyChatScreen()
    }
}
