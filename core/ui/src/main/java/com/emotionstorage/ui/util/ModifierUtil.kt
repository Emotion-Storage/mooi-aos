package com.emotionstorage.ui.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun Modifier.mainBackground(
    isActivated: Boolean = false,
    shape: Shape = RoundedCornerShape(10.dp),
    defaultBackground: Color = Color.Transparent,
): Modifier =
    if (isActivated) {
        this.background(
            MooiTheme.brushScheme.mainButtonBackground,
            shape,
        )
    } else {
        this.background(defaultBackground, shape)
    }

@Composable
fun Modifier.subBackground(
    isActivated: Boolean = false,
    shape: Shape = RoundedCornerShape(10.dp),
    defaultBackground: Color = Color.Transparent,
): Modifier =
    if (isActivated) {
        this
            .background(
                MooiTheme.brushScheme.subButtonBackground,
                shape,
            ).border(
                1.dp,
                MooiTheme.brushScheme.subButtonBorder,
                shape,
            )
    } else {
        this.background(defaultBackground, shape)
    }
