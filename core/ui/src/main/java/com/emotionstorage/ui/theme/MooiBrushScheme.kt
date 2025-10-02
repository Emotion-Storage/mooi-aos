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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.util.LinearGradient

@Immutable
data class MooiBrushScheme(
    val mainButtonBackground: Brush =
        LinearGradient(
            colors =
                listOf(
                    Color(0xFFAFCBFA),
                    Color(0xFF9BB4F2),
                    Color(0xFF859CEA),
                ),
            stops = listOf(0.0f, 0.4f, 1.0f),
            angleInDegrees = -75f,
        ),
    val subButtonBackground: Brush =
        LinearGradient(
            colors =
                listOf(
                    Color(0x1A849BEA),
                    Color(0x0D849BEA),
                ),
            angleInDegrees = -9f,
        ),
    val subButtonBorder: Brush =
        LinearGradient(
            colors =
                listOf(
                    Color(0x66AECBFA),
                    Color(0x08AECBFA),
                ),
            angleInDegrees = -42f,
        ),
    val commentBackground: Brush =
        LinearGradient(
            colors =
                listOf(
                    Color(0x80849BEA),
                    Color(0x14849BEA),
                ),
            angleInDegrees = -42f,
        ),
    val errorRedButtonBackground: Brush =
        LinearGradient(
            colors =
                listOf(
                    Color(0xFFE86666).copy(alpha = 0.1f),
                    Color(0xFFE86666).copy(alpha = 0.05f),
                ),
            angleInDegrees = -11f,
        ),
    val errorRedButtonBorder: Brush =
        LinearGradient(
            colors =
                listOf(
                    Color(0xFFE86666).copy(alpha = 0.4f),
                    Color(0xFFE86666).copy(alpha = 0.03f),
                ),
            angleInDegrees = -17f,
        ),
    val chatMessageBackground: Brush =
        LinearGradient(
            colors =
                listOf(
                    Color(0xFF000000),
                    Color(0xFF26262C),
                ),
            stops = listOf(0.2f, 1.0f),
            angleInDegrees = -9f,
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
                        .background(MooiTheme.brushScheme.commentBackground, RoundedCornerShape(10.dp))
                        .border(
                            width = 1.dp,
                            brush = MooiTheme.brushScheme.subButtonBorder,
                            shape = RoundedCornerShape(10.dp),
                        ),
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
