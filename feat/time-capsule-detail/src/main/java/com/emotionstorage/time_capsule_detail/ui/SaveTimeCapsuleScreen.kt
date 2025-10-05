package com.emotionstorage.time_capsule_detail.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.time_capsule_detail.presentation.SaveTimeCapsuleAction
import com.emotionstorage.time_capsule_detail.presentation.SaveTimeCapsuleState
import com.emotionstorage.time_capsule_detail.presentation.SaveTimeCapsuleState.ArriveAfter
import com.emotionstorage.time_capsule_detail.presentation.SaveTimeCapsuleViewModel
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.CtaButton
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.subBackground
import org.intellij.lang.annotations.JdkConstants
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun SaveTimeCapsuleScreen(
    id: String,
    modifier: Modifier = Modifier,
    isNewTimeCapsule: Boolean = true,
    createdAt: LocalDateTime = LocalDateTime.now(),
    viewModel: SaveTimeCapsuleViewModel = hiltViewModel(),
    navToHome: () -> Unit = {},
    navToPrevious: () -> Unit = {},
    navToBack: () -> Unit = {},
) {
    val state = viewModel.container.stateFlow.collectAsState()
    LaunchedEffect(id, isNewTimeCapsule, createdAt) {
        viewModel.onAction(SaveTimeCapsuleAction.Init(id, isNewTimeCapsule, createdAt))
    }

    StatelessSaveTimeCapsuleScreen(
        modifier = modifier,
        state = state.value,
        onAction = viewModel::onAction,
        navToMain = navToHome,
        navToPrevious = navToPrevious,
        navToBack = navToBack,
    )
}

@Composable
private fun StatelessSaveTimeCapsuleScreen(
    modifier: Modifier = Modifier,
    state: SaveTimeCapsuleState = SaveTimeCapsuleState(),
    onAction: (SaveTimeCapsuleAction) -> Unit = {},
    navToMain: () -> Unit = {},
    navToPrevious: () -> Unit = {},
    navToBack: () -> Unit = {},
) {
    val (showToolTip, setShowToolTip) = remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MooiTheme.colorScheme.background),
        topBar = {
            TopAppBar(
                showBackground = false,
                showBackButton = true,
                onBackClick = navToBack,
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(bottom = 39.67.dp),
        ) {
            if (showToolTip) {
                // todo: tool tip
                // todo: handle background click
            }

            // title & selection grid
            Column(
                modifier = Modifier.align(Alignment.TopStart),
                verticalArrangement = Arrangement.spacedBy(17.dp),
            ) {
                SaveTimeCapsuleTitle(
                    onToolTipClick = { setShowToolTip(true) })
                SaveTimeCapsuleGrid(
                    arriveAt = state.arriveAt?.toLocalDate(),
                    arriveAfter = state.arriveAfter,
                    onSelectArriveAfter = {
                        onAction(SaveTimeCapsuleAction.SelectArriveAfter(it))
                    })
            }

            // speech bubble & button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CtaButton(
                    modifier = Modifier.fillMaxWidth(),
                    labelString = "타임캡슐 보관하기",
                    onClick = {
                        onAction(SaveTimeCapsuleAction.SaveTimeCapsule)
                    },
                    isDefaultWidth = false,
                )
            }
        }
    }
}

@Composable
private fun SaveTimeCapsuleTitle(
    modifier: Modifier = Modifier,
    onToolTipClick: () -> Unit = {},
) {
    Column(modifier = modifier) {
        Text(
            text = "이 감정을",
            style = MooiTheme.typography.head1,
            color = Color.White,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = MooiTheme.colorScheme.primary)) {
                        append("언제 다시")
                    }
                    append("꺼내볼까요?")
                },
                style = MooiTheme.typography.head1,
                color = Color.White,
            )
            Image(
                modifier = Modifier
                    .size(26.dp)
                    .clickable(
                        onClick = onToolTipClick
                    ), painter = painterResource(R.drawable.info), contentDescription = "tool tip"
            )
        }
    }
}

@Composable
fun SaveTimeCapsuleGrid(
    modifier: Modifier = Modifier,
    arriveAt: LocalDate? = null,
    arriveAfter: ArriveAfter? = null,
    onSelectArriveAfter: (ArriveAfter) -> Unit = {},
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "타임캡슐 오픈일",
            style = MooiTheme.typography.body1,
            color = Color.White
        )
        Text(
            text = "* 감정 회고일을 선택하세요. 한 번 더 탭하면 해제돼요.",
            style = MooiTheme.typography.caption7,
            color = MooiTheme.colorScheme.gray500
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                items = ArriveAfter.entries.toTypedArray(),
                key = { it },
            ) { it ->

                ArriveAfterGridItem(
                    arriveAfter = it,
                    isSelected = it == arriveAfter,
                    onSelect = {
                        onSelectArriveAfter(it)
                    }
                )
                if (it == ArriveAfter.AFTER_CUSTOM && it == arriveAfter) {
                    // todo: style date selection row
                    Row(
                        modifier = Modifier.clickable {
                            // todo: show date picker bottom sheet
                        }
                    ) {
                        if (arriveAt == null) {
                            Text("날짜를 선택해주세요")
                        } else {
                            Text(text = arriveAt.toString())
                        }
                        Image(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(R.drawable.calendar),
                            contentDescription = "calendar date picker"
                        )
                    }
                }
            }


        }
    }
}

@Composable
private fun ArriveAfterGridItem(
    arriveAfter: ArriveAfter,
    modifier: Modifier = Modifier,
    onSelect: () -> Unit = {},
    isSelected: Boolean = false,
) {
    Box(
        modifier = modifier
            .size(95.dp, 54.dp)
            .subBackground(
                enabled = isSelected,
                defaultBackground = Color.Black,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(
                enabled = !isSelected,
                onClick = onSelect
            )
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = arriveAfter.label,
            style = MooiTheme.typography.body8,
            color = if (isSelected) MooiTheme.colorScheme.primary else Color.White
        )
    }
}


@Preview
@Composable
private fun SaveTimeCapsuleScreenPreview() {
    MooiTheme {
        StatelessSaveTimeCapsuleScreen()
    }
}
