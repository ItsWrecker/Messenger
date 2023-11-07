package com.qxlabai.messenger.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy

import com.qxlabai.messenger.core.navigation.NavigationParameters
import com.qxlabai.messenger.core.designsystem.component.MessengerBackground
import com.qxlabai.messenger.core.designsystem.component.MessengerNavigationBar
import com.qxlabai.messenger.core.designsystem.component.MessengerNavigationBarItem
import com.qxlabai.messenger.navigation.MessengerNavHost
import com.qxlabai.messenger.navigation.TopLevelDestination
import com.qxlabai.messenger.ui.ConnectionStatusUiState.Connecting
import com.qxlabai.messenger.R


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLifecycleComposeApi::class,
    ExperimentalLayoutApi::class
)
@Composable
fun MessengerApp(
    appState: MessengerAppState = rememberMessengerAppState(),
    viewModel: MessengerViewModel = hiltViewModel()
) {
    val uiState by viewModel.connectionStatusUiState.collectAsStateWithLifecycle()

    MessengerBackground {
        Scaffold(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
        ) { padding ->
            Box(
                Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal
                        )
                    )
            ) {
                MessengerNavHost(
                    navController = appState.navController,
                    onNavigateToDestination = appState::navigate,
                    onExitChat = viewModel::onExitChat,
                    onBackClick = appState::onBackClick,
                    modifier = Modifier
                        .padding(padding)
                        .consumedWindowInsets(padding)
                )

                if (appState.shouldShowConnecting) {
                    Connecting(uiState is Connecting)
                }
            }
        }
    }
}

@Composable
private fun Connecting(isConnecting: Boolean) {
    AnimatedVisibility(
        visible = isConnecting,
        enter = fadeIn() + slideInVertically(),
        exit = slideOutVertically() + fadeOut(),
    ) {
        Surface(color = MaterialTheme.colorScheme.surfaceVariant) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.connecting),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}