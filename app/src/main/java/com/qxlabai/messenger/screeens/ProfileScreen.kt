package com.qxlabai.messenger.screeens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.qxlabai.messenger.R
import com.qxlabai.messenger.navigation.Destinations
import com.qxlabai.presentation.xmpp.connection.XmppViewModel
import com.qxlabai.presentation.xmpp.profile.ProfileViewModel

@Composable
fun ProfileScreen(navHostController: NavHostController) {
    val viewModel = hiltViewModel<ProfileViewModel>()
    val viewState = viewModel.viewState.collectAsState()

    viewModel.fetchProfile()

    if (viewState.value.isLoading) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LinearProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Fetching user's profile")
        }
    } else {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(
                    id = R
                        .drawable.user_icon
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .border(
                        BorderStroke(2.dp, Color.White),
                        CircleShape
                    )
            )
            Spacer(modifier = Modifier.height(40.dp))


            Text(viewState.value.userName)

            Spacer(modifier = Modifier.height(40.dp))
            //Button(onClick = {navHostController.navigate(Destinations.ConversationScreen.route)
            Button(onClick = {
                navHostController.navigate(Destinations.ConversationScreen.route).also {
//                    navHostController.popBackStack()
                }

            }, modifier = Modifier.height(40.dp)) {
                Text("Continue")
            }
        }
    }

}