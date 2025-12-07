package com.emotionstorage.time_capsule.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.emotionstorage.common.formatToKorDateTime
import com.emotionstorage.domain.repo.FavoriteSortBy
import com.emotionstorage.time_capsule.presentation.FavoriteTimeCapsulesAction
import com.emotionstorage.time_capsule.presentation.FavoriteTimeCapsulesState
import com.emotionstorage.time_capsule.presentation.FavoriteTimeCapsulesViewModel
import com.emotionstorage.time_capsule.presentation.ToggleFavoriteAction
import com.emotionstorage.time_capsule.presentation.ToggleFavoriteSideEffect.ShowFavoriteFailToast
import com.emotionstorage.time_capsule.presentation.ToggleFavoriteSideEffect.ShowFavoriteSuccessToast
import com.emotionstorage.time_capsule.presentation.ToggleFavoriteState
import com.emotionstorage.time_capsule.presentation.ToggleFavoriteViewModel
import com.emotionstorage.time_capsule.ui.component.timeCapsuleItem.TimeCapsuleItem
import com.emotionstorage.ui.component.appBar.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.button.RoundedToggleButton
import com.emotionstorage.ui.component.loading.LoadingDots
import com.emotionstorage.ui.component.toast.AppSnackbarHost
import com.emotionstorage.ui.component.picker.DropDownPicker
import com.emotionstorage.ui.component.toast.AppSnackbarController

@Composable
fun FavoriteTimeCapsulesScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoriteTimeCapsulesViewModel = hiltViewModel(),
    favoriteViewModel: ToggleFavoriteViewModel = hiltViewModel(),
    navToTimeCapsuleDetail: (id: Long) -> Unit = {},
    navToBack: () -> Unit = {},
) {
    val context = LocalContext.current

    val state = viewModel.container.stateFlow.collectAsState()
    val timeCapsulesState = state.value.timeCapsulesFlow?.collectAsLazyPagingItems()
    val favoriteState = favoriteViewModel.container.stateFlow.collectAsState()

    val snackState = remember { SnackbarHostState() }
    val snackbarController = remember { AppSnackbarController(snackState) }

    // init state
    LaunchedEffect(Unit) {
        // initial load, triggered on launch
        viewModel.onAction(FavoriteTimeCapsulesAction.Init)
    }

    // init favorite state
    LaunchedEffect(timeCapsulesState?.itemSnapshotList) {
        val favorites =
            timeCapsulesState
                ?.itemSnapshotList
                ?.items
                ?.filter { it.isFavorite }
                ?.map { it.id } ?: emptyList()

        favoriteViewModel.onAction(
            ToggleFavoriteAction.Init(favorites),
        )
    }

    // collect favorite side effect
    LaunchedEffect("init") {
        favoriteViewModel.container.sideEffectFlow.collect {
            when (it) {
                is ShowFavoriteSuccessToast -> {
                    snackState.currentSnackbarData?.dismiss()
                    snackbarController.showSnackbar(
                        message =
                            if (it.isFavorite) {
                                context.getString(R.string.toast_favorite_added)
                            } else {
                                context.getString(R.string.toast_favorite_removed)
                            },
                        iconResId = R.drawable.ic_success_filled,
                    )
                }

                is ShowFavoriteFailToast -> {
                    snackState.currentSnackbarData?.dismiss()
                    snackbarController.showSnackbar(
                        message = context.getString(R.string.toast_favorite_full),
                    )
                }
            }
        }
    }

    StatelessFavoriteTimeCapsulesScreen(
        modifier = modifier.fillMaxSize(),
        snackState = snackState,
        snackbarController = snackbarController,
        state = state.value,
        favoriteState = favoriteState.value,
        onAction = viewModel::onAction,
        onFavoriteAction = favoriteViewModel::onAction,
        navToTimeCapsuleDetail = navToTimeCapsuleDetail,
        navToBack = navToBack,
    )
}

@Composable
private fun StatelessFavoriteTimeCapsulesScreen(
    modifier: Modifier = Modifier,
    snackState: SnackbarHostState = SnackbarHostState(),
    snackbarController: AppSnackbarController? = null,
    state: FavoriteTimeCapsulesState = FavoriteTimeCapsulesState(),
    favoriteState: ToggleFavoriteState = ToggleFavoriteState(),
    onAction: (FavoriteTimeCapsulesAction) -> Unit = {},
    onFavoriteAction: (ToggleFavoriteAction) -> Unit = {},
    navToTimeCapsuleDetail: (id: Long) -> Unit = {},
    navToBack: () -> Unit = {},
) {
    val timeCapsules = state.timeCapsulesFlow?.collectAsLazyPagingItems()

    Scaffold(
        modifier =
            modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background),
        topBar = {
            TopAppBar(title = "내 마음 서랍", showBackButton = true, onBackClick = navToBack)
        },
        snackbarHost = {
            AppSnackbarHost(
                hostState = snackState,
                customDataFlow = snackbarController?.currentData,
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background)
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // info text
            item {
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
                        painter = painterResource(id = R.drawable.ic_favorite_filled),
                        modifier =
                            Modifier
                                .width(11.dp)
                                .height(12.dp),
                        contentDescription = "open",
                        colorFilter = ColorFilter.tint(MooiTheme.colorScheme.gray600),
                    )
                    Text(
                        text = "오래 기억하고싶은 타임캡슐을 즐겨찾기 해보세요.\n최대 30개까지 저장할 수 있습니다.",
                        style = MooiTheme.typography.caption7.copy(lineHeight = 22.sp),
                        color = MooiTheme.colorScheme.gray500,
                    )
                }
            }
            if (timeCapsules != null && timeCapsules.loadState.refresh is LoadState.NotLoading) {
                if (timeCapsules.itemCount == 0) {
                    item {
                        Text(
                            modifier = Modifier.padding(top = 224.dp),
                            text = "아직 즐겨찾기한 타임캡슐이 없어요.",
                            style = MooiTheme.typography.caption2,
                            color = MooiTheme.colorScheme.gray400,
                        )
                    }
                } else {
                    item {
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
                    items(count = timeCapsules.itemCount, key = { timeCapsules[it]?.id ?: it }) {
                        timeCapsules[it]?.let {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Row(
                                    modifier =
                                        modifier
                                            .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Text(
                                        text = it.createdAt.formatToKorDateTime(),
                                        style = MooiTheme.typography.caption4,
                                        color = MooiTheme.colorScheme.gray300,
                                    )
                                    RoundedToggleButton(
                                        modifier = Modifier.size(36.dp),
                                        isSelected = favoriteState.favoriteIds.contains(it.id),
                                        onSelect = {
                                            onFavoriteAction(
                                                ToggleFavoriteAction.OnToggle(it.id),
                                            )
                                        },
                                    )
                                }
                                TimeCapsuleItem(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 26.dp),
                                    timeCapsule = it,
                                    showHeader = false,
                                    onClick = { navToTimeCapsuleDetail(it.id) },
                                )
                            }
                        }
                    }
                }
            } else {
                item {
                    LoadingDots(
                        modifier = Modifier.padding(top = 244.dp),
                        dotSize = 13.dp,
                        dotSpacing = 10.dp,
                    )
                }
                // todo: add error ui
            }
        }
    }
}

@Preview
@Composable
private fun FavoriteTimeCapsulesScreenPreview() {
    MooiTheme {
        StatelessFavoriteTimeCapsulesScreen()
    }
}
