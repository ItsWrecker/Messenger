package com.qxlabai.messenger.screeens


import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.qxlabai.messenger.navigation.Destinations
import com.qxlabai.presentation.xmpp.auth.AuthViewModel
import com.qxlabai.presentation.xmpp.connection.XmppViewModel
import com.qxlabai.presentation.xmpp.services.CredentialsParcel


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Authentication(navHostController: NavHostController) {

    val viewModel = hiltViewModel<AuthViewModel>()
    val viewState = viewModel.viewState.collectAsState()
    val context = LocalContext.current

    if (viewState.value.isAuthenticated) {
        Log.e("AUTH01", "Finished")
        navHostController.navigate(Destinations.ProfileScreen.route){
            navHostController.popBackStack()
        }
    } else {
        var valueUID by remember {
            mutableStateOf("")
        }

        var valuePassword by remember {
            mutableStateOf("")
        }
        var isValid by remember { mutableStateOf(false) }
        val mContext = LocalContext.current


        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = viewState.value.uuid,
                onValueChange = { newText ->
                    viewModel.updateUUID(newText)
                },
                label = {
                    Text("Please Enter UID")
                },
            )
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = viewState.value.passcode,
                onValueChange = { newText ->
                    viewModel.updatePasscode(newText)
                },
                label = {
                    Text("Please Enter Password")
                },
            )

            Spacer(modifier = Modifier.height(40.dp))
            OutlinedButton(onClick = {
                if (viewState.value.uuid.isEmpty()) {
                    Toast.makeText(mContext, "UID is Empty", Toast.LENGTH_SHORT).show()
                } else if (viewState.value.passcode.isEmpty()) {
                    Toast.makeText(mContext, "Password is Empty", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.authenticate(
                        context = context
                    )
                }

            }, modifier = Modifier.height(40.dp)) {
                Text("Submit")
            }

        }
    }
}