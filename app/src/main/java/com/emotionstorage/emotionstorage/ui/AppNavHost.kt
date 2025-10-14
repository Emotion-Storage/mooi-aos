package com.emotionstorage.emotionstorage.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.emotionstorage.ai_chat.presentation.AiChatIntroViewModel
import com.emotionstorage.ai_chat.ui.AIChatDescriptionScreen
import com.emotionstorage.ai_chat.ui.AIChatScreen
import com.emotionstorage.alarm.ui.PushNotificationScreen
import com.emotionstorage.auth.ui.LoginScreen
import com.emotionstorage.auth.ui.SignupCompleteScreen
import com.emotionstorage.daily_report.ui.DailyReportDetailScreen
import com.emotionstorage.domain.model.User.AuthProvider
import com.emotionstorage.home.ui.HomeScreen
import com.emotionstorage.my.ui.AccountInfoScreen
import com.emotionstorage.my.ui.KeyDescriptionScreen
import com.emotionstorage.my.ui.MyPageScreen
import com.emotionstorage.my.ui.NicknameChangeScreen
import com.emotionstorage.my.ui.NotificationSettingScreen
import com.emotionstorage.my.ui.TermsAndPrivacyScreen
import com.emotionstorage.my.ui.WithDrawNoticeScreen
import com.emotionstorage.time_capsule.ui.ArrivedTimeCapsulesScreen
import com.emotionstorage.time_capsule.ui.CalendarScreen
import com.emotionstorage.time_capsule.ui.FavoriteTimeCapsulesScreen
import com.emotionstorage.time_capsule_detail.ui.SaveTimeCapsuleScreen
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
    data class OnBoarding(
        val provider: String,
        val idToken: String,
    ) : AppDestination()

    @Serializable
    data class SignupComplete(
        val provider: String,
        val idToken: String,
    ) : AppDestination()

    @Serializable
    object Home : AppDestination()

    @Serializable
    object TimeCapsuleCalendar : AppDestination()

    @Serializable
    object MyPage : AppDestination()

    @Serializable
    data class AIChat(
        val roomId: String,
    ) : AppDestination()

    @Serializable
    data class AIChatDesc(
        val roomId: String,
    ) : AppDestination()

    @Serializable
    object ArrivedTimeCapsules : AppDestination()

    @Serializable
    object FavoriteTimeCapsules : AppDestination()

    @Serializable
    data class TimeCapsuleDetail(
        val id: String,
        val isNewTimeCapsule: Boolean,
    ) : AppDestination()

    @Serializable
    data class SaveTimeCapsule(
        val id: String,
        val isNewTimeCapsule: Boolean,
    ) : AppDestination()

    @Serializable
    data class DailyReportDetail(
        val id: String,
    ) : AppDestination()

    @Serializable
    object TermsAndPrivacy : AppDestination()

    @Serializable
    object WithDrawNotice : AppDestination()

    @Serializable
    object NicknameChange : AppDestination()

    @Serializable
    object AccountInfo : AppDestination()

    @Serializable
    object KeyDescription : AppDestination()

    @Serializable
    object NotificationSetting : AppDestination()

    @Serializable
    object PushNotification : AppDestination()
}

