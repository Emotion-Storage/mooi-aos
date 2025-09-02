package com.emotionstorage.tutorial.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.emotionstorage.domain.model.User.AuthProvider
import com.emotionstorage.tutorial.presentation.OnBoardingEvent
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
    val state by sharedViewModel.state.collectAsState()
    LaunchedEffect(provider, idToken) {
        sharedViewModel.onProviderIdTokenReceived(provider, idToken)
    }

    StatelessOnBoardingNavHost(
        state = state,
        event = sharedViewModel.event,
        modifier = modifier,
        navToHome = navToHome,
    )
}

@Composable
private fun StatelessOnBoardingNavHost(
    state: OnBoardingViewModel.State,
    event: OnBoardingEvent,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    navToHome: () -> Unit = {},
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
                        onNicknameInputComplete = event::onNicknameInputComplete,
                        navToGenderBirth = {
                            navController.navigate(OnBoardingRoute.GENDER_BIRTH.route)
                        },
                        navToBack = {
                            navController.popBackStack()
                        }
                    )

                    OnBoardingRoute.GENDER_BIRTH -> GenderBirthScreen(
                        nickname = state.signupForm.nickname ?: "",
                        onGenderBirthInputComplete = event::onGenderBirthInputComplete,
                        navToExpectations = {
                            navController.navigate(OnBoardingRoute.EXPECTATIONS.route)
                        },
                        navToBack = {
                            navController.popBackStack()
                        }
                    )

                    OnBoardingRoute.EXPECTATIONS -> ExpectationsScreen(
                        onExpectationsSelectComplete = event::onExpectationsSelectComplete,
                        navToAgreeTerms = {
                            navController.navigate(OnBoardingRoute.AGREE_TERMS.route)
                        },
                        navToBack = {
                            navController.popBackStack()
                        }
                    )

                    OnBoardingRoute.AGREE_TERMS -> AgreeTermsScreen(
                        onAgreeTermsInputComplete = event::onAgreeTermsInputComplete,
                        onSignup = event::onSignup,
                        signupState = state.signupState,
                        navToSignupComplete = {
                            navController.navigateWithClearStack(OnBoardingRoute.SIGNUP_COMPLETE.route)
                        },
                        navToBack = {
                            navController.popBackStack()
                        }
                    )

                    OnBoardingRoute.SIGNUP_COMPLETE -> SignupCompleteScreen(
                        navToHome = navToHome,
                        onLogin = event::onLogin,
                        loginState = state.loginState,
                    )
                }
            }
        }
    }
}