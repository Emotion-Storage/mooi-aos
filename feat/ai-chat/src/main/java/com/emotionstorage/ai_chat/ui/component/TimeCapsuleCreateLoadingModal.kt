package com.emotionstorage.ai_chat.ui.component

import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.component.Modal
import com.emotionstorage.ui.theme.MooiTheme
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.emotionstorage.ui.R

@Composable
fun TimeCapsuleCreateLoadingModal() {
    val context = LocalContext.current

    Modal(
        topDescription = "잠시만 기다려주세요.",
        title = "지금 나눈 감정을\n타임캡슐에 담는 중이에요.",
        onDismissRequest = { },
        verticalSpacing = 0.dp,
        contentPadding = PaddingValues(top = 23.dp, bottom = 8.dp, start = 36.dp, end = 36.dp),
        content = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier = Modifier.size(74.dp),
                    painter =
                        rememberDrawablePainter(
                            drawable =
                                getDrawable(
                                    context,
                                    R.drawable.gif_loading,
                                ),
                        ),
                    contentDescription = "animated gif",
                )
            }
        },
    )
}

@Preview
@Composable
private fun TimeCapsuleCreateLoadingModalPreview() {
    MooiTheme {
        TimeCapsuleCreateLoadingModal()
    }
}