@Composable
internal fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    val aiChatIntroViewModel: AiChatIntroViewModel = hiltViewModel()
    val introSeen = aiChatIntroViewModel.introSeen.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            AppBottomNavBar(
                navController = navController,
                currentDestination = currentDestination,
            )
        },
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = AppDestination.Splash,
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background)
                    .padding(innerPadding),
        ) {
            composable<AppDestination.Splash> { backstackEntry ->
                SplashScreen(
                    navToTutorial = {
                        navController.navigateWithClearStack(AppDestination.Tutorial)
                    },
                    navToHome = {
                        navController.navigateWithClearStack(AppDestination.Home)
                    },
                )
            }

            composable<AppDestination.Tutorial> { backstackEntry ->
                TutorialScreen(
                    navToLogin = {
                        navController.navigateWithClearStack(AppDestination.Login)
                    },
                )
            }

            composable<AppDestination.Login> { backstackEntry ->
                LoginScreen(
                    navToHome = {
                        navController.navigateWithClearStack(AppDestination.Home)
                    },
                    navToOnBoarding = { provider, idToken ->
                        navController.navigate(
                            AppDestination.OnBoarding(
                                provider.toString(),
                                idToken,
                            ),
                        )
                    },
                )
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
                                provider.toString(),
                                idToken,
                            ),
                        )
                    },
                    navToBack = {
                        navController.popBackStack()
                    },
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
                    },
                )
            }

            composable<AppDestination.Home> {
                HomeScreen(
                    navToChat = { roomId ->
                        // DataStore 의 값에 따라 분기 처리
                        val seen = introSeen.value
                        if (seen) {
                            navController.navigate(AppDestination.AIChat(roomId))
                        } else {
                            navController.navigate(AppDestination.AIChatDesc(roomId))
                        }
                    },
                    navToArrivedTimeCapsules = {
                        navController.navigate(AppDestination.ArrivedTimeCapsules)
                    },
                    navToAlarm = {
                        navController.navigate(AppDestination.PushNotification)
                    }
                )
            }
            composable<AppDestination.TimeCapsuleCalendar> {
                CalendarScreen(
                    navToKey = {
                        navController.navigate(AppDestination.KeyDescription)
                    },
                    navToArrived = {
                        navController.navigate(AppDestination.ArrivedTimeCapsules)
                    },
                    navToFavorites = {
                        navController.navigate(AppDestination.FavoriteTimeCapsules)
                    },
                    navToTimeCapsuleDetail = { id ->
                        navController.navigate(AppDestination.TimeCapsuleDetail(id, isNewTimeCapsule = false))
                    },
                    navToDailyReportDetail = { id ->
                        navController.navigate(AppDestination.DailyReportDetail(id))
                    },
                    navToAIChat = { roomId ->
                        navController.navigate(AppDestination.AIChat(roomId))
                    },
                )
            }
            composable<AppDestination.MyPage> {
                MyPageScreen(
                    navToLogin = {
                        navController.navigateWithClearStack(AppDestination.Login)
                    },
                    navToWithdrawNotice = {
                        navController.navigate(AppDestination.WithDrawNotice)
                    },
                    navToNickNameChange = {
                        navController.navigate(AppDestination.NicknameChange)
                    },
                    navToAccountInfo = {
                        navController.navigate(AppDestination.AccountInfo)
                    },
                    navToKeyDescription = {
                        navController.navigate(AppDestination.KeyDescription)
                    },
                    navToTermsAndPrivacy = {
                        navController.navigate(AppDestination.TermsAndPrivacy)
                    },
                    navToNotificationSetting = {
                        navController.navigate(AppDestination.NotificationSetting)
                    },
                )
            }

            composable<AppDestination.AIChat> { navBackStackEntry ->
                val arguments = navBackStackEntry.toRoute<AppDestination.AIChat>()
                AIChatScreen(
                    roomId = arguments.roomId,
                    navToTimeCapsuleDetail = { id ->
                        navController.navigate(AppDestination.TimeCapsuleDetail(id, isNewTimeCapsule = true))
                    },
                    navToBack = {
                        navController.popBackStack()
                    },
                )
            }

            composable<AppDestination.AIChatDesc> { navBackStackEntry ->
                val arguments = navBackStackEntry.toRoute<AppDestination.AIChatDesc>()
                AIChatDescriptionScreen(
                    roomId = arguments.roomId,
                    onCheckboxChanged = { checked ->
                        aiChatIntroViewModel.onIntroSeenChanged(checked)
                    },
                    onStartChat = { roomId ->
                        navController.navigateWithClearStack(AppDestination.AIChat(roomId))
                    },
                )
            }

            composable<AppDestination.ArrivedTimeCapsules> {
                ArrivedTimeCapsulesScreen(
                    navToTimeCapsuleDetail = { id ->
                        navController.navigate(AppDestination.TimeCapsuleDetail(id, isNewTimeCapsule = false))
                    },
                    navToBack = {
                        navController.popBackStack()
                    },
                )
            }

            composable<AppDestination.FavoriteTimeCapsules> {
                FavoriteTimeCapsulesScreen(
                    navToTimeCapsuleDetail = { id ->
                        navController.navigate(AppDestination.TimeCapsuleDetail(id, isNewTimeCapsule = false))
                    },
                    navToBack = {
                        navController.popBackStack()
                    },
                )
            }

            composable<AppDestination.TimeCapsuleDetail> { navBackStackEntry ->
                val arguments = navBackStackEntry.toRoute<AppDestination.TimeCapsuleDetail>()
                TimeCapsuleDetailScreen(
                    id = arguments.id,
                    isNewTimeCapsule = arguments.isNewTimeCapsule,
                    navToHome = {
                        navController.navigateWithClearStack(AppDestination.Home)
                    },
                    navToSaveTimeCapsule = {
                        navController.navigate(
                            AppDestination.SaveTimeCapsule(
                                arguments.id,
                                arguments.isNewTimeCapsule,
                            ),
                        )
                    },
                    navToBack = {
                        navController.popBackStack()
                    },
                )
            }

            composable<AppDestination.SaveTimeCapsule> { navBackStackEntry ->
                val arguments = navBackStackEntry.toRoute<AppDestination.SaveTimeCapsule>()
                SaveTimeCapsuleScreen(
                    id = arguments.id,
                    isNewTimeCapsule = arguments.isNewTimeCapsule,
                    navToMain = {
                        navController.navigateWithClearStack(AppDestination.Home)
                    },
                    navToPrevious = {
                        // pop twice, to navigate to previous screen
                        navController.popBackStack()
                        navController.popBackStack()
                    },
                    navToBack = {
                        navController.popBackStack()
                    },
                )
            }

            composable<AppDestination.DailyReportDetail> { navBackStackEntry ->
                val arguments = navBackStackEntry.toRoute<AppDestination.DailyReportDetail>()
                DailyReportDetailScreen(
                    id = arguments.id,
                    navToBack = {
                        navController.popBackStack()
                    },
                )
            }

            composable<AppDestination.TermsAndPrivacy> { navBackStackEntry ->
                TermsAndPrivacyScreen(
                    navToBack = {
                        navController.popBackStack()
                    },
                )
            }

            composable<AppDestination.WithDrawNotice> {
                WithDrawNoticeScreen(
                    navToBack = {
                        navController.popBackStack()
                    },
                    navToSplash = {
                        navController.navigateWithClearStack(AppDestination.Splash)
                    },
                    navToNotificationSetting = {
                        navController.navigateWithClearStack(AppDestination.NotificationSetting)
                    },
                )
            }

            composable<AppDestination.NicknameChange> {
                NicknameChangeScreen(
                    navToBack = {
                        navController.popBackStack()
                    },
                )
            }

            composable<AppDestination.AccountInfo> {
                AccountInfoScreen(
                    navToBack = {
                        navController.popBackStack()
                    },
                )
            }

            composable<AppDestination.KeyDescription> {
                KeyDescriptionScreen(
                    navToBack = {
                        navController.popBackStack()
                    },
                )
            }

            composable<AppDestination.NotificationSetting> {
                NotificationSettingScreen(
                    navToBack = {
                        navController.popBackStack()
                    },
                )
            }

            composable<AppDestination.PushNotification> {
                PushNotificationScreen(
                    navToBack = {
                        navController.popBackStack()
                    },
                )
            }
        }
    }
}
