package com.emotionstorage.time_capsule.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.emotionstorage.common.formatToKorDateTime
import com.emotionstorage.common.formatToKorTime
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.model.TimeCapsule.Emotion
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.RoundedToggleButton
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.LinearGradient
import com.emotionstorage.ui.util.errorRedBackground
import java.time.LocalDateTime
import kotlin.math.absoluteValue

private object TimeCapsuleItemDesignToken {
    val contentHeight = 93.dp
    val contentPadding = PaddingValues(top = 18.dp, bottom = 20.dp, start = 15.dp, end = 9.dp)
}

@Composable
fun TimeCapsuleItem(
    modifier: Modifier = Modifier,
    timeCapsule: TimeCapsuleItemState,
    showDate: Boolean = false,
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
        TimeCapsuleItemInfo(
            modifier = Modifier.fillMaxWidth(),
            status = timeCapsule.status,
            createdAt = timeCapsule.createdAt,
            showDate = showDate,
            isFavorite = timeCapsule.isFavorite,
            onFavoriteClick = onFavoriteClick,
        )

        // content box
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(TimeCapsuleItemDesignToken.contentHeight)
                    .background(
                        Color.Transparent,
                        RoundedCornerShape(15.dp),
                    )
                    .clickable(onClick = onClick),
        ) {
            // content
            if (timeCapsule.status == TimeCapsule.STATUS.TEMPORARY) {
                TemporaryContent(
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                if (timeCapsule.status == TimeCapsule.STATUS.LOCKED) {
                    LockedContentOverLay(
                        modifier = Modifier.fillMaxSize(),
                        openDDay = timeCapsule.openDDay ?: 0,
                    )
                }
                if (timeCapsule.status == TimeCapsule.STATUS.ARRIVED) {
                    ArrivedContentOverLay(
                        modifier = Modifier.fillMaxSize(),
                        openDDay = timeCapsule.openDDay ?: 0,
                    )
                }

                TimeCapsuleContent(
                    modifier = Modifier.fillMaxSize(),
                    timeCapsule = timeCapsule,
                )
            }
        }
    }
}

@Composable
private fun TemporaryContent(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .errorRedBackground(
                    true, RoundedCornerShape(15.dp),
                ),
    ) {
        // 아직 보관하지 않은 타임 캡슐이 있어요
    }
}

@Composable
private fun LockedContentOverLay(
    openDDay: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .zIndex(5f)
            .background(
                Color(0xFF0E0C12).copy(alpha = 0.8f),
                RoundedCornerShape(15.dp),
            ),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(17.dp, 20.dp),
                painter = painterResource(id = R.drawable.lock),
                contentDescription = "lock",
            )
            Text(
                text = "(D-${openDDay.absoluteValue})",
                style = MooiTheme.typography.body4.copy(fontWeight = FontWeight.SemiBold, lineHeight = 24.sp),
                color = MooiTheme.colorScheme.secondary
            )
        }
    }
}


