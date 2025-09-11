package com.emotionstorage.time_capsule.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.common.formatToKorDateTime
import com.emotionstorage.domain.model.TimeCapsule.Emotion
import com.emotionstorage.time_capsule.ui.component.EmotionTag
import com.emotionstorage.time_capsule.ui.model.FavoriteTimeCapsule
import com.emotionstorage.ui.component.RoundedToggleButton
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.DropDownPicker
import java.time.LocalDateTime


private val DUMMY_FAVORITES = (1..15).toList().map { it ->
    FavoriteTimeCapsule(
        id = it.toString(),
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
        createdAt = LocalDateTime.now()
    )
}

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    navToTimeCapsuleDetail: (id: String) -> Unit = {},
    navToBack: () -> Unit = {}
) {
    StatelessFavoriteScreen(
        favoriteTimeCapsules = DUMMY_FAVORITES,
        modifier = modifier,
        navToTimeCapsuleDetail = navToTimeCapsuleDetail,
        navToBack = navToBack
    )
}

@Composable
private fun StatelessFavoriteScreen(
    modifier: Modifier = Modifier,
    favoriteTimeCapsules: List<FavoriteTimeCapsule> = emptyList(),
    navToTimeCapsuleDetail: (id: String) -> Unit = {},
    navToBack: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MooiTheme.colorScheme.background),
        topBar = {
            TopAppBar(title = "내 마음 서랍", showBackButton = true, onBackClick = navToBack)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // info text
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 13.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.favorite_filled),
                    modifier = Modifier
                        .width(11.dp)
                        .height(12.dp),
                    contentDescription = "open",
                    colorFilter = ColorFilter.tint(MooiTheme.colorScheme.gray500)
                )
                Text(
                    text = "오래 기억하고싶은 타임캡슐을 즐겨찾기 해보세요.\n최대 30개까지 저장할 수 있습니다.",
                    style = MooiTheme.typography.body5,
                    color = MooiTheme.colorScheme.gray500
                )
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                DropDownPicker(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .width(102.dp)
                        .padding(top = 7.dp, bottom = 22.dp),
                    selectedValue = "최신 날짜순",
                    options = listOf("최신 날짜순", "즐겨찾기순"),
                    onSelect = {
                        // todo: change list sort order
                    }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .scrollable(scrollState, orientation = Orientation.Vertical)
            ) {
                items(items = favoriteTimeCapsules, key = { it.id }) {
                    FavoriteItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 17.dp),
                        item = it,
                        onClick = { navToTimeCapsuleDetail(it.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoriteItem(
    modifier: Modifier = Modifier,
    item: FavoriteTimeCapsule,
    onFavoriteClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MooiTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.createdAt.formatToKorDateTime(),
                style = MooiTheme.typography.body3.copy(fontWeight = FontWeight.Light),
                color = MooiTheme.colorScheme.gray300
            )
            RoundedToggleButton(
                isSelected = item.isFavorite,
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
                    for (emotion in item.emotions) {
                        EmotionTag(emotion = emotion)
                    }
                }
                Text(
                    text = item.title,
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
private fun FavoriteScreenPreview() {
    MooiTheme {
        StatelessFavoriteScreen(
            favoriteTimeCapsules = DUMMY_FAVORITES
        )
    }
}