package com.emotionstorage.emotionstorage.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.emotionstorage.home.ui.HomeScreen
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.emotionstorage.R
import com.emotionstorage.my.ui.MyScreen
import com.emotionstorage.time_capsule.ui.TimeCapsuleCalendarScreen

private enum class MainNavItem(
    val route: AppDestination.Main,
    val icon: Int,
    val label: String
) {
    HOME(AppDestination.Main.Home, R.drawable.ic_home, "홈 화면"),
    CALENDAR(AppDestination.Main.TIME_CAPSULE_CALENDAR, R.drawable.ic_calendar, "감정 보관함"),
    MY(AppDestination.Main.My, R.drawable.ic_my, "내 페이지"),
}

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
) {
    val mainNavController = rememberNavController()

    Scaffold(
        modifier = modifier,
        bottomBar = {
            MainBottomAppBar(mainNavController)
        }
    ) { it ->
        NavHost(
            navController = mainNavController,
            modifier = Modifier.padding(it),
            startDestination = AppDestination.Main.Home
        ) {
            composable<AppDestination.Main.Home> {
                HomeScreen()
            }
            composable<AppDestination.Main.TIME_CAPSULE_CALENDAR> {
                TimeCapsuleCalendarScreen()
            }
            composable<AppDestination.Main.My> {
                MyScreen()
            }
        }
    }
}


@Composable
private fun MainBottomAppBar(mainNavController: NavHostController) {
    BottomAppBar {
        val navBackStackEntry = mainNavController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry.value?.destination

        Row(modifier = Modifier.fillMaxWidth()) {
            MainNavItem.values().forEach {
                MainBottomAppBarItem(
                    iconId = it.icon,
                    label = it.label,
                    isSelected = currentDestination == it.route,
                    onClick = {
                        mainNavController.navigate(it.route) {
                            popUpTo(mainNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun MainBottomAppBarItem(
    iconId: Int,
    label: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = label,
            tint = if (isSelected) Color.White else MooiTheme.colorScheme.gray700
        )
        Text(
            text = label,
            style = MooiTheme.typography.body3.copy(fontSize = 10.sp, lineHeight = 24.sp),
            color = if (isSelected) Color.White else MooiTheme.colorScheme.gray700
        )
    }
}