@Composable
private fun ArrivedContentOverLay(
    openDDay: Int,
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier
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
            ), // todo: add drop shadow
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(17.dp, 20.dp),
                painter = painterResource(id = R.drawable.lock),
                contentDescription = "arrived",
            )
            Text(
                text = "도착한지 D+${openDDay}",
                style = MooiTheme.typography.body4.copy(fontWeight = FontWeight.SemiBold, lineHeight = 24.sp),
                color = MooiTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
private fun TimeCapsuleContent(
    modifier: Modifier = Modifier,
    timeCapsule: TimeCapsuleItemState,
) {
    Row(
        modifier =
            modifier
                .fillMaxSize()
                .background(
                    Color(0x1A849BEA),
                    RoundedCornerShape(15.dp),
                )
                .run {
                    // blur content if not opened
                    if (timeCapsule.status == TimeCapsule.STATUS.LOCKED || timeCapsule.status == TimeCapsule.STATUS.ARRIVED) {
                        this.blur(4.dp)
                    } else {
                        this
                    }
                }
                .padding(TimeCapsuleItemDesignToken.contentPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(11.dp),
    ) {
        // unlocked icon
        if (timeCapsule.status == TimeCapsule.STATUS.OPENED) {
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
                    style = MooiTheme.typography.body3.copy(fontSize = 11.sp, lineHeight = 24.sp),
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
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                for (emotion in timeCapsule.emotions) {
                    EmotionTag(emotion = emotion)
                }
            }
            Text(
                text = timeCapsule.title,
                style = MooiTheme.typography.body4,
                color = MooiTheme.colorScheme.primary,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun TimeCapsuleItemInfo(
    status: TimeCapsule.STATUS,
    createdAt: LocalDateTime,
    modifier: Modifier = Modifier,
    showDate: Boolean = false,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {},
) {
    when (status) {
        TimeCapsule.STATUS.TEMPORARY -> {
            // timer
            Row(
                modifier = modifier.padding(bottom = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(7.dp),
            ) {
                Image(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(R.drawable.caution),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(MooiTheme.colorScheme.errorRed)
                )
                Text(
                    text = "임시저장 보관기간이 N시간 남았어요.", // todo: set timer
                    style = MooiTheme.typography.body5.copy(fontWeight = FontWeight.Normal),
                    color = MooiTheme.colorScheme.errorRed,
                )
            }
        }

        TimeCapsule.STATUS.LOCKED -> {
            // createdAt
            Text(
                modifier = modifier.padding(bottom = 6.dp),
                text = if (showDate) createdAt.formatToKorDateTime() else createdAt.formatToKorTime(),
                style = MooiTheme.typography.body4.copy(fontWeight = FontWeight.Light),
                color = MooiTheme.colorScheme.gray300,
            )
        }

        TimeCapsule.STATUS.ARRIVED -> {
            // createdAt & open info text
            Column(
                modifier = modifier.padding(bottom = (10.5).dp),
                verticalArrangement = Arrangement.spacedBy((10.5).dp),
            ) {
                Text(
                    text = if (showDate) createdAt.formatToKorDateTime() else createdAt.formatToKorTime(),
                    style = MooiTheme.typography.body4.copy(fontWeight = FontWeight.Light),
                    color = MooiTheme.colorScheme.gray300,
                )
                Row(
                    modifier = modifier.padding(bottom = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                ) {
                    Image(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(R.drawable.key),
                        contentDescription = "",
                    )
                    Text(
                        text = "도착한 타임캡슐을 열어 내 지난 감정을 확인해요.",
                        style = MooiTheme.typography.body5.copy(fontWeight = FontWeight.Medium),
                        color = MooiTheme.colorScheme.gray400,
                    )
                }
            }
        }

        TimeCapsule.STATUS.OPENED -> {
            // createdAt & favorite button
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = if (showDate) createdAt.formatToKorDateTime() else createdAt.formatToKorTime(),
                    style = MooiTheme.typography.body4.copy(fontWeight = FontWeight.Light),
                    color = MooiTheme.colorScheme.gray300,
                )
                RoundedToggleButton(
                    modifier = Modifier.size(36.dp),
                    isSelected = isFavorite,
                    onSelect = onFavoriteClick,
                )
            }
        }
    }
}


@Preview
@Composable
private fun TimeCapsuleItemPreview() {
    val dummyTimeCapsule =
        TimeCapsuleItemState(
            id = "",
            status = TimeCapsule.STATUS.OPENED,
            title = "오늘 아침에 친구를 만났는데, 친구가 늦었어..",
            emotions =
                listOf(
                    Emotion(
                        label = "서운함",
                        icon = 0,
                    ),
                    Emotion(
                        label = "화남",
                        icon = 1,
                    ),
                    Emotion(
                        label = "피곤함",
                        icon = 2,
                    ),
                ),
            isFavorite = true,
            isFavoriteAt = LocalDateTime.now(),
            createdAt = LocalDateTime.now(),
            openDDay = -99,
        )

    MooiTheme {
        Column(
            modifier = Modifier
                .background(MooiTheme.colorScheme.background)
                .padding(vertical = 20.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(17.dp)
        ) {
            TimeCapsuleItem(
                timeCapsule = dummyTimeCapsule.copy(
                    status = TimeCapsule.STATUS.TEMPORARY,
                    createdAt = LocalDateTime.now().minusHours(2),
                ),
            )
            TimeCapsuleItem(
                timeCapsule = dummyTimeCapsule.copy(
                    status = TimeCapsule.STATUS.LOCKED,
                    openDDay = -20,
                ),
            )
            TimeCapsuleItem(
                timeCapsule = dummyTimeCapsule.copy(
                    status = TimeCapsule.STATUS.ARRIVED,
                    openDDay = 20,
                ),
            )
            TimeCapsuleItem(
                timeCapsule = dummyTimeCapsule.copy(
                    status = TimeCapsule.STATUS.OPENED,
                    openDDay = 20,
                ),
            )
        }
    }
}
