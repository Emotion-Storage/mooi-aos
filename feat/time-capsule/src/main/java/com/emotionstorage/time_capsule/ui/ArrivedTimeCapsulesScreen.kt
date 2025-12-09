package com.emotionstorage.time_capsule.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.emotionstorage.common.formatToKorDateTime
import com.emotionstorage.time_capsule.presentation.ArrivedTimeCapsulesViewModel
import com.emotionstorage.time_capsule.ui.component.timeCapsuleItem.TimeCapsuleItem
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.appBar.TopAppBar
import com.emotionstorage.ui.component.loading.LoadingDots
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun ArrivedTimeCapsulesScreen(
    modifier: Modifier = Modifier,
    viewModel: ArrivedTimeCapsulesViewModel = hiltViewModel(),
    navToTimeCapsuleDetail: (id: Long) -> Unit = {},
    navToBack: () -> Unit = {},
) {
    val context = LocalContext.current
    val timeCapsulesState = viewModel.arrivedTimeCapsules.collectAsLazyPagingItems()

    StatelessArrivedTimeCapsulesScreen(
        modifier = modifier,
        timeCapsules = timeCapsulesState,
        navToTimeCapsuleDetail = navToTimeCapsuleDetail,
        navToBack = navToBack,
    )
}

@Composable
private fun StatelessArrivedTimeCapsulesScreen(
    modifier: Modifier = Modifier,
    timeCapsules: LazyPagingItems<TimeCapsuleItemState>? = null,
    navToTimeCapsuleDetail: (id: Long) -> Unit = {},
    navToBack: () -> Unit = {},
) {
    Scaffold(
        modifier =
            modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.backgroundDefault),
        topBar = {
            TopAppBar(
                title = "도착한 타임캡슐",
                showBackButton = true,
                onBackClick = navToBack,
            )
        },
    ) { innerPadding ->
        // todo: pull down to refresh
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.backgroundDefault)
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
                        painter = painterResource(id = R.drawable.ic_lock_open),
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

            // update ui when load state is not loading
            when (timeCapsules?.loadState?.refresh) {
                is LoadState.NotLoading -> {
                    if (timeCapsules.itemCount == 0) {
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
                            timeCapsules[it]?.let {
                                Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                                    Text(
                                        text = it.createdAt.formatToKorDateTime(),
                                        style = MooiTheme.typography.caption4,
                                        color = MooiTheme.colorScheme.gray300,
                                    )
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
                }

                else -> {
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
}
