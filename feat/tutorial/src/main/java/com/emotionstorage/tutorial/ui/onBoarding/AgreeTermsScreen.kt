package com.emotionstorage.tutorial.ui.onBoarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.tutorial.R as tutorialR
import com.emotionstorage.tutorial.presentation.onBoarding.AgreeTermsEvent
import com.emotionstorage.tutorial.presentation.onBoarding.AgreeTermsViewModel
import com.emotionstorage.tutorial.presentation.onBoarding.AgreeTermsViewModel.State
import com.emotionstorage.tutorial.ui.component.OnBoardingTitle
import com.emotionstorage.ui.component.appBar.TopAppBar
import com.emotionstorage.ui.component.button.CtaButton
import com.emotionstorage.ui.component.button.ToggleButton
import com.emotionstorage.ui.theme.MooiTheme
import kotlinx.coroutines.launch

/**
 * On boarding step 4
 * - agree terms of service
 * - signup user before navigating to signup complete screen
 */
@Composable
fun AgreeTermsScreen(
    modifier: Modifier = Modifier,
    isAllAgreed: Boolean? = null,
    isTermAgreed: Boolean? = null,
    isPrivacyAgreed: Boolean? = null,
    isMarketingAgreed: Boolean? = null,
    isAgeAgreed: Boolean? = null,
    viewModel: AgreeTermsViewModel = hiltViewModel(),
    onAgreeTermsInputComplete: (
        isAllAgreed: Boolean,
        isTermAgreed: Boolean,
        isPrivacyAgreed: Boolean,
        isMarketingAgreed: Boolean,
        isAgeAgreed: Boolean,
    ) -> Unit = { _, _, _, _, _ -> },
    onSignup: suspend () -> Unit = {},
    navToBack: () -> Unit = {},
    navToTermDetail: () -> Unit = {},
    navToPrivacyDetail: () -> Unit = {},
    navToMarketingDetail: () -> Unit = {},
    // todo: delete test navigations
    navToSignupComplete: () -> Unit = {},
) {
    val state = viewModel.state.collectAsState().value

    LaunchedEffect("init") {
        viewModel.event.resetAgreedTerms()
        if (isAllAgreed == true) {
            viewModel.event.onToggleAllAgreed()
        } else {
            if (isTermAgreed == true) viewModel.event.onToggleTermAgreed()
            if (isPrivacyAgreed == true) viewModel.event.onTogglePrivacyAgreed()
            if (isMarketingAgreed == true) viewModel.event.onToggleMarketingAgreed()
            if (isAgeAgreed == true) viewModel.event.onToggleAgeAgreed()
        }
    }

    StatelessAgreeTermsScreen(
        state = state,
        event = viewModel.event,
        modifier = modifier,
        onSignup = onSignup,
        onAgreeTermsInputComplete = onAgreeTermsInputComplete,
        navToBack = navToBack,
        navToTermDetail = navToTermDetail,
        navToPrivacyDetail = navToPrivacyDetail,
        navToMarketingDetail = navToMarketingDetail,
        navToSignupComplete = navToSignupComplete,
    )
}

