import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.dpToPixel

enum class BubbleTail { TopCenter, BottomCenter, TopLeft, }

@Composable
fun SpeechBubble(
    modifier: Modifier = Modifier,
    tail: BubbleTail = BubbleTail.BottomCenter,
    sizeParam: DpSize = DpSize(265.dp, 101.dp),
    cornerRadius: Dp = 10.dp,
    tailWidth: Dp = 14.dp,
    tailHeight: Dp = 12.dp,
    tailOffset: Dp = 14.dp,
    bgBrush: Brush = MooiTheme.brushScheme.commentBackground,
    borderBrush: Brush = MooiTheme.brushScheme.subButtonBorder,
    textStyle: TextStyle = MooiTheme.typography.caption3,
    textColor: Color = MooiTheme.colorScheme.gray300,
    contentText: String? = null,
    content: @Composable (() -> Unit)? = null,
) {
    val context = LocalContext.current

    val cornerRadiusPx = dpToPixel(context, cornerRadius.value)
    val tailWidthPx = dpToPixel(context, tailWidth.value)
    val tailHeightPx = dpToPixel(context, tailHeight.value)
    val tailOffsetPx = dpToPixel(context, tailOffset.value)
    val borderWidth = dpToPixel(context, 1f)

    val padding =
        when (tail) {
            BubbleTail.TopCenter, BubbleTail.TopLeft -> PaddingValues(top = tailHeight)
            BubbleTail.BottomCenter -> PaddingValues(bottom = tailHeight)
        }

    Box(
        modifier =
            modifier
                .then(Modifier.size(sizeParam))
    ) {
        Canvas(
            modifier =
                Modifier
                    .matchParentSize()
                    // set background brush opacity to 20%
                    .graphicsLayer(alpha = 0.2f),
        ) {
            val w = size.width
            val h = size.height

            val path =
                Path().apply {
                    when (tail) {
                        BubbleTail.BottomCenter -> {
                            val rectBottom = h - tailHeightPx
                            addRoundRect(
                                RoundRect(
                                    left = 0f,
                                    top = 0f,
                                    right = w,
                                    bottom = rectBottom,
                                    cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
                                ),
                            )
                            // 하단 가운데 말풍선 꼬리
                            moveTo(w / 2 - tailWidthPx / 2, rectBottom)
                            lineTo(w / 2, h)
                            lineTo(w / 2 + tailWidthPx / 2, rectBottom)
                            close()
                        }

                        BubbleTail.TopCenter -> {
                            val rectTop = tailHeightPx
                            addRoundRect(
                                RoundRect(
                                    left = 0f,
                                    top = rectTop,
                                    right = w,
                                    bottom = h,
                                    cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
                                ),
                            )
                            // 상단 가운데 말풍선 꼬리
                            moveTo(w / 2 - tailWidthPx / 2, rectTop)
                            lineTo(w / 2, 0f)
                            lineTo(w / 2 + tailWidthPx / 2, rectTop)
                            close()
                        }

                        BubbleTail.TopLeft -> {
                            val rectTop = tailHeightPx
                            addRoundRect(
                                RoundRect(
                                    left = 0f,
                                    top = rectTop,
                                    right = w,
                                    bottom = h,
                                    cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
                                ),
                            )
                            // 좌상단 말풍선 꼬리
                            moveTo(tailOffsetPx - tailWidthPx / 2, rectTop)
                            lineTo(tailOffsetPx, 0f)
                            lineTo(tailOffsetPx + tailWidthPx / 2, rectTop)
                            close()
                        }
                    }
                }
            drawPath(path = path, brush = bgBrush, style = Fill)
            // todo: fix border
            // drawPath(path = path, brush = borderBrush, style = Stroke(borderWidth))
        }

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (contentText != null) {
                Text(
                    text = contentText,
                    style = textStyle,
                    color = textColor,
                    textAlign = TextAlign.Center,
                )
            }
            if (content != null) {
                content()
            }
        }
    }
}

@Preview
@Composable
fun SpeechBubblePreview() {
    MooiTheme {
        Column(
            modifier = Modifier.background(MooiTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SpeechBubble(
                contentText = "비밀은 지켜드릴게요,\n당신의 감정을 편하게 나누어보세요.",
            )
            SpeechBubble(
                sizeParam = DpSize(286.dp, 83.dp),
                cornerRadius = 100.dp,
            ) {
                Column(modifier = Modifier) {
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Image(
                            modifier = Modifier.size(14.dp),
                            painter = painterResource(R.drawable.clock),
                            contentDescription = "",
                        )
                        Text(
                            text = "보관일 : 2025. 07. 30  08:40",
                            style = MooiTheme.typography.caption3,
                            color = MooiTheme.colorScheme.gray300,
                        )
                    }
                }
            }
        }
    }
}
