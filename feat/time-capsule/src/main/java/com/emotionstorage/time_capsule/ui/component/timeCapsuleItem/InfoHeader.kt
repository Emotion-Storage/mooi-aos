package com.emotionstorage.time_capsule.ui.component.timeCapsuleItem

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.emotionstorage.common.formatToKorDateTime
import com.emotionstorage.common.formatToKorTime
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.CountDownTimer
import com.emotionstorage.ui.component.button.RoundedToggleButton
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalDateTime

@Composable
fun InfoHeader(
    status: TimeCapsule.Status,
    createdAt: LocalDateTime,
    modifier: Modifier = Modifier,
    expireAt: LocalDateTime? = null,
    showDate: Boolean = false,
    showInfoText: Boolean = true,
    showFavorite: Boolean = false,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {},
) {
    when (status) {
        TimeCapsule.Status.TEMPORARY -> {
            // timer
            if (expireAt != null) {
                Row(
                    modifier = modifier.padding(bottom = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                ) {
                    Image(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(R.drawable.ic_caution),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(MooiTheme.colorScheme.errorRed),
                    )
                    CountDownTimer(
                        deadline = expireAt,
                        optimizeMinuteTick = true,
                        optimizeSecondTick = true,
                    ) { hours, minutes, _ ->
                        Text(
                            text =
                                "임시저장 보관기간이 " +
                                    (if (hours >= 1) "${hours}시간 " else "${minutes}분 ") +
                                    "남았어요.",
                            style = MooiTheme.typography.caption6,
                            color = MooiTheme.colorScheme.errorRed,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.size(4.dp))
        }

        TimeCapsule.Status.LOCKED -> {
            // createdAt
            Text(
                modifier = modifier.padding(bottom = 6.dp),
                text = if (showDate) createdAt.formatToKorDateTime() else createdAt.formatToKorTime(),
                style = MooiTheme.typography.caption4,
                color = MooiTheme.colorScheme.gray300,
            )
        }

        TimeCapsule.Status.ARRIVED -> {
            // createdAt & (favorite button) & open info text
            Column(
                verticalArrangement = Arrangement.spacedBy((10.5).dp),
            ) {
                Row(
                    modifier =
                        modifier
                            .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = if (showDate) createdAt.formatToKorDateTime() else createdAt.formatToKorTime(),
                        style = MooiTheme.typography.caption4,
                        color = MooiTheme.colorScheme.gray300,
                    )
                    if (showFavorite) {
                        RoundedToggleButton(
                            modifier = Modifier.size(36.dp),
                            isSelected = isFavorite,
                            onSelect = onFavoriteClick,
                        )
                    }
                }
                if (showInfoText) {
                    Row(
                        modifier = modifier.padding(bottom = (10.5).dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(7.dp),
                    ) {
                        Image(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(R.drawable.ic_key),
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(MooiTheme.colorScheme.gray600),
                        )
                        Text(
                            text = "도착한 타임캡슐을 열어 내 지난 감정을 확인해요.",
                            style = MooiTheme.typography.caption6,
                            color = MooiTheme.colorScheme.gray400,
                        )
                    }
                }
            }
        }

        TimeCapsule.Status.OPENED -> {
            // createdAt & favorite button
            Row(
                modifier =
                    modifier
                        .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = if (showDate) createdAt.formatToKorDateTime() else createdAt.formatToKorTime(),
                    style = MooiTheme.typography.caption4,
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
