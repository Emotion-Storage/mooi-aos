package com.emotionstorage.ui.util

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun Modifier.dropShadow(
    shape: Shape,
    color: Color = Color.Black.copy(0.25f),
    blur: Dp = 1.dp,
    offsetY: Dp = 1.dp,
    offsetX: Dp = 1.dp,
    spread: Dp = 1.dp,
) = composed {
    val density = LocalDensity.current

    val paint =
        remember(color, blur) {
            Paint().apply {
                this.color = color
                val blurPx = with(density) { blur.toPx() }
                if (blurPx > 0f) {
                    this.asFrameworkPaint().maskFilter =
                        BlurMaskFilter(blurPx, BlurMaskFilter.Blur.NORMAL)
                }
            }
        }

    drawBehind {
        val spreadPx = spread.toPx()
        val offsetXPx = offsetX.toPx()
        val offsetYPx = offsetY.toPx()

        val shadowWidth = size.width + spreadPx
        val shadowHeight = size.height + spreadPx

        if (shadowWidth <= 0f || shadowHeight <= 0f) return@drawBehind

        val shadowSize = Size(shadowWidth, shadowHeight)
        val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)

        drawIntoCanvas { canvas ->
            canvas.save()
            canvas.translate(offsetXPx, offsetYPx)
            canvas.drawOutline(shadowOutline, paint)
            canvas.restore()
        }
    }
}

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

@Composable
fun Modifier.errorRedBackground(
    isActivated: Boolean = false,
    shape: Shape = RoundedCornerShape(10.dp),
    defaultBackground: Color = Color.Transparent,
): Modifier =
    if (isActivated) {
        this
            .background(
                MooiTheme.brushScheme.errorRedButtonBackground,
                shape,
            ).border(
                1.dp,
                MooiTheme.brushScheme.errorRedButtonBorder,
                shape,
            )
    } else {
        this.background(defaultBackground, shape)
    }
