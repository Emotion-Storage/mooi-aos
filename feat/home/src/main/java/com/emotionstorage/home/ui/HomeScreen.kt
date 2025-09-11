package com.emotionstorage.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.home.presentation.HomeAction
import com.emotionstorage.home.presentation.HomeSideEffect
import com.emotionstorage.home.presentation.HomeState
import com.emotionstorage.home.presentation.HomeViewModel
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navToChat: (roomId: String) -> Unit = {}
) {
    val state = viewModel.container.stateFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect {
            when (it) {
                is HomeSideEffect.EnterCharRoomSuccess -> {
                    navToChat(it.roomId)
                }
            }
        }
    }

    StatelessHomeScreen(
        modifier = modifier,
        state = state.value,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun StatelessHomeScreen(
    modifier: Modifier = Modifier,
    state: HomeState = HomeState(),
    onAction: (HomeAction) -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            onAction(HomeAction.EnterChat)
        }) {
            Text("대화 시작하기")
        }
        Button(onClick = { /*TODO: 로그아웃*/ }) {
            Text("로그아웃")
        }
        Button(onClick = { /*TODO: 회원탈퇴*/ }) {
            Text("회원탈퇴")
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    MooiTheme {
        StatelessHomeScreen()
    }
}