package com.emotionstorage.tutorial.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.emotionstorage.domain.model.User.AuthProvider
import com.emotionstorage.tutorial.presentation.OnBoardingAction
import com.emotionstorage.tutorial.presentation.OnBoardingSideEffect
import com.emotionstorage.tutorial.presentation.OnBoardingState
import com.emotionstorage.tutorial.presentation.OnBoardingViewModel
import com.emotionstorage.tutorial.ui.onBoarding.AgreeTermsScreen
import com.emotionstorage.tutorial.ui.onBoarding.ExpectationsScreen
import com.emotionstorage.tutorial.ui.onBoarding.GenderBirthScreen
import com.emotionstorage.tutorial.ui.onBoarding.NicknameScreen
import com.emotionstorage.ui.theme.MooiTheme

/**
 * On boarding destinations
 */
enum class OnBoardingRoute(
    val route: String,
) {
    NICKNAME("on_boarding/nickname"),
    GENDER_BIRTH("on_boarding/gender_birth"),
    EXPECTATIONS("on_boarding/expectations"),
    AGREE_TERMS("on_boarding/agree_terms"),
}

@Composable
fun OnBoardingNavHost(
    provider: AuthProvider,
    idToken: String,
    modifier: Modifier = Modifier,
    sharedViewModel: OnBoardingViewModel = hiltViewModel(),
    navToSignupComplete: (provider: AuthProvider, idToken: String) -> Unit = { _, _ -> },
    navToBack: () -> Unit = {},
) {
    val navController = rememberNavController()
    val state = sharedViewModel.container.stateFlow.collectAsState()

    LaunchedEffect(provider, idToken) {
        sharedViewModel.onAction(OnBoardingAction.Initiate(provider, idToken))
    }
    LaunchedEffect(Unit) {
        sharedViewModel.container.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is OnBoardingSideEffect.SignupSuccess -> {
                    navToSignupComplete(sideEffect.provider, sideEffect.idToken)
                }

                is OnBoardingSideEffect.SignupFailed -> {
                    // todo: handle signup failure
                }
            }
        }
    }

    StatelessOnBoardingNavHost(
        modifier = modifier,
        navController = navController,
        state = state.value,
        onAction = sharedViewModel::onAction,
        navToBack = navToBack,
        navToSignupComplete = navToSignupComplete,
    )
}

@Composable
private fun StatelessOnBoardingNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    state: OnBoardingState = OnBoardingState(),
    onAction: (OnBoardingAction) -> Unit = {},
    navToBack: () -> Unit = {},
    // todo: delete test navigation
    navToSignupComplete: (provider: AuthProvider, idToken: String) -> Unit = { _, _ -> },
) {
    NavHost(
        navController,
        startDestination = OnBoardingRoute.NICKNAME.route,
        modifier =
            modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background),
    ) {
        OnBoardingRoute.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    OnBoardingRoute.NICKNAME -> {
                        NicknameScreen(
                            onNicknameInputComplete = { nickname ->
                                onAction(OnBoardingAction.InputNickname(nickname))
                            },
                            navToGenderBirth = {
                                navController.navigate(OnBoardingRoute.GENDER_BIRTH.route)
                            },
                            navToBack = {
                                // call navToBack() instead of navController.popBackStack(), to exit from on boarding nav controller
                                navToBack()
                            },
                        )
                    }

                    OnBoardingRoute.GENDER_BIRTH -> {
                        GenderBirthScreen(
                            nickname = state.signupForm.nickname ?: "",
                            onGenderBirthInputComplete = { gender, birth ->
                                onAction(OnBoardingAction.InputGenderAndBirth(gender, birth))
                            },
                            navToExpectations = {
                                navController.navigate(OnBoardingRoute.EXPECTATIONS.route)
                            },
                            navToBack = {
                                navController.popBackStack()
                            },
                        )
                    }

                    OnBoardingRoute.EXPECTATIONS -> {
                        ExpectationsScreen(
                            onExpectationsSelectComplete = { expectations ->
                                onAction(OnBoardingAction.InputExpectations(expectations))
                            },
                            navToAgreeTerms = {
                                navController.navigate(OnBoardingRoute.AGREE_TERMS.route)
                            },
                            navToBack = {
                                navController.popBackStack()
                            },
                        )
                    }

                    OnBoardingRoute.AGREE_TERMS -> {
                        AgreeTermsScreen(
                            onAgreeTermsInputComplete = { isTermAgreed, isPrivacyAgreed, isMarketingAgreed ->
                                onAction(
                                    OnBoardingAction.InputAgreedTerms(
                                        isTermAgreed,
                                        isPrivacyAgreed,
                                        isMarketingAgreed,
                                    ),
                                )
                            },
                            onSignup = {
                                onAction(OnBoardingAction.Signup)
                            },
                            navToBack = {
                                navController.popBackStack()
                            },
                            navToSignupComplete = {
                                navToSignupComplete(AuthProvider.KAKAO, "")
                            },
                        )
                    }
                }
            }
        }
    }
}
