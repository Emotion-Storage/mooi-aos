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
}

@Composable
fun OnBoardingNavHost(
    provider: AuthProvider,
    idToken: String,
    modifier: Modifier = Modifier,
    sharedViewModel: OnBoardingViewModel = hiltViewModel(),
    navToSignupComplete: (provider: AuthProvider, idToken: String) -> Unit = { _, _ -> },
) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Log.d("on boarding nav controller", "currentRoute: $currentRoute")

    val signupForm = sharedViewModel.signupForm.collectAsState()

    StatelessOnBoardingNavHost(
        provider = provider,
        idToken = idToken,
        navController = navController,
        signupForm = signupForm.value,
        event = sharedViewModel,
        modifier = modifier,
        navToSignupComplete = navToSignupComplete
    )
}

@Composable
private fun StatelessOnBoardingNavHost(
    provider: AuthProvider,
    idToken: String,
    navController: NavHostController,
    signupForm: SignupForm,
    event: OnBoardingEvent,
    modifier: Modifier = Modifier,
    navToSignupComplete: (provider: AuthProvider, idToken: String) -> Unit = { _, _ -> },
) {
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
                        onSignup = {
                            event.onSignup(provider, idToken)
                        },
                        navToSignupComplete = {
                            navToSignupComplete(provider, idToken)
                        }
                    )
                }
            }
        }
    }
}