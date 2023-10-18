package com.qxlabai.messenger.logic

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.qxlabai.messenger.navigation.TopLevelDestination
import com.qxlabai.presentation.navigation.NavigationParameters


@Composable
fun MessengerBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (NavigationParameters) -> Unit,
    currentDestination: NavDestination?
) {
    Surface(color = MaterialTheme.colorScheme.surface) {
        MessengerNavigationBar(
            modifier = Modifier.windowInsetsPadding(
                WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
                )
            )
        ) {
            destinations.forEach { destination ->
                val selected =
                    currentDestination?.hierarchy?.any { it.route == destination.route } == true

                MessengerNavigationBarItem(
                    selected = selected,
                    onClick = { onNavigateToDestination(NavigationParameters(destination)) },
                    icon = {
                        Icon(imageVector = destination.icon, contentDescription = null)
                    })
            }
        }
    }
}

@Composable
fun RowScope.MessengerNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    selectedIcon: @Composable () -> Unit = icon,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        colors = NavigationBarItemDefaults.colors(
//            selectedIconColor = DialogueNavigationDefaults.navigationSelectedItemColor(),
//            unselectedIconColor = DialogueNavigationDefaults.navigationContentColor(),
//            selectedTextColor = DialogueNavigationDefaults.navigationSelectedItemColor(),
//            unselectedTextColor = DialogueNavigationDefaults.navigationContentColor(),
//            indicatorColor = DialogueNavigationDefaults.navigationIndicatorColor()
        )
    )
}

@Composable
fun MessengerNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    NavigationBar(
        modifier = modifier,
        tonalElevation = 0.dp,
        content = content
    )
}