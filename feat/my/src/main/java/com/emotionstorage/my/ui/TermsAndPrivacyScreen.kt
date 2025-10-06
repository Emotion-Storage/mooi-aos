package com.emotionstorage.my.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.my.ui.component.PrivacyPolicyContent
import com.emotionstorage.my.ui.component.TermsOfServiceContent
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun TermsAndPrivacyScreen(
    modifier: Modifier = Modifier,
    navToBack: () -> Unit = {},
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("이용약관", "개인정보처리방침")

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier =
            modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background),
        topBar = {
            TopAppBar(title = "이용 약관 및 개인정보처리방침", showBackButton = true, onBackClick = navToBack)
        },
    ) { innerPadding ->

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(WindowInsets.navigationBars)
                    .background(
                        color = MooiTheme.colorScheme.background,
                    ),
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MooiTheme.colorScheme.background,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = MooiTheme.colorScheme.primary,
                    )
                },
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                style = MooiTheme.typography.body7,
                                color =
                                    if (selectedTabIndex ==
                                        index
                                    ) {
                                        MooiTheme.colorScheme.primary
                                    } else {
                                        MooiTheme.colorScheme.gray800
                                    },
                            )
                        },
                    )
                }
            }
            when (selectedTabIndex) {
                0 -> TermsOfServiceContent()
                1 -> PrivacyPolicyContent()
            }
        }
    }
}

@Preview
@Composable
private fun TermsAndPrivacyScreenPreview() {
    MooiTheme {
        TermsAndPrivacyScreen(
            modifier = Modifier,
        )
    }
}
