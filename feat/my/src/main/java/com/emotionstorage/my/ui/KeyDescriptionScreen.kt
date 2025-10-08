package com.emotionstorage.my.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.my.ui.component.CountRow
import com.emotionstorage.my.ui.component.WhenToUseKeyDialog
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun KeyDescriptionScreen(navToBack: () -> Unit) {
    PreviewKeyDescriptionScreen(
        navToBack = navToBack,
    )
}

@Composable
fun PreviewKeyDescriptionScreen(navToBack: () -> Unit = {}) {
    var showWhenToUseDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                showBackButton = true,
                handleBackPress = true,
                onBackClick = navToBack,
            )
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = MooiTheme.colorScheme.background),
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(innerPadding)
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
                            .heightIn(198.dp)
                            .widthIn(198.dp)
                            .align(Alignment.CenterHorizontally),
                    painter = painterResource(R.drawable.big_key),
                    contentDescription = "열쇠",
                )

                Spacer(modifier = Modifier.padding(6.dp))

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
                    // Todo : ViewModel에 keyCount를 UI State로 변경한 후 값 전달하기
                    count = 123,
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
                            showWhenToUseDialog = true
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(R.drawable.question_mark),
                        contentDescription = "물음표",
                    )

                    Spacer(modifier = Modifier.size(4.dp))

                    Text(
                        text = "열쇠는 언제 써야 좋을까요?",
                        style = MooiTheme.typography.caption5,
                        color = MooiTheme.colorScheme.gray500,
                    )
                }
            }
        }
        if (showWhenToUseDialog) {
            WhenToUseKeyDialog { showWhenToUseDialog = false }
        }
    }
}

@Preview
@Composable
fun KeyDescriptionScreenPreview() {
    MooiTheme {
        PreviewKeyDescriptionScreen()
    }
}
