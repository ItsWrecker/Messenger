package com.qxlabai.messenger.screeens


import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.qxlabai.messenger.navigation.Destinations
import com.qxlabai.presentation.xmpp.XmppAction
import com.qxlabai.presentation.xmpp.XmppViewModel


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Authentication(navHostController: NavHostController) {

    val viewModel = hiltViewModel<XmppViewModel>()
    val viewState = viewModel.viewState.collectAsState()

    if (viewModel.viewState.value.isConnectionEstablished.not()) {
        navHostController.popBackStack()
        return
    }

    if (viewModel.viewState.value.isAuthenticated) {
        viewModel.processAction(XmppAction.FetchUserId)
        navHostController.navigate(Destinations.ProfileScreen.route) {
            navHostController.popBackStack()
            launchSingleTop = true
            restoreState = false
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
            val isError = viewState.value.error.isNullOrEmpty()
            OutlinedTextField(
                value = valueUID,
                onValueChange = { newText ->
                    valueUID = newText
                    isValid = newText.isNotEmpty() // Add your custom validation rules here

                },
                label = {
                    Text(if (isError) "Please Enter UID" else "Please Enter UID")
                },
                isError = isError
            )
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = valuePassword,
                onValueChange = { newText ->
                    valuePassword = newText
                    isValid = newText.isNotEmpty() // Add your custom validation rules here

                },
                label = {
                    Text(if (isError) "Please Enter correct passcode" else "Please Enter Password")
                },
                isError = isError
            )

            Spacer(modifier = Modifier.height(40.dp))
            OutlinedButton(onClick = {
                if (valueUID.isEmpty()) {
                    Toast.makeText(mContext, "UID is Empty", Toast.LENGTH_SHORT).show()
                } else if (valuePassword.isEmpty()) {
                    Toast.makeText(mContext, "Password is Empty", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.processAction(
                        XmppAction.Authenticate(
                            valueUID,
                            valuePassword
                        )
                    )
                }

            }, modifier = Modifier.height(40.dp)) {
                Text("Submit")
            }
        }
    }
}