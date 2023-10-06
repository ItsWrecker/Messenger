package com.qxlabai.messenger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.qxlabai.messenger.navigation.SetupNavigation
import com.qxlabai.messenger.ui.theme.MessengerTheme
import com.qxlabai.presentation.xmpp.connection.XmppViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navHostController: NavHostController
    private val viewModel by viewModels<XmppViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navHostController = rememberNavController()
            MessengerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetupNavigation(navHostController = navHostController)
                }
            }
        }
    }
}