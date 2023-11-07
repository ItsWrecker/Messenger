package com.qxlabai.messenger

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import com.qxlabai.messenger.core.model.data.DarkConfig.System
import com.qxlabai.messenger.core.model.data.ThemeConfig
import com.qxlabai.messenger.core.data.repository.PreferencesRepository
import com.qxlabai.messenger.core.design.theme.MessengerTheme
import com.qxlabai.messenger.core.model.data.DarkConfig.Dark
import com.qxlabai.messenger.core.model.data.ThemeBranding
import com.qxlabai.messenger.service.xmpp.XmppService
import com.qxlabai.messenger.ui.MessengerApp
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var themeConfig: ThemeConfig? by mutableStateOf(null)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(STARTED) {
                preferencesRepository.getThemeConfig().collect {
//                    themeConfig = it
                    themeConfig = ThemeConfig(
                        themeBranding = ThemeBranding.Android,
                        darkConfig = Dark
                    )
                }
            }
        }

        splashScreen.setKeepOnScreenCondition {
            when (themeConfig) {
                null -> true
                else -> false
            }
        }

        ContextCompat.startForegroundService(this, Intent(this, XmppService::class.java))

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = rememberSystemUiController()
            val darkTheme = themeConfig.shouldUseDarkTheme

            DisposableEffect(systemUiController, darkTheme) {
                systemUiController.systemBarsDarkContentEnabled = !darkTheme
                onDispose {}
            }

            MessengerTheme(
                darkTheme = darkTheme,
                androidTheme = themeConfig.shouldUseAndroidTheme
            ) {
                MessengerApp()
            }
        }
    }

    override fun onResume() {
        super.onResume()

    }
}

private val ThemeConfig?.shouldUseAndroidTheme: Boolean
    get() = when (this) {
        null -> false
        else -> themeBranding == com.qxlabai.messenger.core.model.data.ThemeBranding.Android
    }

private val ThemeConfig?.shouldUseDarkTheme: Boolean
    @Composable get() = when (this?.darkConfig) {
        null, System -> isSystemInDarkTheme()
        else -> darkConfig == com.qxlabai.messenger.core.model.data.DarkConfig.Dark
    }
