package com.qxlabai.messenger.screeens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.qxlabai.messenger.navigation.Destinations
import com.qxlabai.presentation.xmpp.connection.XmppViewModel

@Composable
fun ConnectionScreen(navHostController: NavHostController) {
    val xmppViewModel = hiltViewModel<XmppViewModel>()
    val context = LocalContext.current
    val viewState = xmppViewModel.viewState.collectAsState()

    if (viewState.value.isConnectionEstablished) navHostController.navigate(Destinations.AuthenticationScreen.route) {
        navHostController.popBackStack()
        launchSingleTop = true
        restoreState = false
    } else xmppViewModel.bindService(context)

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth(0.75F)
                    .progressSemantics()
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = viewState.value.progressMessage.toString())
        }
        return@Surface
    }
}