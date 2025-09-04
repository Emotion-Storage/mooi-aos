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
import com.emotionstorage.tutorial.ui.onBoarding.SignupCompleteScreen
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.navigateWithClearStack

/**
 * On boarding destinations
 */
enum class OnBoardingRoute(
    val route: String
) {
    NICKNAME("on_boarding/nickname"),
    GENDER_BIRTH("on_boarding/gender_birth"),
    EXPECTATIONS("on_boarding/expectations"),
    AGREE_TERMS("on_boarding/agree_terms"),
    SIGNUP_COMPLETE("on_boarding/signup_complete"),
}

@Composable
fun OnBoardingNavHost(
    provider: AuthProvider,
    idToken: String,
    modifier: Modifier = Modifier,
    sharedViewModel: OnBoardingViewModel = hiltViewModel(),
    navToHome: () -> Unit = {},
) {
    val navController = rememberNavController()
    val state = sharedViewModel.container.stateFlow.collectAsState()

    LaunchedEffect(provider, idToken) {
        sharedViewModel.onAction(OnBoardingAction.Initiate(provider, idToken))
    }
    LaunchedEffect(Unit) {
        sharedViewModel.container.sideEffectFlow.collect { effect ->
            when (effect) {
                is OnBoardingSideEffect.SignupSuccess -> {
                    navController.navigateWithClearStack(OnBoardingRoute.SIGNUP_COMPLETE.route)
                }

                is OnBoardingSideEffect.SignupFailed -> {
                    // todo: handle signup failure
                }

                is OnBoardingSideEffect.LoginSuccess -> {
                    navToHome()
                }

                is OnBoardingSideEffect.LoginFailed -> {
                    navController.popBackStack()
                }
            }
        }
    }


    StatelessOnBoardingNavHost(
        navController = navController,
        state = state.value,
        onAction = sharedViewModel::onAction,
        modifier = modifier,
    )
}

@Composable
private fun StatelessOnBoardingNavHost(
    navController: NavHostController,
    state: OnBoardingState,
    onAction: (OnBoardingAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController,
        startDestination = OnBoardingRoute.NICKNAME.route,
        modifier = modifier
            .fillMaxSize()
            .background(MooiTheme.colorScheme.background)
    ) {
        OnBoardingRoute.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    OnBoardingRoute.NICKNAME -> NicknameScreen(
                        onNicknameInputComplete = { nickname ->
                            onAction(OnBoardingAction.InputNickname(nickname))
                        },
                        navToGenderBirth = {
                            navController.navigate(OnBoardingRoute.GENDER_BIRTH.route)
                        },
                        navToBack = {
                            navController.popBackStack()
                        }
                    )

                    OnBoardingRoute.GENDER_BIRTH -> GenderBirthScreen(
                        nickname = state.signupForm.nickname ?: "",
                        onGenderBirthInputComplete ={gender, birth ->
                            onAction(OnBoardingAction.InputGenderAndBirth(gender, birth))
                        },
                        navToExpectations = {
                            navController.navigate(OnBoardingRoute.EXPECTATIONS.route)
                        },
                        navToBack = {
                            navController.popBackStack()
                        }
                    )

                    OnBoardingRoute.EXPECTATIONS -> ExpectationsScreen(
                        onExpectationsSelectComplete ={ expectations ->
                            onAction(OnBoardingAction.InputExpectations(expectations))
                        },
                        navToAgreeTerms = {
                            navController.navigate(OnBoardingRoute.AGREE_TERMS.route)
                        },
                        navToBack = {
                            navController.popBackStack()
                        }
                    )

                    OnBoardingRoute.AGREE_TERMS -> AgreeTermsScreen(
                        onAgreeTermsInputComplete = {isTermAgreed, isPrivacyAgreed, isMarketingAgreed ->
                            onAction(OnBoardingAction.InputAgreedTerms(isTermAgreed, isPrivacyAgreed, isMarketingAgreed))
                        },
                        onSignup = {
                            onAction(OnBoardingAction.Signup)
                        },
                        navToBack = {
                            navController.popBackStack()
                        }
                    )

                    OnBoardingRoute.SIGNUP_COMPLETE -> SignupCompleteScreen(
                        onLogin = {
                            onAction(OnBoardingAction.Login)
                        },
                    )
                }
            }
        }
    }
}