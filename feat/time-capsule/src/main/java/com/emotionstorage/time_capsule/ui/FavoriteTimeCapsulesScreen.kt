package com.emotionstorage.time_capsule.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.model.TimeCapsule.Emotion
import com.emotionstorage.domain.repo.FavoriteSortBy
import com.emotionstorage.time_capsule.presentation.FavoriteTimeCapsulesAction
import com.emotionstorage.time_capsule.presentation.FavoriteTimeCapsulesSideEffect.ShowToast
import com.emotionstorage.time_capsule.presentation.FavoriteTimeCapsulesSideEffect.ShowToast.FavoriteToast
import com.emotionstorage.time_capsule.presentation.FavoriteTimeCapsulesState
import com.emotionstorage.time_capsule.presentation.FavoriteTimeCapsulesViewModel
import com.emotionstorage.time_capsule.ui.component.TimeCapsuleItem
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.AppSnackbarHost
import com.emotionstorage.ui.component.DropDownPicker
import com.emotionstorage.ui.component.Toast
import java.time.LocalDateTime

private val DUMMY_TIME_CAPSULES =
    (1..15).toList().map { it ->
        TimeCapsuleItemState(
            id = it.toString(),
            status = TimeCapsule.STATUS.OPENED,
            title = "오늘 아침에 친구를 만났는데, 친구가 늦었어..",
            emotions =
                listOf(
                    Emotion(
                        emoji = "\uD83D\uDE14",
                        label = "서운함",
                        percentage = 30.0f,
                    ), Emotion(
                        emoji = "\uD83D\uDE0A",
                        label = "고마움",
                        percentage = 30.0f,
                    ),
                    Emotion(
                        emoji = "\uD83E\uDD70",
                        label = "안정감",
                        percentage = 80.0f,
                    ),
                ),
            isFavorite = true,
            isFavoriteAt = LocalDateTime.now(),
            createdAt = LocalDateTime.now(),
            expireAt = LocalDateTime.now().plusHours(5),
        )
    }

@Composable
fun FavoriteTimeCapsulesScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoriteTimeCapsulesViewModel = hiltViewModel(),
    navToTimeCapsuleDetail: (id: String) -> Unit = {},
    navToBack: () -> Unit = {},
) {
    val state = viewModel.container.stateFlow.collectAsState()
    LaunchedEffect(Unit) {
        // initial load, triggered on launch
        viewModel.onAction(FavoriteTimeCapsulesAction.Init)
    }

    val snackState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is ShowToast -> {
                    // dismiss current snackbar if exists
                    snackState.currentSnackbarData?.dismiss()
                    // show new snackbar
                    snackState.showSnackbar(sideEffect.toast.message)
                }
            }
        }
    }

    StatelessFavoriteTimeCapsulesScreen(
        modifier = modifier.fillMaxSize(),
        snackState = snackState,
        state = state.value,
        onAction = viewModel::onAction,
        navToTimeCapsuleDetail = navToTimeCapsuleDetail,
        navToBack = navToBack,
    )
}

@Composable
private fun StatelessFavoriteTimeCapsulesScreen(
    modifier: Modifier = Modifier,
    snackState: SnackbarHostState = SnackbarHostState(),
    state: FavoriteTimeCapsulesState = FavoriteTimeCapsulesState(),
    onAction: (FavoriteTimeCapsulesAction) -> Unit = {},
    navToTimeCapsuleDetail: (id: String) -> Unit = {},
    navToBack: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier =
            modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background),
        topBar = {
            TopAppBar(title = "내 마음 서랍", showBackButton = true, onBackClick = navToBack)
        },
        snackbarHost = {
            AppSnackbarHost(hostState = snackState) { snackbarData ->
                Toast(
                    message = snackbarData.visuals.message,
                    iconId =
                        if (snackbarData.visuals.message ==
                            FavoriteToast.FAVORITE_FULL.message
                        ) {
                            R.drawable.success_filled
                        } else {
                            null
                        },
                )
            }
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background)
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
        ) {
            if (state.timeCapsules.isEmpty()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "아직 즐겨찾기한 타임캡슐이 없어요.",
                    style = MooiTheme.typography.caption2,
                    color = MooiTheme.colorScheme.gray400,
                )
            }
            Column(
                modifier =
                    Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth(),
            ) {
                // info text
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 13.dp)
                            .offset(x = -1.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.favorite_filled),
                        modifier =
                            Modifier
                                .width(11.dp)
                                .height(12.dp),
                        contentDescription = "open",
                        colorFilter = ColorFilter.tint(MooiTheme.colorScheme.gray500),
                    )
                    Text(
                        text = "오래 기억하고싶은 타임캡슐을 즐겨찾기 해보세요.\n최대 30개까지 저장할 수 있습니다.",
                        style = MooiTheme.typography.caption7.copy(lineHeight = 22.sp),
                        color = MooiTheme.colorScheme.gray500,
                    )
                }

                if (!state.timeCapsules.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        DropDownPicker(
                            modifier =
                                Modifier
                                    .align(Alignment.CenterEnd)
                                    .width(102.dp)
                                    .padding(top = 7.dp, bottom = 22.dp),
                            selectedValue = state.sortOrder.label,
                            options = FavoriteSortBy.entries.map { it.label },
                            onSelect = { label ->
                                onAction(
                                    FavoriteTimeCapsulesAction.SetSortOrder(label),
                                )
                            },
                        )
                    }
                }

                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .scrollable(scrollState, orientation = Orientation.Vertical),
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                ) {
                    items(items = state.timeCapsules, key = { it.id }) {
                        TimeCapsuleItem(
                            modifier = Modifier.fillMaxWidth(),
                            timeCapsule = it,
                            showDate = true,
                            onClick = { navToTimeCapsuleDetail(it.id) },
                            onFavoriteClick = {
                                onAction(
                                    FavoriteTimeCapsulesAction.ToggleFavorite(it.id),
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun FavoriteTimeCapsulesScreenPreview() {
    MooiTheme {
        StatelessFavoriteTimeCapsulesScreen(
            state = FavoriteTimeCapsulesState(timeCapsules = DUMMY_TIME_CAPSULES),
        )
    }
}

@Preview
@Composable
private fun EmptyFavoriteTimeCapsulesScreenPreview() {
    MooiTheme {
        StatelessFavoriteTimeCapsulesScreen(
            state = FavoriteTimeCapsulesState(timeCapsules = emptyList()),
        )
    }
}
