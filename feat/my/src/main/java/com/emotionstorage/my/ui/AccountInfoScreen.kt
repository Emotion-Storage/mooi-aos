package com.emotionstorage.my.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.my.presentation.AccountInfoState
import com.emotionstorage.my.presentation.AccountInfoViewModel
import com.emotionstorage.my.ui.component.AccountInfoContent
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun AccountInfoScreen(
    viewModel: AccountInfoViewModel = hiltViewModel(),
    navToBack: () -> Unit = {},
) {
    val state = viewModel.state.collectAsState()

    PreviewAccountInfoScreen(
        state = state.value,
        navToBack = navToBack
    )
}

@Composable
fun PreviewAccountInfoScreen(
    state: AccountInfoState = AccountInfoState(),
    navToBack: () -> Unit = {},
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                showBackButton = true,
                title = "계정 정보",
                handleBackPress = true,
                onBackClick = navToBack,
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MooiTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(WindowInsets.navigationBars),
            ) {

                Row {
                    Text(
                        modifier = Modifier.padding(
                            start = 16.dp, top = 14.dp
                        ),
                        text = "*",
                        style = MooiTheme.typography.caption7,
                        color = MooiTheme.colorScheme.gray500
                    )
                    Text(
                        modifier = Modifier.padding(
                            start = 2.dp, top = 16.dp
                        ),
                        text = "가입 시 입력한 기본 정보는 변경할 수 없습니다.",
                        style = MooiTheme.typography.caption3,
                        color = MooiTheme.colorScheme.gray500
                    )
                }

                Spacer(modifier = Modifier.padding(16.dp))

                AccountInfoContent(
                    email = state.email,
                    socialType = state.authProvider,
                    gender = state.gender,
                    birthYear = state.birthYear,
                    birthMonth = state.birthMonth,
                    birthDay = state.birthDay,
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewAccountInfoScreenPreview() {
    MooiTheme {
        PreviewAccountInfoScreen()
    }
}
