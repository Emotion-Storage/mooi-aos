package com.emotionstorage.emotionstorage.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import com.emotionstorage.auth.ui.LoginScreen
import com.emotionstorage.domain.model.User.AuthProvider
import com.emotionstorage.home.ui.HomeScreen
import com.emotionstorage.tutorial.ui.onBoarding.OnBoardingNavHost
import com.emotionstorage.tutorial.ui.splash.SplashScreen
import com.emotionstorage.tutorial.ui.tutorial.TutorialScreen
import com.emotionstorage.ui.theme.MooiTheme
import kotlinx.serialization.Serializable


/**
 * App destinations
 */
@Serializable
internal sealed class AppDestination {
    @Serializable
    object Splash : AppDestination()

    @Serializable
    object Tutorial : AppDestination()

    @Serializable
    object Login : AppDestination()

    @Serializable
    data class OnBoarding(val provider: String, val idToken: String) : AppDestination()

    @Serializable
    object Home : AppDestination()
}

// clear stack, including splash
internal fun NavOptionsBuilder.clearStack(){
    popUpTo(AppDestination.Splash) {
        inclusive = true
    }
}

@Composable
internal fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    val navToHome = {
        navController.navigate(AppDestination.Home) {
            // always clear back stack when navigating to home
            clearStack()
        }
    }


    NavHost(
        navController,
        startDestination = AppDestination.Splash,
        modifier = modifier
            .fillMaxSize()
            .background(MooiTheme.colorScheme.background)
    ) {
        composable<AppDestination.Splash> { backstackEntry ->
            SplashScreen(
                navToTutorial = {
                    navController.navigate(AppDestination.Tutorial) {
                        clearStack()
                    }
                },
                navToHome = navToHome
            )
        }

        composable<AppDestination.Tutorial> { backstackEntry ->
            TutorialScreen(
                navToLogin = {
                    navController.navigate(AppDestination.Login)
                }
            )
        }

        composable<AppDestination.Login> { backstackEntry ->
            LoginScreen(
                navToHome = navToHome,
                navToOnBoarding = { provider, idToken ->
                    navController.navigate(AppDestination.OnBoarding(provider.toString(), idToken))
                }
            )
        }

        composable<AppDestination.OnBoarding> { backstackEntry ->
            val arguments = backstackEntry.toRoute<AppDestination.OnBoarding>()
            OnBoardingNavHost(
                navToLogin = {
                    navController.navigate(AppDestination.Login)
                },
                navToHome = navToHome,
                idToken = arguments.idToken,
                provider = AuthProvider.valueOf(arguments.provider)
            )
        }

        composable<AppDestination.Home> { backstackEntry ->
            HomeScreen()
        }
    }
}