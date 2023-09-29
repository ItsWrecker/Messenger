package com.qxlabai.messenger.screeens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.qxlabai.messenger.navigation.Destinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionScreen(navHostController: NavHostController) {
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
        TextField(
            value = valueUID,
            onValueChange = { newText ->
                valueUID = newText
                isValid = newText.isNotEmpty() // Add your custom validation rules here

            },
            label = { Text("Please Enter UID") },
        )
        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = valuePassword,
            onValueChange = { newText ->
                valuePassword = newText
                isValid = newText.isNotEmpty() // Add your custom validation rules here

            },
            label = { Text("Please Enter Password") },
        )

        Spacer(modifier = Modifier.height(40.dp))
        //Button(onClick = {navHostController.navigate(Destinations.ConversationScreen.route)
        Button(onClick = {
            if (valueUID.isEmpty()) {
                Toast.makeText(mContext, "UID is Empty", Toast.LENGTH_SHORT).show()
            } else if (valuePassword.isEmpty()) {
                Toast.makeText(mContext, "Password is Empty", Toast.LENGTH_SHORT).show()
            } else {
                navHostController.navigate(Destinations.ProfileScreen.route)
            }
        }, modifier = Modifier.height(40.dp)) {
            Text("Submit")
        }
    }
}