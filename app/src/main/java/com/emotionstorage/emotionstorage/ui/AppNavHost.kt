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
import com.emotionstorage.tutorial.ui.OnBoardingNavHost
import com.emotionstorage.tutorial.ui.SplashScreen
import com.emotionstorage.tutorial.ui.tutorial.TutorialScreen
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.navigateWithClearStack
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
    sealed class Main : AppDestination() {
        @Serializable
        object Home : Main()

        @Serializable
        object TIME_CAPSULE_CALENDAR : Main()

        @Serializable
        object My : Main()
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
                navToMain = {
                    navController.navigateWithClearStack(AppDestination.Main)
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
                navToMain = {
                    navController.navigateWithClearStack(AppDestination.Main)
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
                navToMain = {
                    navController.navigateWithClearStack(AppDestination.Main)
                },
            )
        }

        composable<AppDestination.Main> { backstackEntry ->
            MainNavHost()
        }
    }
}
