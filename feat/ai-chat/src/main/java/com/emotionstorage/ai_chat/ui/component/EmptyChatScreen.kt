package com.emotionstorage.ai_chat.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun EmptyChatScreen(
    modifier: Modifier = Modifier,
    isKeyboardVisible: Boolean = false,
) {
    val imageScale by animateFloatAsState(
        targetValue = if (isKeyboardVisible) 1.5f else 1f,
        label = "image_scale",
    )

    val imageAlpha by animateFloatAsState(
        targetValue = if (isKeyboardVisible) 0.3f else 1f,
        label = "image_alpha",
    )

    val textAlpha by animateFloatAsState(
        targetValue = if (isKeyboardVisible) 0f else 1f,
        label = "text_alpha",
    )

    Box(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier
                    .size(186.dp, 155.dp)
                    .offset(y = 5.dp)
                    .graphicsLayer {
                        scaleX = imageScale
                        scaleY = imageScale
                    }
                    .alpha(imageAlpha),
                painter = painterResource(R.drawable.graphic_ai_chat),
                contentDescription = null,
            )

            Text(
                modifier = Modifier.alpha(textAlpha),
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
