package com.emotionstorage.time_capsule_detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.time_capsule_detail.presentation.SaveTimeCapsuleAction
import com.emotionstorage.time_capsule_detail.presentation.SaveTimeCapsuleState
import com.emotionstorage.time_capsule_detail.presentation.SaveTimeCapsuleViewModel
import com.emotionstorage.ui.component.CtaButton
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalDateTime

@Composable
fun SaveTimeCapsuleScreen(
    id: String,
    modifier: Modifier = Modifier,
    isNewTimeCapsule: Boolean = true,
    createdAt: LocalDateTime = LocalDateTime.now(),
    viewModel: SaveTimeCapsuleViewModel = hiltViewModel(),
    navToMain: () -> Unit = {},
    // should pop navigation twice
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
        navToMain = navToMain,
        navToBack = navToBack,
    )
}

@Composable
private fun StatelessSaveTimeCapsuleScreen(
    modifier: Modifier = Modifier,
    state: SaveTimeCapsuleState = SaveTimeCapsuleState(),
    onAction: (SaveTimeCapsuleAction) -> Unit = {},
    navToMain: () -> Unit = {},
    navToBack: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MooiTheme.colorScheme.background),
        topBar = {
            TopAppBar(
                showBackground = false,
                showBackButton = true,
                onBackClick = navToBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(bottom = 39.67.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            // title & selection grid
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text =
                        buildAnnotatedString {
                            append("이 감정을\n")
                            withStyle(SpanStyle(color = MooiTheme.colorScheme.primary)) {
                                append("언제 다시")
                            }
                            append("꺼내볼까요?")
                        },
                    style = MooiTheme.typography.head1.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.White,
                )
                // todo: add tool tip
            }

            // speech bubble & button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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

@Preview
@Composable
private fun SaveTimeCapsuleScreenPreview() {
    MooiTheme {
        StatelessSaveTimeCapsuleScreen()
    }
}
