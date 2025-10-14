package com.emotionstorage.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.util.LinearGradient

@Immutable
data class MooiBrushScheme(
    val mainButtonBackground: Brush =
        LinearGradient(
            colors =
                listOf(
                    Color(0xFFAECBFA),
                    Color(0xFF9AB4F2),
                    Color(0xFF849BEA),
                ),
            stops = listOf(0.0f, 0.4f, 1.0f),
            angleInDegrees = 93f,
            useAsCssAngle = true,
        ),
    val subButtonBackground: Brush =
        LinearGradient(
            colors =
                listOf(
                    Color(0x1A849BEA),
                    Color(0x0D4A5784),
                ),
            stops = listOf(0.0f, 1.0f),
            angleInDegrees = 153f,
            useAsCssAngle = true,
        ),
    val subButtonBorder: Brush =
        LinearGradient(
            colors =
                listOf(
                    Color(0x66AECBFA),
                    Color(0x08AECBFA),
                ),
            stops = listOf(0.0f, 1.0f),
        ),
    val commentBackground: Brush =
        LinearGradient(
            colors =
                listOf(
                    Color(0xFF849BEA).copy(alpha = 0.5f),
                    Color(0xFF849BEA).copy(alpha = 0.08f),
                ),
            stops = listOf(0.0f, 1.0f),
            angleInDegrees = 109f,
            useAsCssAngle = true,
        ),
    val errorRedButtonBackground: Brush =
        LinearGradient(
            colors =
                listOf(
                    Color(0x1AE86666),
                    Color(0x0DE86666),
                ),
            angleInDegrees = 154f,
            useAsCssAngle = true,
        ),
    val errorRedButtonBorder: Brush =
        LinearGradient(
            colors =
                listOf(
                    Color(0x66E86666),
                    Color(0x08E86666),
                ),
            angleInDegrees = 154f,
            useAsCssAngle = true,
        ),
)

@Preview(showBackground = true)
@Composable
private fun BrushPreview() {
    MooiTheme {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MooiTheme.colorScheme.background)
                    .padding(50.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // main button
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(
                            MooiTheme.brushScheme.mainButtonBackground,
                            RoundedCornerShape(10.dp),
                        ),
            )
            // sub button
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(
                            MooiTheme.brushScheme.subButtonBackground,
                            RoundedCornerShape(10.dp),
                        ).border(
                            width = 1.dp,
                            brush = MooiTheme.brushScheme.subButtonBorder,
                            shape = RoundedCornerShape(10.dp),
                        ),
            )
            // comment button
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .border(
                            width = 1.dp,
                            brush = MooiTheme.brushScheme.subButtonBorder,
                            shape = RoundedCornerShape(10.dp),
                        )
                        // set background brush opacity to 20%
                        .graphicsLayer(alpha = 0.2f)
                        .background(MooiTheme.brushScheme.commentBackground, RoundedCornerShape(10.dp)),
            )
            // errorRedButton
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(MooiTheme.brushScheme.errorRedButtonBackground, RoundedCornerShape(10.dp))
                        .border(
                            width = 1.dp,
                            brush = MooiTheme.brushScheme.errorRedButtonBorder,
                            shape = RoundedCornerShape(10.dp),
                        ),
            )
        }
    }
}
