package com.emotionstorage.tutorial.ui.onBoarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.tutorial.presentation.onBoarding.AgreeTermsEvent
import com.emotionstorage.tutorial.presentation.onBoarding.AgreeTermsViewModel
import com.emotionstorage.tutorial.presentation.onBoarding.AgreeTermsViewModel.State
import com.emotionstorage.ui.component.CtaButton
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.component.CheckboxIcon
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.tutorial.R
import kotlinx.coroutines.launch

/**
 * On boarding step 4
 * - agree terms of service
 * - signup user before navigating to signup complete screen
 */
@Composable
fun AgreeTermsScreen(
    modifier: Modifier = Modifier,
    viewModel: AgreeTermsViewModel = hiltViewModel(),
    onAgreeTermsInputComplete: (isTermAgreed: Boolean, isPrivacyAgreed: Boolean, isMarketingAgreed: Boolean) -> Unit = { _, _, _ -> },
    onSignup: suspend () -> Boolean = { false },
    navToSignupComplete: () -> Unit = {},
) {

    val state = viewModel.state.collectAsState().value

    StatelessAgreeTermsScreen(
        state = state,
        event = viewModel.event,
        modifier = modifier,
        onAgreeTermsInputComplete = onAgreeTermsInputComplete,
        onSignup = onSignup,
        navToSignupComplete = navToSignupComplete
    )
}


@Composable
private fun StatelessAgreeTermsScreen(
    state: State,
    event: AgreeTermsEvent,
    modifier: Modifier = Modifier,
    onAgreeTermsInputComplete: (isTermAgreed: Boolean, isPrivacyAgreed: Boolean, isMarketingAgreed: Boolean) -> Unit = { _, _, _ -> },
    onSignup: suspend () -> Boolean = { false },
    navToSignupComplete: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier
            .background(MooiTheme.colorScheme.background)
            .fillMaxSize(),
        topBar = {
            TopAppBar(showBackButton = true)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .background(MooiTheme.colorScheme.background)
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .imePadding(),
        ) {
            OnBoardingTitle(
                modifier = Modifier.fillMaxWidth(),
                currentStep = 3,
                title = stringResource(R.string.on_boarding_terms_title),
                titleHighlights = stringResource(R.string.on_boarding_terms_title_highlights).split(
                    ','
                )
            )

            // todo: add agree terms column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 46.dp),
                verticalArrangement = Arrangement.spacedBy(19.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(11.dp)
                    ) {
                        CheckboxIcon(
                            isSelected = state.isAllAgree,
                            onSelect = event::onToggleAllAgree
                        )
                        Text(
                            style = MooiTheme.typography.body2.copy(fontWeight = FontWeight.SemiBold),
                            color = Color.White,
                            text = "약관 전체 동의"
                        )
                    }

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.5.dp)
                            .background(MooiTheme.colorScheme.gray600)
                    )

                    TermItem(
                        term = "[필수] 이용 약관 동의",
                        isSelected = state.isTermAgree,
                        onSelect = event::onToggleTermAgree,
                        onShowTermDetail = {
                            // todo: nav to term detail screen
                        }
                    )
                    TermItem(
                        term = "[필수] 개인정보 수집 및 이용 동의",
                        isSelected = state.isPrivacyAgree,
                        onSelect = event::onTogglePrivacyAgree,
                        onShowTermDetail = {
                            // todo: nav to term detail screen
                        }
                    )
                    TermItem(
                        term = "[선택] 마케팅 활용 및 수신 동의",
                        isSelected = state.isMarketingAgree,
                        onSelect = event::onToggleMarketingAgree,
                        onShowTermDetail = {
                            // todo: nav to marketing term detail screen
                        }
                    )
                    TermItem(
                        term = "[필수] 만 14세 이상입니다",
                        isSelected = state.isAgeAgree,
                        onSelect = event::onToggleAgeAgree,
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        style = MooiTheme.typography.body3.copy(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Light
                        ),
                        color = MooiTheme.colorScheme.primary,
                        text = "* 필수 약관에 동의하셔야만 서비스를 이용하실 수 있어요.",
                    )
                    Text(
                        style = MooiTheme.typography.body3.copy(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Light
                        ),
                        color = MooiTheme.colorScheme.primary,
                        text = "* 선택 약관은 원하실 경우에만 동의하셔도 괜찮아요.",
                    )
                }
            }

            // todo: add bottom padding when keyboard is hidden
            CtaButton(
                modifier = Modifier
                    .fillMaxWidth(),
//                    .padding(bottom = 39.dp),
                label = "가입 완료하기",
                enabled = state.isSignupCompleteButtonEnabled,
                onClick = {
                    coroutineScope.launch {
                        onAgreeTermsInputComplete(
                            state.isTermAgree,
                            state.isPrivacyAgree,
                            state.isMarketingAgree
                        )

                        if (onSignup()) {
                            navToSignupComplete()
                        } else {
                            // todo: add signup fail ui
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun TermItem(
    term: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onSelect: () -> Unit = {},
    onShowTermDetail: (() -> Unit)? = null
) {
    Row(modifier = modifier) {
        CheckboxIcon(
            isSelected = isSelected,
            onSelect = onSelect
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 11.dp)
                .clickable(
                    enabled = onShowTermDetail != null,
                    onClick = {
                        onShowTermDetail?.invoke()
                    }
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                style = MooiTheme.typography.body2,
                color = Color.White,
                text = term
            )
        }
        if (onShowTermDetail != null) {
            Image(
                modifier = Modifier.height(12.dp).width(6.dp),
                painter = painterResource(R.drawable.arrow_right),
                contentDescription = null
            )
        }
    }
}

@PreviewScreenSizes
@Composable
private fun AgreeTermsScreenPreview() {
    MooiTheme {
        StatelessAgreeTermsScreen(
            state = State(),
            event = object : AgreeTermsEvent {
                override fun onToggleAllAgree() {}
                override fun onToggleTermAgree() {}
                override fun onTogglePrivacyAgree() {}
                override fun onToggleMarketingAgree() {}
                override fun onToggleAgeAgree() {}
            }
        )
    }
}