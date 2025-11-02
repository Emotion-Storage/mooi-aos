package com.emotionstorage.time_capsule.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.emotionstorage.time_capsule.presentation.ArrivedTimeCapsulesAction
import com.emotionstorage.time_capsule.presentation.ArrivedTimeCapsulesSideEffect
import com.emotionstorage.time_capsule.presentation.ArrivedTimeCapsulesViewModel
import com.emotionstorage.time_capsule.ui.component.TimeCapsuleItem
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.toast.AppSnackbarHost
import com.emotionstorage.ui.component.toast.FavoriteToast
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun ArrivedTimeCapsulesScreen(
    modifier: Modifier = Modifier,
    viewModel: ArrivedTimeCapsulesViewModel = hiltViewModel(),
    navToTimeCapsuleDetail: (id: Long) -> Unit = {},
    navToBack: () -> Unit = {},
) {
    val state = viewModel.container.stateFlow.collectAsState()
    val timeCapsulesState = state.value.timeCapsules?.collectAsLazyPagingItems()

    val snackState = remember { SnackbarHostState() }
    LaunchedEffect("init") {
        viewModel.onAction(ArrivedTimeCapsulesAction.Init)

        viewModel.container.sideEffectFlow.collect {
            when (it) {
                is ArrivedTimeCapsulesSideEffect.ShowFavoriteToast -> {
                    snackState.currentSnackbarData?.dismiss()
                    snackState.showSnackbar(it.favoriteResult.name)
                }
            }
        }
    }

    StatelessArrivedTimeCapsulesScreen(
        modifier = modifier,
        snackState = snackState,
        timeCapsules = timeCapsulesState,
        onAction = viewModel::onAction,
        navToTimeCapsuleDetail = navToTimeCapsuleDetail,
        navToBack = navToBack,
    )
}

@Composable
private fun StatelessArrivedTimeCapsulesScreen(
    modifier: Modifier = Modifier,
    snackState: SnackbarHostState = remember { SnackbarHostState() },
    timeCapsules: LazyPagingItems<TimeCapsuleItemState>? = null,
    onAction: (ArrivedTimeCapsulesAction) -> Unit = {},
    navToTimeCapsuleDetail: (id: Long) -> Unit = {},
    navToBack: () -> Unit = {},
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier =
            modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background),
        topBar = {
            TopAppBar(title = "도착한 타임캡슐", showBackButton = true, onBackClick = navToBack)
        },
        snackbarHost = {
            AppSnackbarHost(
                hostState = snackState,
            ) {
                FavoriteToast(it.visuals.message)
            }
        },
    ) { innerPadding ->
        // todo: pull down to refresh
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background)
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 13.dp, bottom = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.lock_open),
                        modifier =
                            Modifier
                                .width(12.dp)
                                .height(14.dp),
                        contentDescription = "arrived",
                        colorFilter = ColorFilter.tint(MooiTheme.colorScheme.gray600),
                    )
                    Text(
                        text = "최근 3주간 도착한 타임캡슐을 표시합니다.\n도착한 타임캡슐을 열어 내 지난 감정을 확인해보세요.",
                        style = MooiTheme.typography.caption7.copy(lineHeight = 22.sp),
                        color = MooiTheme.colorScheme.gray500,
                    )
                }
            }

            if (timeCapsules == null || timeCapsules.itemCount == 0) {
                item {
                    Text(
                        modifier = Modifier.padding(top = 244.dp),
                        text = "최근 도착한 타임캡슐이 없어요.",
                        style = MooiTheme.typography.caption2,
                        color = MooiTheme.colorScheme.gray400,
                        textAlign = TextAlign.Center,
                    )
                }
            } else {
                items(count = timeCapsules.itemCount, key = { timeCapsules[it]?.id ?: it }) {
                    timeCapsules[it]?.run {
                        TimeCapsuleItem(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 26.dp),
                            timeCapsule = this,
                            showDate = true,
                            showInfoText = false,
                            showFavorite = true,
                            onClick = { navToTimeCapsuleDetail(this.id) },
                            onFavoriteClick = {
                                onAction(
                                    ArrivedTimeCapsulesAction.ToggleFavorite(
                                        this.id,
                                        this.isFavorite,
                                    ),
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}
