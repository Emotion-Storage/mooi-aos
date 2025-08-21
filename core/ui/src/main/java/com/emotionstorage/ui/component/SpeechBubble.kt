import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.emotionstorage.ui.theme.MooiTheme

const val SPEECH_BUBBLE_Z_INDEX = 10f

@Composable
fun SpeechBubble(
    text: String,
    modifier: Modifier = Modifier,
    cornerRadius: Float = 10f,
    tailWidth: Float = 16f,
    tailHeight: Float = 14f,
    gradientColors: List<Color> = listOf(Color(0x80849BEA), Color(0x14849BEA))
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .zIndex(SPEECH_BUBBLE_Z_INDEX)
    ) {
        Canvas(
            modifier = Modifier
                .matchParentSize()
        ) {
            val rectBottom = size.height - tailHeight

            val path = Path().apply {
                addRoundRect(
                    RoundRect(
                        left = 0f,
                        top = 0f,
                        right = size.width,
                        bottom = rectBottom,
                        cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                    )
                )
                moveTo(size.width / 2 - tailWidth / 2, rectBottom)
                lineTo(size.width / 2, size.height)
                lineTo(size.width / 2 + tailWidth / 2, rectBottom)
                close()
            }

            // todo: fix gradient offset
            drawPath(
                path = path,
                brush = Brush.verticalGradient(
                    colors = gradientColors,
                    startY = 0f,
                    endY = size.height
                )
            )
        }

        Box(
            modifier = Modifier
                .padding(bottom = tailHeight.dp)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .zIndex(SPEECH_BUBBLE_Z_INDEX + 1),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun SpeechBubblePreview() {
    MooiTheme {
        Box(modifier = Modifier
            .padding(50.dp)
            .background(MooiTheme.colorScheme.background))
        SpeechBubble(
            text = "비밀은 지켜드릴게요,\n당신의 감정을 편하게 나누어보세요.",
            modifier = Modifier.size(height = 44.dp, width = 265.dp),
        )
    }
}