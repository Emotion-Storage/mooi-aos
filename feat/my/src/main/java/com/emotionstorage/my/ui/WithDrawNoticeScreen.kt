package com.emotionstorage.my.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.my.presentation.MyPageSideEffect
import com.emotionstorage.my.presentation.MyPageViewModel
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun WithDrawNoticeScreen(
    viewModel: MyPageViewModel = hiltViewModel(),
    navToBack: () -> Unit = {},
    navToSplash: () -> Unit = {},
) {
    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is MyPageSideEffect.WithDrawSuccess -> {
                    navToSplash()
                }

                else -> Unit
            }
        }
    }



}

@Composable
fun StatelessWithDrawNoticeScreen(
    onBackClick: () -> Unit = {},
    onButtonClick: () -> Unit = {},
) {

}

@Preview
@Composable
private fun WithDrawNoticeScreenPreview() {
    MooiTheme {
        WithDrawNoticeScreen()
    }
}
