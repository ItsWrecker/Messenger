package com.qxlabai.messenger.logic

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qxlabai.messenger.navigation.MessengerNavHost
import com.qxlabai.presentation.massenger.MessengerUiState
import com.qxlabai.presentation.massenger.MessengerViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MessengerApp(
    appState: MessengerAppState = rememberMessengerAppState(),
) {
    val viewModel = hiltViewModel<MessengerViewModel>()

    val uiState by viewModel.uiState.collectAsState()

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
                onBackClick = appState::onBackPress,
                modifier = Modifier
                    .padding(padding)
                    .consumeWindowInsets(padding)
            )

            AnimatedVisibility(
                visible = appState.shouldShowBottomBar,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                MessengerBottomBar(
                    destinations = appState.topLevelDestination,
                    onNavigateToDestination = appState::navigate,
                    currentDestination = appState.currentDestination,
                )
            }

            if (appState.shouldShowConnecting) {
                Connecting(isConnecting = uiState is MessengerUiState.Connecting)
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
                    text = "Connecting",
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