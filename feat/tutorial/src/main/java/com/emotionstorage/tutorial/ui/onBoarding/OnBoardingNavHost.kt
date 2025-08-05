package com.emotionstorage.tutorial.ui.onBoarding

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

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
    navToMain: () -> Unit = {},
) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Log.d("on boarding nav controller", "currentRoute: $currentRoute")

    NavHost(
        navController,
        startDestination = OnBoardingRoute.NICKNAME.route,
        modifier = modifier.fillMaxSize()
    ) {
        OnBoardingRoute.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    OnBoardingRoute.NICKNAME -> NicknameScreen(
                        navToGenderBirth = { navController.navigate(OnBoardingRoute.GENDER_BIRTH.route) }
                    )

                    OnBoardingRoute.GENDER_BIRTH -> GenderBirthScreen(
                        navToExpectations = { navController.navigate(OnBoardingRoute.EXPECTATIONS.route) }
                    )

                    OnBoardingRoute.EXPECTATIONS -> ExpectationsScreen(
                        navToAgreeTerms = { navController.navigate(OnBoardingRoute.AGREE_TERMS.route) }
                    )

                    OnBoardingRoute.AGREE_TERMS -> AgreeTermsScreen(
                        navToSignupComplete = { navController.navigate(OnBoardingRoute.SIGNUP_COMPLETE.route) }
                    )

                    OnBoardingRoute.SIGNUP_COMPLETE -> SignupCompleteScreen(
                        navToMain = navToMain
                    )
                }
            }
        }
    }
}