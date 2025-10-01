package com.emotionstorage.emotionstorage.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.emotionstorage.R

private enum class BottomNavDestination(
    val route: String,
    val icon: Int,
    val label: String,
) {
    HOME(AppDestination.Home::class.qualifiedName!!, R.drawable.ic_home, "홈 화면"),
    CALENDAR(AppDestination.TimeCapsuleCalendar::class.qualifiedName!!, R.drawable.ic_calendar, "감정 보관함"),
    MY(AppDestination.MyPage::class.qualifiedName!!, R.drawable.ic_my, "내 페이지"),
}

private val BottomNavDestinationRoutes = BottomNavDestination.values().map { it.route }

@Composable
fun AppBottomNavBar(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    currentDestination: NavDestination? = null,
) {
    if (currentDestination?.route in BottomNavDestinationRoutes) {
        BottomAppBar(
            modifier =
                modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp)),
            containerColor = MooiTheme.colorScheme.bottomBarBackground,
            contentColor = Color.White,
            contentPadding = PaddingValues(horizontal = 44.dp, vertical = 8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                BottomNavDestination.values().forEach {
                    BottomNavBarItem(
                        iconId = it.icon,
                        label = it.label,
                        isSelected = (currentDestination?.route == it.route),
                        onClick = {
                            navController.navigate(it.route!!) {
                                popUpTo(currentDestination?.route.toString()) {
                                    inclusive = true
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomNavBarItem(
    iconId: Int,
    label: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
) {
    Column(
        modifier =
            modifier.clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = label,
            tint = if (isSelected) Color.White else MooiTheme.colorScheme.gray700,
        )
        Box(modifier = Modifier.height(24.dp)) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = label,
                style = MooiTheme.typography.bottomBar,
                color = if (isSelected) Color.White else MooiTheme.colorScheme.gray700,
                textAlign = TextAlign.Center,
            )
        }
    }
}
