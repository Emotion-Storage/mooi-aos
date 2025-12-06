package com.emotionstorage.time_capsule.ui.component.timeCapsuleItem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.emotionstorage.common.formatToKorTime
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.button.RoundedToggleButton
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.dropShadow


@Composable
fun OpenTimeCapsuleItem(
    timeCapsule: TimeCapsuleItemState,
    modifier: Modifier = Modifier,
    showHeader: Boolean = true,
    onClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(Color.Transparent),
    ) {
        // header
        if (showHeader) {
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
                RoundedToggleButton(
                    modifier = Modifier.size(36.dp),
                    isSelected = timeCapsule.isFavorite,
                    onSelect = onFavoriteClick,
                )
            }
            Spacer(modifier = Modifier.padding(bottom = 10.dp))
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
                    )
                    .clip(RoundedCornerShape(15.dp))
                    .clickable(onClick = onClick),
        ) {
            TimeCapsuleItemContent(
                modifier = Modifier.fillMaxSize(),
                timeCapsule = timeCapsule,
                blurContent = false
            )
        }
    }
}

