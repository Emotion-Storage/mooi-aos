
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
fun SpeechBubble(
    text: String,
    modifier: Modifier = Modifier.size(265.dp, 84.dp),
) {
    val context = LocalContext.current

    val cornerRadius = dpToPixel(context, 10f)
    val tailWidth = dpToPixel(context, 14f)
    val tailHeight = dpToPixel(context, 12f)
    val bgBrush = MooiTheme.brushScheme.commentBackground

    Box(
        modifier = modifier,
    ) {
        Canvas(
            modifier =
                Modifier
                    .matchParentSize(),
        ) {
            val rectBottom = size.height - tailHeight

            val path =
                Path().apply {
                    addRoundRect(
                        RoundRect(
                            left = 0f,
                            top = 0f,
                            right = size.width,
                            bottom = rectBottom,
                            cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                        ),
                    )
                    moveTo(size.width / 2 - tailWidth / 2, rectBottom)
                    lineTo(size.width / 2, size.height)
                    lineTo(size.width / 2 + tailWidth / 2, rectBottom)
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
                    .offset(y = 15.dp),
            text = text,
            style = MooiTheme.typography.body4.copy(lineHeight = 20.sp),
            color = MooiTheme.colorScheme.gray300,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun SpeechBubblePreview() {
    MooiTheme {
        Box(
            modifier =
                Modifier
                    .padding(50.dp)
                    .background(MooiTheme.colorScheme.background),
        ) {
            SpeechBubble(
                text = "비밀은 지켜드릴게요,\n당신의 감정을 편하게 나누어보세요.",
            )
        }
    }
}
