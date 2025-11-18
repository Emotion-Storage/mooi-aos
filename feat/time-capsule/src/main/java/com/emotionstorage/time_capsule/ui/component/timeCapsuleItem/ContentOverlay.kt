package com.emotionstorage.time_capsule.ui.component.timeCapsuleItem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.LinearGradient
import kotlin.math.absoluteValue

@Composable
fun ContentOverlay(
    status: TimeCapsule.Status,
    openDDay: Int,
    modifier: Modifier = Modifier,
) {
    when (status) {
        TimeCapsule.Status.LOCKED -> {
            LockedContentOverLay(
                openDDay = openDDay,
                modifier = modifier,
            )
        }

        TimeCapsule.Status.ARRIVED -> {
            ArrivedContentOverLay(
                openDDay = openDDay,
                modifier = modifier,
            )
        }

        else -> {
            // no overlay
        }
    }
}

@Composable
private fun LockedContentOverLay(
    openDDay: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .zIndex(5f)
                .background(
                    Color(0xFF0E0C12).copy(alpha = 0.8f),
                    RoundedCornerShape(15.dp),
                ),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier.size(17.dp, 20.dp),
                painter = painterResource(id = R.drawable.ic_lock),
                contentDescription = "lock",
            )
            Box(
                modifier =
                    Modifier
                        .height(24.dp),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "(D-${openDDay.absoluteValue})",
                    style = MooiTheme.typography.caption1,
                    color = MooiTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun ArrivedContentOverLay(
    openDDay: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .zIndex(5f)
                .background(
                    Color(0xFF262736).copy(alpha = 0.85f),
                    RoundedCornerShape(15.dp),
                ).border(
                    1.dp,
                    LinearGradient(
                        colors =
                            listOf(
                                Color(0xFF849BEA).copy(alpha = 0.4f),
                                Color(0xFF849BEA).copy(alpha = 0.03f),
                            ),
                        angleInDegrees = -17f,
                    ),
                    RoundedCornerShape(15.dp),
                ),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier.size(17.dp, 20.dp),
                painter = painterResource(id = R.drawable.ic_lock),
                contentDescription = "arrived",
            )
            Box(
                modifier =
                    Modifier
                        .height(24.dp),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "도착한지 D+${openDDay.absoluteValue}",
                    style = MooiTheme.typography.caption1,
                    color = MooiTheme.colorScheme.secondary,
                )
            }
        }
    }
}
