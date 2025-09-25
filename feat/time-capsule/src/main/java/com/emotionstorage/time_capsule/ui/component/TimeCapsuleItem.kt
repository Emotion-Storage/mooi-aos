package com.emotionstorage.time_capsule.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.common.formatToKorDateTime
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.model.TimeCapsule.Emotion
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.RoundedToggleButton
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalDateTime

private val DUMMY_TIME_CAPSULE =  TimeCapsuleItemState(
    id = "",
    status = TimeCapsule.STATUS.OPENED,
    title = "오늘 아침에 친구를 만났는데, 친구가 늦었어..",
    emotions = listOf(
        Emotion(
            label = "서운함",
            icon = 0,
        ), Emotion(
            label = "화남",
            icon = 1,
        ), Emotion(
            label = "피곤함",
            icon = 2,
        )
    ),
    isFavorite = true,
    isFavoriteAt = LocalDateTime.now(),
    createdAt = LocalDateTime.now(),
    openDday = -99,
)

// todo: add ui per status
@Composable
fun TimeCapsuleItem(
    modifier: Modifier = Modifier,
    timeCapsule: TimeCapsuleItemState,
    onFavoriteClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = timeCapsule.createdAt.formatToKorDateTime(),
                style = MooiTheme.typography.body3.copy(fontWeight = FontWeight.Light),
                color = MooiTheme.colorScheme.gray300
            )
            RoundedToggleButton(
                isSelected = timeCapsule.isFavorite,
                onSelect = onFavoriteClick
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(92.dp)
                .background(
                    Color(0x1A849BEA), RoundedCornerShape(15.dp)
                )
                .padding(vertical = 18.dp, horizontal = 15.dp)
                .clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(11.dp)
        ) {
            // unlocked icon
            Column(
                modifier = Modifier
                    .size(54.dp)
                    .border(1.dp, Color(0xAECBFA).copy(alpha = 0.2f), CircleShape),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.lock_open),
                    modifier = Modifier
                        .width(11.dp)
                        .height(14.dp),
                    contentDescription = "open"
                )
                Text(
                    modifier = Modifier.padding(top = 3.dp),
                    text = "열림",
                    style = MooiTheme.typography.body3.copy(fontSize = 11.sp, lineHeight = 24.sp),
                    color = MooiTheme.colorScheme.secondary
                )
            }

            // time capsule content
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (emotion in timeCapsule.emotions) {
                        EmotionTag(emotion = emotion)
                    }
                }
                Text(
                    text = timeCapsule.title,
                    style = MooiTheme.typography.body4,
                    color = MooiTheme.colorScheme.primary,
                    maxLines = 1
                )
            }
        }
    }
}

@Preview
@Composable
private fun TimeCapsuleItemPreview(){
    MooiTheme {
        TimeCapsuleItem(
            timeCapsule = DUMMY_TIME_CAPSULE,
        )
    }
}
