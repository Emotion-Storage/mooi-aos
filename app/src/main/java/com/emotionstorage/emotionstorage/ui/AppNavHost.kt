package com.emotionstorage.emotionstorage.ui

import android.content.Context
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.emotionstorage.ai_chat.presentation.AiChatIntroViewModel
import com.emotionstorage.ai_chat.ui.AIChatDescriptionScreen
import com.emotionstorage.ai_chat.ui.AIChatScreen
import com.emotionstorage.alarm.ui.PushNotificationScreen
import com.emotionstorage.auth.ui.LoginScreen
import com.emotionstorage.auth.ui.SignupCompleteScreen
import com.emotionstorage.daily_report.ui.DailyReportDetailScreen
import com.emotionstorage.domain.model.ChatEntry
import com.emotionstorage.domain.model.User.AuthProvider
import com.emotionstorage.home.ui.HomeScreen
import com.emotionstorage.my.ui.NicknameChangeScreen
import com.emotionstorage.my.ui.TermsAndPrivacyScreen
import com.emotionstorage.my.ui.accountInfo.AccountInfoScreen
import com.emotionstorage.my.ui.keyDescription.KeyDescriptionScreen
import com.emotionstorage.my.ui.myPage.MyPageScreen
import com.emotionstorage.my.ui.notificationSettings.NotificationSettingScreen
import com.emotionstorage.my.ui.withdraw.WithDrawNoticeScreen
import com.emotionstorage.time_capsule.ui.ArrivedTimeCapsulesScreen
import com.emotionstorage.time_capsule.ui.CalendarScreen
import com.emotionstorage.time_capsule.ui.FavoriteTimeCapsulesScreen
import com.emotionstorage.time_capsule_detail.ui.SaveTimeCapsuleScreen
import com.emotionstorage.time_capsule_detail.ui.TimeCapsuleDetailScreen
import com.emotionstorage.tutorial.ui.OnBoardingNavHost
import com.emotionstorage.tutorial.ui.SplashScreen
import com.emotionstorage.tutorial.ui.TutorialScreen
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.appBar.BottomAppNavBar
import com.emotionstorage.ui.component.appBar.BottomNavDest
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.navigateAsRoot
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
        val roomId: Long,
    ) : AppDestination()

    @Serializable
    data class AIChatDesc(
        val roomId: Long,
    ) : AppDestination()

    @Serializable
    object ArrivedTimeCapsules : AppDestination()

    @Serializable
    object FavoriteTimeCapsules : AppDestination()

    @Serializable
    data class TimeCapsuleDetail(
        val id: Long,
        val isNewTimeCapsule: Boolean,
    ) : AppDestination()

    @Serializable
    data class SaveTimeCapsule(
        val id: Long,
        val isNewTimeCapsule: Boolean,
    ) : AppDestination()

    @Serializable
    data class DailyReportDetail(
        val id: Long,
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
    getGoogleIdToken: suspend () -> String,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    aiChatIntroViewModel: AiChatIntroViewModel = hiltViewModel(),
) {
    val introSeen = aiChatIntroViewModel.introSeen.collectAsState()

    val bottomAppBar: @Composable () -> Unit = {
        BottomAppNavBar(
            navController = navController,
            currentDestination = navController.currentBackStackEntry?.destination,
            bottomNavDestinations =
                listOf(
                    BottomNavDest(AppDestination.Home::class.qualifiedName!!, R.drawable.nav_ic_home, "홈 화면"),
                    BottomNavDest(
                        AppDestination.TimeCapsuleCalendar::class.qualifiedName!!,
                        R.drawable.nav_ic_calendar,
                        "감정 보관함",
                    ),
                    BottomNavDest(AppDestination.MyPage::class.qualifiedName!!, R.drawable.nav_ic_my, "내 페이지"),
                ),
        )
    }

    NavHost(
        navController,
        startDestination = AppDestination.Splash,
        modifier =
            modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.backgroundDefault),
    ) {
        composable<AppDestination.Splash>(
            enterTransition = {
                fadeIn(
                    animationSpec = tween(durationMillis = 800),
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(durationMillis = 800),
                )
            },
        ) { backstackEntry ->
            SplashScreen(
                navToTutorial = {
                    navController.navigateWithClearStack(AppDestination.Tutorial)
                },
                navToHome = {
                    navController.navigateWithClearStack(AppDestination.Home)
                },
            )
        }

        composable<AppDestination.Tutorial>(
            enterTransition = {
                fadeIn(
                    animationSpec = tween(durationMillis = 800),
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(durationMillis = 800),
                )
            },
        ) { backstackEntry ->
            TutorialScreen(
                navToLogin = {
                    navController.navigateWithClearStack(AppDestination.Login)
                },
            )
        }

        composable<AppDestination.Login> { backstackEntry ->
            LoginScreen(
                getGoogleIdToken = getGoogleIdToken,
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
                bottomAppBar = bottomAppBar,
                navToChat = { roomId, entry ->
                    when (entry) {
                        ChatEntry.Resume -> {
                            navController.navigate(AppDestination.AIChat(roomId))
                        }

                        ChatEntry.New -> {
                            val seen = introSeen.value
                            if (seen) {
                                navController.navigate(AppDestination.AIChat(roomId))
                            } else {
                                navController.navigate(AppDestination.AIChatDesc(roomId))
                            }
                        }
                    }
                },
                navToKey = {
                    navController.navigate(AppDestination.KeyDescription)
                },
                navToArrivedTimeCapsules = {
                    navController.navigate(AppDestination.ArrivedTimeCapsules)
                },
                navToAlarm = {
                    navController.navigate(AppDestination.PushNotification)
                },
                navToDailyReport = { id ->
                    navController.navigate(AppDestination.DailyReportDetail(id))
                },
                navToLogin = {
                    navController.navigateAsRoot(AppDestination.Login)
                },
            )
        }
        composable<AppDestination.TimeCapsuleCalendar> {
            CalendarScreen(
                bottomAppBar = bottomAppBar,
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
                navToLogin = {
                    navController.navigateAsRoot(AppDestination.Login)
                },
            )
        }
        composable<AppDestination.MyPage> {
            MyPageScreen(
                bottomAppBar = bottomAppBar,
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
                /* navToNotificationSetting = {
                     navController.navigate(AppDestination.NotificationSetting)
                 },*/
            )
        }

        composable<AppDestination.AIChat> { navBackStackEntry ->
            val arguments = navBackStackEntry.toRoute<AppDestination.AIChat>()
            AIChatScreen(
                roomId = arguments.roomId,
                navToTimeCapsuleDetail = { id ->
                    navController.navigateWithClearStack(AppDestination.TimeCapsuleDetail(id, isNewTimeCapsule = true))
                },
                navToBack = {
                    navController.popBackStack()
                },
                navToLogin = {
                    navController.navigateAsRoot(AppDestination.Login)
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
                navToLogin = {
                    navController.navigateAsRoot(AppDestination.Login)
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
                navToLogin = {
                    navController.navigateAsRoot(AppDestination.Login)
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
                navToLogin = {
                    navController.navigateAsRoot(AppDestination.Login)
                },
            )
        }

        composable<AppDestination.SaveTimeCapsule> { navBackStackEntry ->
            val arguments = navBackStackEntry.toRoute<AppDestination.SaveTimeCapsule>()
            SaveTimeCapsuleScreen(
                id = arguments.id,
                isNewTimeCapsule = arguments.isNewTimeCapsule,
                navToMain = {
                    navController.navigateAsRoot(AppDestination.Home)
                },
                navToPrevious = {
                    // pop twice, to navigate to previous screen
                    navController.popBackStack()
                    navController.popBackStack()
                },
                navToBack = {
                    navController.popBackStack()
                },
                navToLogin = {
                    navController.navigateAsRoot(AppDestination.Login)
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
                navToLogin = {
                    navController.navigateAsRoot(AppDestination.Login)
                },
                /*  navToNotificationSetting = {
                      navController.navigateWithClearStack(AppDestination.NotificationSetting)
                  },*/
            )
        }

        composable<AppDestination.NicknameChange> {
            NicknameChangeScreen(
                navToBack = {
                    navController.popBackStack()
                },
                navToLogin = {
                    navController.navigateAsRoot(AppDestination.Login)
                },
            )
        }

        composable<AppDestination.AccountInfo> {
            AccountInfoScreen(
                navToBack = {
                    navController.popBackStack()
                },
                navToLogin = {
                    navController.navigateAsRoot(AppDestination.Login)
                },
            )
        }

        composable<AppDestination.KeyDescription> {
            KeyDescriptionScreen(
                navToBack = {
                    navController.popBackStack()
                },
                navToLogin = {
                    navController.navigateAsRoot(AppDestination.Login)
                },
            )
        }

        composable<AppDestination.NotificationSetting> {
            NotificationSettingScreen(
                navToBack = {
                    navController.popBackStack()
                },
                navToLogin = {
                    navController.navigateAsRoot(AppDestination.Login)
                },
            )
        }

        composable<AppDestination.PushNotification> {
            PushNotificationScreen(
                navToDailyReportDetail = { id ->
                    navController.navigate(AppDestination.DailyReportDetail(id))
                },
                navToTimeCapsuleDetail = { id ->
                    navController.navigate(AppDestination.TimeCapsuleDetail(id, isNewTimeCapsule = false))
                },
                navToBack = {
                    navController.popBackStack()
                },
            )
        }
    }
}
