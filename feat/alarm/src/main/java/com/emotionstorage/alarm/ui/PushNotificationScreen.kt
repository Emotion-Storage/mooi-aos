package com.emotionstorage.alarm.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.emotionstorage.alarm.presentation.PushNotificationAction
import com.emotionstorage.alarm.presentation.PushNotificationSideEffect
import com.emotionstorage.alarm.presentation.PushNotificationViewModel
import com.emotionstorage.alarm.ui.component.EmptyPushHolder
import com.emotionstorage.alarm.ui.component.PushAlarmCard
import com.emotionstorage.domain.model.Notification
import com.emotionstorage.domain.model.NotificationType.DailyReportArrival
import com.emotionstorage.domain.model.NotificationType.TimeCapsuleArrival
import com.emotionstorage.ui.R
import com.emotionstorage.ui.annotation.PreviewScreenRatios
import com.emotionstorage.ui.component.appBar.TopAppBar
import com.emotionstorage.ui.component.loading.LoadingDots
import com.emotionstorage.ui.component.toast.AppSnackbarController
import com.emotionstorage.ui.component.toast.AppSnackbarHost
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun PushNotificationScreen(
    viewModel: PushNotificationViewModel = hiltViewModel(),
    navToTimeCapsuleDetail: (Long) -> Unit = {},
    navToDailyReportDetail: (Long) -> Unit = {},
    navToBack: () -> Unit = {},
) {
    val lazyNotifications = viewModel.notifications.collectAsLazyPagingItems()

    val snackState = remember { SnackbarHostState() }
    val snackbarController = remember { AppSnackbarController(snackState) }

    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect {
            when (it) {
                is PushNotificationSideEffect.GetDailyReportDetailSuccess -> {
                    navToDailyReportDetail(it.id)
                }

                is PushNotificationSideEffect.GetTimeCapsuleDetailSuccess -> {
                    navToTimeCapsuleDetail(it.id)
                }

                is PushNotificationSideEffect.GetDetailError -> {
                    snackbarController.showSnackbar(
                        message = "아쉽지만 이 기록은 더 이상 열 수 없어요.",
                    )
                }
            }
        }
    }

    StatelessPushNotificationScreen(
        lazyNotifications = lazyNotifications,
        snackState = snackState,
        snackbarController = snackbarController,
        onAction = viewModel::onAction,
        navToBack = navToBack,
    )
}

@Composable
private fun StatelessPushNotificationScreen(
    onAction: (action: PushNotificationAction) -> Unit,
    navToBack: () -> Unit,
    modifier: Modifier = Modifier,
    lazyNotifications: LazyPagingItems<Notification>? = null,
    snackState: SnackbarHostState = SnackbarHostState(),
    snackbarController: AppSnackbarController? = null,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = "알림 내역",
                showBackButton = true,
                onBackClick = navToBack,
            )
        },
        snackbarHost = {
            AppSnackbarHost(
                hostState = snackState,
                customDataFlow = snackbarController?.currentData,
            )
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = MooiTheme.colorScheme.backgroundDefault)
                    .padding(innerPadding),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(13.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Icon(
                            modifier =
                                Modifier
                                    .size(18.dp),
                            painter = painterResource(R.drawable.ic_alarm),
                            contentDescription = "알림 아이콘",
                            tint = MooiTheme.colorScheme.gray600,
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "최근 3주간의 알림이 표시됩니다.",
                            style = MooiTheme.typography.caption7,
                            color = MooiTheme.colorScheme.gray500,
                        )
                    }
                }

                // update ui when load state is not loading
                when (lazyNotifications?.loadState?.refresh) {
                    is LoadState.NotLoading -> {
                        if (lazyNotifications.itemCount == 0) {
                            item {
                                EmptyPushHolder()
                            }
                        } else {
                            items(
                                count = lazyNotifications.itemCount,
                                key = { lazyNotifications[it]?.id ?: it },
                            ) { item ->
                                val item = lazyNotifications[item] ?: return@items

                                when (item.type) {
                                    is DailyReportArrival -> {
                                        PushAlarmCard(
                                            notification = item,
                                            onClick = {
                                                onAction(
                                                    PushNotificationAction.GetDailyReportDetail(
                                                        (item.type as DailyReportArrival).dailyReportId,
                                                    ),
                                                )
                                            },
                                        )
                                    }

                                    is TimeCapsuleArrival -> {
                                        PushAlarmCard(
                                            notification = item,
                                            onClick = {
                                                onAction(
                                                    PushNotificationAction.GetTimeCapsuleDetail(
                                                        (item.type as TimeCapsuleArrival).timeCapsuleId,
                                                    ),
                                                )
                                            },
                                        )
                                    }

                                    else -> {
                                        // no ui
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
}

@PreviewScreenRatios
@Composable
fun PushNotificationScreenPreview() {
    MooiTheme {
        StatelessPushNotificationScreen(
            lazyNotifications = null,
            onAction = {},
            navToBack = {},
        )
    }
}
