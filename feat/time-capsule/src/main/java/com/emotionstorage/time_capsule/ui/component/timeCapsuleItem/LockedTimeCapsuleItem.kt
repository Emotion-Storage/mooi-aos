package com.emotionstorage.time_capsule.ui.component.timeCapsuleItem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.emotionstorage.common.formatToKorTime
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme
import kotlin.math.absoluteValue

@Composable
fun LockedTimeCapsuleItem(
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
            Text(
                text = timeCapsule.createdAt.formatToKorTime(),
                style = MooiTheme.typography.caption4,
                color = MooiTheme.colorScheme.gray300,
            )
            Spacer(modifier = Modifier.padding(bottom = 6.dp))
        }
        // content
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(93.dp)
                    .background(
                        Color.Transparent,
                        RoundedCornerShape(15.dp),
                    ).clip(RoundedCornerShape(15.dp))
                    .clickable(onClick = onClick),
        ) {
            LockedOverLay(
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
private fun LockedOverLay(
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
                    color = MooiTheme.colorScheme.secondaryBlue700,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
