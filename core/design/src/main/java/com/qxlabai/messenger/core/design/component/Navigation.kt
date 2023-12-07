package com.qxlabai.messenger.core.designsystem.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
            selectedIconColor = MessengerNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = MessengerNavigationDefaults.navigationContentColor(),
            selectedTextColor = MessengerNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = MessengerNavigationDefaults.navigationContentColor(),
            indicatorColor = MessengerNavigationDefaults.navigationIndicatorColor()
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
        containerColor = MessengerNavigationDefaults.NavigationContainerColor,
        contentColor = MessengerNavigationDefaults.navigationContentColor(),
        tonalElevation = 0.dp,
        content = content
    )
}

/**
 * Dialogue navigation default values.
 */
object MessengerNavigationDefaults {
    val NavigationBarHeight = 80.dp
    val NavigationContainerColor = Color.Transparent
    @Composable
    fun navigationContentColor() = MaterialTheme.colorScheme.onSurfaceVariant
    @Composable
    fun navigationSelectedItemColor() = MaterialTheme.colorScheme.onPrimaryContainer
    @Composable
    fun navigationIndicatorColor() = MaterialTheme.colorScheme.primaryContainer
}

val NavigationBarsHeight: Dp
    @Composable
    get() = MessengerNavigationDefaults.NavigationBarHeight +
        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
