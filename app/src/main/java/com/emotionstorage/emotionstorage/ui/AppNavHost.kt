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
import com.emotionstorage.auth.ui.SignupCompleteScreen
import com.emotionstorage.domain.model.User.AuthProvider
import com.emotionstorage.home.ui.HomeScreen
import com.emotionstorage.my.ui.MyPageScreen
import com.emotionstorage.time_capsule.ui.ArrivedTimeCapsulesScreen
import com.emotionstorage.time_capsule.ui.CalendarScreen
import com.emotionstorage.time_capsule.ui.FavoriteTimeCapsulesScreen
import com.emotionstorage.time_capsule_detail.ui.TimeCapsuleDetailScreen
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
    data class SignupComplete(val provider: String, val idToken: String) : AppDestination()

    @Serializable
    object Home : AppDestination()

    @Serializable
    object TIME_CAPSULE_CALENDAR : AppDestination()

    @Serializable
    object MyPage : AppDestination()

    @Serializable
    data class AI_CHAT(val roomId: String) : AppDestination()

    @Serializable
    object ARRIVED_TIME_CAPSULES : AppDestination()

    @Serializable
    object FAVORITE_TIME_CAPSULES : AppDestination()

    @Serializable
    data class TIME_CAPSULE_DETAIL(val id: String) : AppDestination()
}

@Composable
internal fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            AppBottomNavBar(
                navController = navController, currentDestination = currentDestination
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
                    navToSignupComplete = { provider, idToken ->
                        navController.popBackStack()
                        navController.navigate(
                            AppDestination.SignupComplete(
                                provider.toString(), idToken
                            )
                        )
                    },
                    navToBack ={
                        navController.popBackStack()
                    }
                )
            }

            composable<AppDestination.SignupComplete> { backstackEntry ->
                val arguments = backstackEntry.toRoute<AppDestination.SignupComplete>()
                SignupCompleteScreen(
                    provider = AuthProvider.valueOf(arguments.provider),
                    idToken = arguments.idToken,
                    navToHome = {
                        navController.navigateWithClearStack(AppDestination.Home)
                    },
                    navToLogin = {
                        // pop back to nav to login, as signup complete screen is always on top of login
                        navController.popBackStack()
                    }
                )
            }

            composable<AppDestination.Home> {
                HomeScreen(
                    navToChat = { roomId ->
                        navController.navigate(AppDestination.AI_CHAT(roomId))
                    },
                    navToArrivedTimeCapsules = {
                        navController.navigate(AppDestination.ARRIVED_TIME_CAPSULES)
                    }
                )
            }
            composable<AppDestination.TIME_CAPSULE_CALENDAR> {
                CalendarScreen(
                    navToArrived = {
                        navController.navigate(AppDestination.ARRIVED_TIME_CAPSULES)
                    },
                    navToFavorites = {
                        navController.navigate(AppDestination.FAVORITE_TIME_CAPSULES)

                    }
                )
            }
            composable<AppDestination.MyPage> {
                MyPageScreen(
                    navToLogin = {
                        navController.navigateWithClearStack(AppDestination.Login)
                    }
                )
            }

            composable<AppDestination.AI_CHAT> { navBackStackEntry ->
                val arguments = navBackStackEntry.toRoute<AppDestination.AI_CHAT>()
                AIChatScreen(
                    roomId = arguments.roomId, navToBack = {
                        navController.popBackStack()
                    })
            }

            composable<AppDestination.ARRIVED_TIME_CAPSULES> {
                ArrivedTimeCapsulesScreen(
                    navToTimeCapsuleDetail = { id ->
                        navController.navigate(AppDestination.TIME_CAPSULE_DETAIL(id))
                    },
                    navToBack = {
                        navController.popBackStack()
                    })
            }

            composable<AppDestination.FAVORITE_TIME_CAPSULES> {
                FavoriteTimeCapsulesScreen(
                    navToTimeCapsuleDetail = { id ->
                        navController.navigate(AppDestination.TIME_CAPSULE_DETAIL(id))
                    },
                    navToBack = {
                        navController.popBackStack()
                    })
            }

            composable<AppDestination.TIME_CAPSULE_DETAIL> { navBackStackEntry ->
                val arguments = navBackStackEntry.toRoute<AppDestination.TIME_CAPSULE_DETAIL>()
                TimeCapsuleDetailScreen(id = arguments.id, navToBack = {
                    navController.popBackStack()
                })
            }
        }
    }
}
