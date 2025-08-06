package com.emotionstorage.tutorial.ui.onBoarding

import android.util.Log
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.emotionstorage.auth.domain.model.SignupForm
import com.emotionstorage.domain.model.User.AuthProvider
import com.emotionstorage.tutorial.presentation.onBoarding.OnBoardingEvent
import com.emotionstorage.tutorial.presentation.onBoarding.OnBoardingViewModel

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
    modifier: Modifier = Modifier,
    sharedViewModel: OnBoardingViewModel = hiltViewModel(),
    navToMain: () -> Unit = {},
    navToLogin: () -> Unit = {}
) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Log.d("on boarding nav controller", "currentRoute: $currentRoute")

    val signupForm = sharedViewModel.signupForm.collectAsState()

    StatelessOnBoardingNavHost(
        navController = navController,
        signupForm = signupForm.value,
        event = sharedViewModel,
        modifier = modifier,
        navToMain = navToMain,
        navToLogin = navToLogin
    )
}

@Composable
private fun StatelessOnBoardingNavHost(
    navController: NavHostController,
    signupForm: SignupForm,
    event: OnBoardingEvent,
    modifier: Modifier = Modifier,
    navToMain: () -> Unit = {},
    navToLogin: () -> Unit = {}
) {
    // todo receive provider & id token from nav
    val provider = AuthProvider.GOOGLE
    val idToken = ""
    LaunchedEffect(provider, idToken) {
        event.onProviderIdTokenReceived(provider, idToken)
    }

    NavHost(
        navController,
        startDestination = OnBoardingRoute.NICKNAME.route,
        modifier = modifier.fillMaxSize()
    ) {
        OnBoardingRoute.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    OnBoardingRoute.NICKNAME -> NicknameScreen(
                        onNicknameInputComplete = event::onNicknameInputComplete,
                        navToGenderBirth = {
                            navController.navigate(OnBoardingRoute.GENDER_BIRTH.route)
                        }
                    )

                    OnBoardingRoute.GENDER_BIRTH -> GenderBirthScreen(
                        nickname = signupForm.nickname ?: "",
                        onGenderBirthInputComplete = event::onGenderBirthInputComplete,
                        navToExpectations = {
                            navController.navigate(OnBoardingRoute.EXPECTATIONS.route)
                        }
                    )

                    OnBoardingRoute.EXPECTATIONS -> ExpectationsScreen(
                        onExpectationsSelectComplete = event::onExpectationsSelectComplete,
                        navToAgreeTerms = {
                            navController.navigate(OnBoardingRoute.AGREE_TERMS.route)
                        }
                    )

                    OnBoardingRoute.AGREE_TERMS -> AgreeTermsScreen(
                        onAgreeTermsInputComplete = event::onAgreeTermsInputComplete,
                        onSignup = event::onSignup,
                        navToSignupComplete = {
                            // todo: 회원가입 완료 화면에서 뒤로가기 시, 로그인 화면으로 이동하도록 백스택 비우기
                            navController.navigate(OnBoardingRoute.SIGNUP_COMPLETE.route)
                        }
                    )

                    OnBoardingRoute.SIGNUP_COMPLETE -> SignupCompleteScreen(
                        onLogin = event::onLogin,
                        navToMain = navToMain,
                        navToLogin = navToLogin
                    )
                }
            }
        }
    }
}