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
    INPUT_NICKNAME("on_boarding/nickname"),
    INPUT_GENDER_BIRTH("on_boarding/gender_birth"),
    SELECT_EXPECTATIONS("on_boarding/expectations"),
    AGREE_TERMS("on_boarding/terms"),
    SIGNUP_COMPLETE("on_boarding/complete"),
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
        startDestination = OnBoardingRoute.INPUT_NICKNAME.route,
        modifier = modifier.fillMaxSize()
    ) {
        OnBoardingRoute.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    OnBoardingRoute.INPUT_NICKNAME -> InputNicknameScreen(
                        navToInputGenderBirth = { navController.navigate(OnBoardingRoute.INPUT_GENDER_BIRTH.route) }
                    )

                    OnBoardingRoute.INPUT_GENDER_BIRTH -> InputGenderBirthScreen(
                        navToSelectExpectations = { navController.navigate(OnBoardingRoute.SELECT_EXPECTATIONS.route) }
                    )

                    OnBoardingRoute.SELECT_EXPECTATIONS -> SelectExpectationsScreen(
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