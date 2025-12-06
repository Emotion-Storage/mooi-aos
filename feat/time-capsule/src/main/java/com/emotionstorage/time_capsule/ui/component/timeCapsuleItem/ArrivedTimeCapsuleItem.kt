package com.emotionstorage.time_capsule.ui.component.timeCapsuleItem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.emotionstorage.common.formatToKorTime
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.LinearGradient
import com.emotionstorage.ui.util.dropShadow
import kotlin.math.absoluteValue

@Composable
fun ArrivedTimeCapsuleItem(
    timeCapsule: TimeCapsuleItemState,
    modifier: Modifier = Modifier,
    showHeader: Boolean = true,
    onClick: () -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(Color.Transparent),
    ) {
        // header
        if (showHeader) {
            Column {
                Row(
                    modifier =
                        modifier
                            .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = timeCapsule.createdAt.formatToKorTime(),
                        style = MooiTheme.typography.caption4,
                        color = MooiTheme.colorScheme.gray300,
                    )
                }
                Row(
                    modifier = Modifier.height(31.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                ) {
                    Image(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(R.drawable.ic_key),
                        contentDescription = "",
                    )
                    Text(
                        text = "도착한 타임캡슐을 열어 내 지난 감정을 확인해요.",
                        style = MooiTheme.typography.caption6,
                        color = MooiTheme.colorScheme.gray400,
                    )
                }
            }
            Spacer(modifier = Modifier.padding(bottom = (5.5).dp))
        }
        // content
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .dropShadow(
                        shape = RoundedCornerShape(15.dp),
                        color = Color(0xFF849BEA).copy(alpha = 0.15f),
                        offsetX = 0.dp,
                        offsetY = 0.dp,
                        blur = 5.dp,
                        spread = 2.dp,
                    )
                    .height(93.dp)
                    .background(
                        Color.Transparent,
                        RoundedCornerShape(15.dp),
                    )
                    .clip(RoundedCornerShape(15.dp))
                    .clickable(onClick = onClick),
        ) {
            ArrivedOverLay(
                openDDay = timeCapsule.openDDay ?: 0,
                modifier = Modifier.fillMaxSize(),
            )

            // content
            TimeCapsuleItemContent(
                modifier = Modifier.fillMaxSize(),
                timeCapsule = timeCapsule,
                blurContent = true,
            )
        }
    }
}

@Composable
private fun ArrivedOverLay(
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
                )
                .border(
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