@Composable
private fun StatelessAgreeTermsScreen(
    state: State,
    event: AgreeTermsEvent,
    modifier: Modifier = Modifier,
    onAgreeTermsInputComplete: (
        isAllAgreed: Boolean,
        isTermAgreed: Boolean,
        isPrivacyAgreed: Boolean,
        isMarketingAgreed: Boolean,
        isAgeAgreed: Boolean,
    ) -> Unit = { _, _, _, _, _ -> },
    onSignup: suspend () -> Unit = {},
    navToBack: () -> Unit = {},
    navToTermDetail: () -> Unit = {},
    navToPrivacyDetail: () -> Unit = {},
    navToMarketingDetail: () -> Unit = {},
    // todo: delete test navigations
    navToSignupComplete: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()
    val onNavBack: () -> Unit = {
        // save agreed terms state on nav back, to restore state
        coroutineScope.launch {
            onAgreeTermsInputComplete(
                state.isAllAgreed,
                state.isTermAgreed,
                state.isPrivacyAgreed,
                state.isMarketingAgreed,
                state.isAgeAgreed,
            )
            navToBack()
        }
    }

    Scaffold(
        modifier =
            modifier
                .background(MooiTheme.colorScheme.backgroundDefault)
                .fillMaxSize(),
        topBar = {
            TopAppBar(
                showBackground = false,
                showBackButton = true,
                onBackClick = onNavBack,
                handleBackPress = true,
                onHandleBackPress = onNavBack,
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.backgroundDefault)
                    .padding(innerPadding),
        ) {
            OnBoardingTitle(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                currentStep = 3,
                title = stringResource(tutorialR.string.on_boarding_p3_title),
                titleHighlights =
                    stringResource(tutorialR.string.on_boarding_p3_title_highlights).split(
                        ',',
                    ),
            )

            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .padding(top = 46.dp),
                verticalArrangement = Arrangement.spacedBy(19.dp),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(11.dp),
                    ) {
                        ToggleButton(
                            isSelected = state.isAllAgreed,
                            onSelect = event::onToggleAllAgreed,
                        )
                        Text(
                            style =
                                MooiTheme.typography.body4.copy(
                                    fontWeight = FontWeight.SemiBold,
                                ),
                            color = Color.White,
                            text = stringResource(tutorialR.string.on_boarding_p3_agree_all),
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(1.5.dp)
                                .background(MooiTheme.colorScheme.gray600),
                    )

                    TermItem(
                        term = stringResource(tutorialR.string.on_boarding_p3_agree_terms),
                        isSelected = state.isTermAgreed,
                        onSelect = event::onToggleTermAgreed,
                        showTermDetail = true,
                        onShowDetail = {
                            navToTermDetail()
                        },
                    )
                    TermItem(
                        term = stringResource(tutorialR.string.on_boarding_p3_agree_privacy),
                        isSelected = state.isPrivacyAgreed,
                        onSelect = event::onTogglePrivacyAgreed,
                        showTermDetail = true,
                        onShowDetail = {
                            navToPrivacyDetail()
                        },
                    )
                    TermItem(
                        term = stringResource(tutorialR.string.on_boarding_p3_agree_marketing),
                        isSelected = state.isMarketingAgreed,
                        onSelect = event::onToggleMarketingAgreed,
                        showTermDetail = true,
                        onShowDetail = {
                            navToMarketingDetail()
                        },
                    )
                    TermItem(
                        term = stringResource(tutorialR.string.on_boarding_p3_agree_age),
                        isSelected = state.isAgeAgreed,
                        onSelect = event::onToggleAgeAgreed,
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Text(
                        style =
                            MooiTheme.typography.caption7,
                        color = MooiTheme.colorScheme.primaryBlue500,
                        text = stringResource(tutorialR.string.on_boarding_p3_info1),
                    )
                    Spacer(modifier = Modifier.size(5.dp))
                    Text(
                        style =
                            MooiTheme.typography.caption7,
                        color = MooiTheme.colorScheme.primaryBlue500,
                        text = stringResource(tutorialR.string.on_boarding_p3_info2),
                    )
                }

                // todo: delete test navigatyion button
                Button(
                    onClick = {
                        navToSignupComplete()
                    },
                ) {
                    Text("회원가입 성공 화면 이동")
                }
            }

            CtaButton(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .padding(bottom = 39.dp),
                labelString = stringResource(tutorialR.string.on_boarding_p3_btn_signup),
                enabled = state.isSignupCompleteButtonEnabled,
                onClick = {
                    coroutineScope.launch {
                        onAgreeTermsInputComplete(
                            state.isAllAgreed,
                            state.isTermAgreed,
                            state.isPrivacyAgreed,
                            state.isMarketingAgreed,
                            state.isAgeAgreed,
                        )
                        onSignup()
                    }
                },
                isDefaultWidth = false,
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
    showTermDetail: Boolean = false,
    onShowDetail: () -> Unit = { },
) {
    Row(modifier = modifier.fillMaxWidth()) {
        ToggleButton(
            isSelected = isSelected,
            onSelect = onSelect,
        )
        Row(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(start = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                style = MooiTheme.typography.body5,
                color = Color.White,
                text = term,
            )
        }
        if (showTermDetail) {
            Image(
                modifier =
                    Modifier
                        .height(14.dp)
                        .width(8.dp)
                        .clickable {
                            onShowDetail()
                        },
                painter = painterResource(tutorialR.drawable.arrow_right),
                contentDescription = "show term detail",
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
            event =
                object : AgreeTermsEvent {
                    override fun resetAgreedTerms() {}

                    override fun onToggleAllAgreed() {}

                    override fun onToggleTermAgreed() {}

                    override fun onTogglePrivacyAgreed() {}

                    override fun onToggleMarketingAgreed() {}

                    override fun onToggleAgeAgreed() {}
                },
        )
    }
}
