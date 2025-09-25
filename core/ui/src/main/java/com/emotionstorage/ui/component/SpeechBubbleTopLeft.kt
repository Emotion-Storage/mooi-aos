package com.emotionstorage.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.dpToPixel

@Composable
fun SpeechBubbleTopLeft(
    text: String,
    modifier: Modifier = Modifier.size(222.dp, 122.dp),
) {
    val context = LocalContext.current

    val cornerRadius = dpToPixel(context, 8f)
    val tailWidth = dpToPixel(context, 14f)
    val tailHeight = dpToPixel(context, 12f)
    val bgBrush = MooiTheme.brushScheme.subButtonBackground

    Box(
        modifier = modifier,
    ) {
        Canvas(
            modifier =
                Modifier
                    .matchParentSize(),
        ) {
            val rectTop = tailHeight

            val path =
                Path().apply {
                    addRoundRect(
                        RoundRect(
                            left = 0f,
                            top = rectTop,
                            right = size.width,
                            bottom = size.height,
                            cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                        ),
                    )

                    val leftOffset = tailWidth
                    moveTo(leftOffset - tailWidth / 2, rectTop)
                    lineTo(leftOffset, 0f)
                    lineTo(leftOffset + tailWidth / 2, rectTop)
                    close()
                }

            drawPath(
                path = path,
                brush = bgBrush,
            )
        }

        Text(
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 26.dp),
            text = text,
            style = MooiTheme.typography.body4.copy(lineHeight = 20.sp),
            color = MooiTheme.colorScheme.gray300,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
fun SpeechBubbleTopLeftPreview() {
    MooiTheme {
        SpeechBubbleTopLeft(
            text = "대화를 충분히 나누지 않으면\n타임캡슐을 만들 수 없어요.\n사용한 티켓은 되돌릴 수 없으니,\n나가기는 신중히!",
        )
    }
}
