package com.emotionstorage.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// todo: adjust gradient offsets
@Immutable
data class MooiBrushScheme(
    val mainButtonBackground: Brush = Brush.linearGradient(
        listOf(
            Color(0xFFAFCBFA),
            Color(0xFF9BB4F2),
            Color(0xFF859CEA)
        ),
    ),
    val subButtonBackground: Brush = Brush.verticalGradient(
        listOf(
            Color(0x1A849BEA),
            Color(0x0D849BEA),
        )
    ),
    val subButtonBorder: Brush = Brush.horizontalGradient(
        listOf(
            Color(0x66AECBFA),
            Color(0x08AECBFA),
        )
    ),
    val commentBackground: Brush = Brush.verticalGradient(
        listOf(
            Color(0x80849BEA),
            Color(0x14849BEA)
        )
    ),
)


@Preview(showBackground = true)
@Composable
private fun BrushPreview() {
    MooiTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MooiTheme.colorScheme.background)
                .padding(50.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // main button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        MooiTheme.brushScheme.mainButtonBackground,
                        RoundedCornerShape(10.dp)
                    )
            )
            // sub button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        MooiTheme.brushScheme.subButtonBackground,
                        RoundedCornerShape(10.dp)
                    )
                    .border(
                        width = 1.dp,
                        brush = MooiTheme.brushScheme.subButtonBorder,
                        shape = RoundedCornerShape(10.dp)
                    )
            )
            // comment button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MooiTheme.brushScheme.commentBackground, RoundedCornerShape(10.dp))
            )
        }
    }
}