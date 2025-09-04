package com.emotionstorage.emotionstorage.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.emotionstorage.ai_chat.ui.AIChatScreen
import com.emotionstorage.auth.ui.LoginScreen
import com.emotionstorage.domain.model.User.AuthProvider
import com.emotionstorage.home.ui.HomeScreen
import com.emotionstorage.my.ui.MyScreen
import com.emotionstorage.time_capsule.ui.TimeCapsuleCalendarScreen
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
    object Home : AppDestination()

    @Serializable
    object TIME_CAPSULE_CALENDAR : AppDestination()

    @Serializable
    object My : AppDestination()

    @Serializable
    data class AI_CHAT(val roomId: String) : AppDestination()
}

@Composable
internal fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    Scaffold(
        modifier,
        bottomBar = {
            AppBottomNavBar(
                navController = navController,
                currentDestination = currentDestination
            )
        }) { innerPadding ->
        NavHost(
            navController,
            startDestination = AppDestination.Splash,
            modifier = Modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            composable<AppDestination.Splash> { backstackEntry ->
                SplashScreen(navToTutorial = {
                    navController.navigateWithClearStack(AppDestination.Tutorial)
                }, navToHome = {
                    navController.navigateWithClearStack(AppDestination.Home)
                })
            }

            composable<AppDestination.Tutorial> { backstackEntry ->
                TutorialScreen(
                    navToLogin = {
                        navController.navigateWithClearStack(AppDestination.Login)
                    })
            }

            composable<AppDestination.Login> { backstackEntry ->
                LoginScreen(navToHome = {
                    navController.navigateWithClearStack(AppDestination.Home)
                }, navToOnBoarding = { provider, idToken ->
                    navController.navigate(
                        AppDestination.OnBoarding(
                            provider.toString(), idToken
                        )
                    )
                })
            }

            composable<AppDestination.OnBoarding> { backstackEntry ->
                val arguments = backstackEntry.toRoute<AppDestination.OnBoarding>()
                OnBoardingNavHost(
                    idToken = arguments.idToken,
                    provider = AuthProvider.valueOf(arguments.provider),
                    navToHome = {
                        navController.navigateWithClearStack(AppDestination.Home)
                    },
                )
            }

            composable<AppDestination.Home> {
                HomeScreen(
                    navToChat = { roomId ->
                        navController.navigate(AppDestination.AI_CHAT(roomId))
                    }
                )
            }
            composable<AppDestination.TIME_CAPSULE_CALENDAR> {
                TimeCapsuleCalendarScreen()
            }
            composable<AppDestination.My> {
                MyScreen()
            }

            composable<AppDestination.AI_CHAT> { navBackStackEntry ->
                val arguments = navBackStackEntry.toRoute<AppDestination.AI_CHAT>()
                AIChatScreen(
                    roomId = arguments.roomId,
                    navToBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
