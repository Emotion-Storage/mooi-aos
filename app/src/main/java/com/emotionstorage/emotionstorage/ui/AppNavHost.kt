package com.emotionstorage.emotionstorage.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

// navigate to destination with clear stack
internal fun <T : Any> NavHostController.navigateWithClearStack(destRoute: T) {
    val currentRoute = currentBackStackEntry?.destination?.route

    this.navigate(destRoute) {
        popUpTo(currentRoute ?: destRoute.toString()) {
            inclusive = true
        }
    }
}

@Composable
internal fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
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
                    navController.navigateWithClearStack(AppDestination.Tutorial)
                },
                navToHome = {
                    navController.navigateWithClearStack(AppDestination.Home)
                }
            )
        }

        composable<AppDestination.Tutorial> { backstackEntry ->
            TutorialScreen(
                navToLogin = {
                    navController.navigateWithClearStack(AppDestination.Login)
                }
            )
        }

        composable<AppDestination.Login> { backstackEntry ->
            LoginScreen(
                navToHome =  {
                    navController.navigateWithClearStack(AppDestination.Home)
                },
                navToOnBoarding = { provider, idToken ->
                    navController.navigate(AppDestination.OnBoarding(provider.toString(), idToken))
                }
            )
        }

        composable<AppDestination.OnBoarding> { backstackEntry ->
            val arguments = backstackEntry.toRoute<AppDestination.OnBoarding>()
            OnBoardingNavHost(
                idToken = arguments.idToken,
                provider = AuthProvider.valueOf(arguments.provider),
                navToLogin = {
                    navController.navigateWithClearStack(AppDestination.Login)
                },
                navToHome =  {
                    navController.navigateWithClearStack(AppDestination.Home)
                },
            )
        }

        composable<AppDestination.Home> { backstackEntry ->
            HomeScreen()
        }
    }
}