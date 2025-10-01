package com.emotionstorage.time_capsule_detail.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun DecorativeDots(
    modifier: Modifier = Modifier,
    dotSize: Int = 8,
    dotSpace: Int = 12,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dotSpace.dp),
    ) {
        for (alpha in listOf(0.1f, 0.3f, 0.7f)) {
            Box(
                modifier =
                    Modifier
                        .size(dotSize.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    MooiTheme.colorScheme.primary.copy(alpha = alpha),
                                    Color(0xFF9AB4F2).copy(alpha = alpha),
                                    MooiTheme.colorScheme.tertiary.copy(alpha = alpha),
                                ),
                            ),
                            CircleShape,
                        ),
            )
        }
    }
}
