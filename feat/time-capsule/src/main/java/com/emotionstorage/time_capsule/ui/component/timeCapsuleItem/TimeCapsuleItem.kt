package com.emotionstorage.time_capsule.ui.component.timeCapsuleItem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.time_capsule.ui.util.TimeCapsuleItemStateProvider
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.dropShadow

private object TimeCapsuleItemDesignToken {
    val contentHeight = 93.dp
    val contentPadding = PaddingValues(top = 18.dp, bottom = 20.dp, start = 15.dp, end = 9.dp)
}

@Composable
fun TimeCapsuleItem(
    modifier: Modifier = Modifier,
    timeCapsule: TimeCapsuleItemState,
    showDate: Boolean = false,
    showInfoText: Boolean = true,
    showFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {},
    onClick: () -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(Color.Transparent),
    ) {
        // information row
        InfoHeader(
            modifier = Modifier.fillMaxWidth(),
            status = timeCapsule.status,
            createdAt = timeCapsule.createdAt,
            expireAt = timeCapsule.expireAt,
            showDate = showDate,
            showInfoText = showInfoText,
            showFavorite = showFavorite,
            isFavorite = timeCapsule.isFavorite,
            onFavoriteClick = onFavoriteClick,
        )
        // content
        if (timeCapsule.status == TimeCapsule.Status.TEMPORARY) {
            TempContent(
                modifier = Modifier.fillMaxWidth(),
                onClick = onClick,
            )
        } else {
            // content box
            Spacer(modifier = Modifier.size(10.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .run {
                            if (timeCapsule.status == TimeCapsule.Status.ARRIVED) {
                                this.dropShadow(
                                    shape = RoundedCornerShape(15.dp),
                                    color = Color(0xFF849BEA).copy(alpha = 0.15f),
                                    offsetX = 0.dp,
                                    offsetY = 0.dp,
                                    blur = 5.dp,
                                    spread = 2.dp,
                                )
                            } else {
                                this
                            }
                        }.height(TimeCapsuleItemDesignToken.contentHeight)
                        .background(
                            Color.Transparent,
                            RoundedCornerShape(15.dp),
                        ).clip(RoundedCornerShape(15.dp))
                        .clickable(onClick = onClick),
            ) {
                // overlay
                ContentOverlay(
                    status = timeCapsule.status,
                    openDDay = timeCapsule.openDDay ?: 0,
                    modifier = Modifier.fillMaxSize(),
                )

                // content
                TimeCapsuleContent(
                    modifier = Modifier.fillMaxSize(),
                    timeCapsule = timeCapsule,
                )
            }
        }
    }
}

@Composable
private fun TimeCapsuleContent(
    modifier: Modifier = Modifier,
    timeCapsule: TimeCapsuleItemState,
) {
    val blurContent =
        timeCapsule.status == TimeCapsule.Status.LOCKED || timeCapsule.status == TimeCapsule.Status.ARRIVED
    Row(
        modifier =
            modifier
                .fillMaxSize()
                .background(
                    Color(0x1A849BEA),
                    RoundedCornerShape(15.dp),
                ).run {
                    // blur content if not opened
                    if (blurContent) {
                        this.blur(4.dp)
                    } else {
                        this
                    }
                }.padding(TimeCapsuleItemDesignToken.contentPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(11.dp),
    ) {
        // unlocked icon
        if (timeCapsule.status == TimeCapsule.Status.OPENED) {
            Column(
                modifier =
                    Modifier
                        .size(54.dp)
                        .border(1.dp, Color(0xFFAECBFA).copy(alpha = 0.2f), CircleShape),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.lock_open),
                    modifier =
                        Modifier
                            .width(11.dp)
                            .height(14.dp),
                    contentDescription = "open",
                )
                Text(
                    modifier = Modifier.padding(top = 3.dp),
                    text = "열림",
                    style = MooiTheme.typography.caption6.copy(fontSize = 11.sp),
                    color = MooiTheme.colorScheme.secondary,
                )
            }
        }

        // time capsule content
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                for (emotion in timeCapsule.emotions) {
                    EmotionTag(emotion = emotion)
                }
            }
            Text(
                text = timeCapsule.title,
                style = MooiTheme.typography.caption2,
                color = MooiTheme.colorScheme.primary,
                maxLines = 1,
            )
        }
    }
}

@Preview
@Composable
private fun TimeCapsuleItemPreview(
    @PreviewParameter(TimeCapsuleItemStateProvider::class) timeCapsule: TimeCapsuleItemState,
) {
    MooiTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MooiTheme.colorScheme.background)
                    .padding(16.dp),
        ) {
            TimeCapsuleItem(
                modifier = Modifier.align(Alignment.Center),
                timeCapsule = timeCapsule,
            )
        }
    }
}
