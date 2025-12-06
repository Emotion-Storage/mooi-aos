package com.emotionstorage.my.ui.keyDescription

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.emotionstorage.my.presentation.KeyBalanceViewModel
import com.emotionstorage.my.presentation.KeyCountState
import com.emotionstorage.my.presentation.KeyDescriptionDialog
import com.emotionstorage.my.ui.keyDescription.component.CountRow
import com.emotionstorage.my.ui.keyDescription.component.WhenToUseKeyDialog
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.appBar.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun KeyDescriptionScreen(
    modifier: Modifier = Modifier,
    viewModel: KeyBalanceViewModel = hiltViewModel(),
    navToBack: () -> Unit,
) {
    val state by viewModel.keyCountState.collectAsStateWithLifecycle()

    LifecycleStartEffect("onStart") {
        viewModel.refreshKeyCount()
        onStopOrDispose {}
    }

    StatelessKeyDescriptionScreen(
        modifier = modifier,
        state,
        onOpenDialog = viewModel::openWhenToUseDialog,
        onDismissDialog = viewModel::dismissDialog,
        navToBack = navToBack,
    )
}

@Composable
fun StatelessKeyDescriptionScreen(
    modifier: Modifier = Modifier,
    state: KeyCountState,
    onOpenDialog: () -> Unit = {},
    onDismissDialog: () -> Unit = {},
    navToBack: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                showBackButton = true,
                showBackground = false,
                onBackClick = navToBack,
            )
        },
        containerColor = MooiTheme.colorScheme.background,
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            Column(
                modifier =
                    Modifier
                        .align(Alignment.TopCenter),
            ) {
                Text(
                    modifier = Modifier.padding(top = 24.dp),
                    text = "차곡차곡 모은 열쇠로\n타입캡슐을 미리 열어보세요!",
                    style = MooiTheme.typography.head2,
                    color = MooiTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.padding(10.dp))

                Text(
                    text = "열쇠는 출석 보상을 통해 얻을 수 있어요.",
                    style = MooiTheme.typography.body5,
                    color = MooiTheme.colorScheme.gray400,
                )

                Spacer(modifier = Modifier.padding(28.dp))

                Image(
                    modifier =
                        Modifier
                            .heightIn(147.dp)
                            .widthIn(147.dp)
                            .align(Alignment.CenterHorizontally),
                    painter = painterResource(R.drawable.graphic__key),
                    contentDescription = "열쇠",
                )

                Spacer(modifier = Modifier.padding(58.dp))

                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "현재 보유한 열쇠",
                    style = MooiTheme.typography.head1,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.size(12.dp))
                CountRow(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    count = state.keyCount,
                )
            }

            Box(
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 48.dp),
            ) {
                Row(
                    modifier =
                        Modifier.clickable {
                            onOpenDialog()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_question_mark),
                        contentDescription = "물음표",
                    )

                    Spacer(modifier = Modifier.size(4.dp))

                    Text(
                        text = "열쇠는 언제 써야 좋을까요?",
                        style = MooiTheme.typography.caption4,
                        color = MooiTheme.colorScheme.gray500,
                    )
                }
            }

            when (state.dialog) {
                KeyDescriptionDialog.WhenToUse -> {
                    WhenToUseKeyDialog(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        onDismiss = onDismissDialog,
                    )
                }

                null -> {
                    Unit
                }
            }
        }
    }
}

@Preview
@Composable
fun KeyDescriptionScreenPreview() {
    MooiTheme {
        StatelessKeyDescriptionScreen(
            state = KeyCountState(keyCount = 5),
        )
    }
}
