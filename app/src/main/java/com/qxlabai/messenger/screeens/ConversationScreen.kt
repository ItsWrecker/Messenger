package com.qxlabai.messenger.screeens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.twotone.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.qxlabai.messenger.R
import com.qxlabai.messenger.components.SingleMassage
import com.qxlabai.messenger.models.Message


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ConversationScreen(navHostController: NavHostController) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Surface(shadowElevation = 2.dp) {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            Text(text = "KJ_XYZ")
                            Spacer(modifier = Modifier.size(8.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_online_prediction_24),
                                contentDescription = "online",
                                tint = Color.Green
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = "AuthIcons"
                            )
                        }
                    },
                    scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
                    windowInsets = WindowInsets.statusBars,
                    navigationIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Outlined.List, contentDescription = "")
                        }
                    }
                )
            }
        }
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            val messageList = mutableListOf(
                Message(
                    "Hey, how's it going?",
                    "user123",
                    System.currentTimeMillis()
                ),
                Message(
                    "Not too bad, just relaxing at home. How about you?",
                    "user123",
                    System.currentTimeMillis()
                ),
                Message(
                    "Same here, just chilling. Did you catch that new movie last night?",
                    "user123",
                    System.currentTimeMillis()
                ),
                Message(
                    "Oh yeah, I did! It was awesome. I loved the action scenes.",
                    "user123",
                    System.currentTimeMillis()
                ),
                Message(
                    "Totally agree! The effects were mind-blowing. What's on your agenda for the weekend?",
                    "user123",
                    System.currentTimeMillis()
                ),
                Message(
                    "I'm thinking of going hiking on Saturday and maybe hitting the beach on Sunday. How about you?",
                    "user123",
                    System.currentTimeMillis()
                ),
                Message(
                    "Sounds like a fun weekend plan! I'm up for some brunch on Sunday and maybe a bit of gaming on Saturday.",
                    "user123",
                    System.currentTimeMillis()
                ),
                Message(
                    "Lately, I've been hooked on that new RPG game. It's addictive! What about you?",
                    "user123",
                    System.currentTimeMillis()
                ),
                Message(
                    "I've been playing a lot of multiplayer shooters with friends. It gets pretty competitive.",
                    "user123",
                    System.currentTimeMillis()
                ),
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1f), reverseLayout = false
            ) {
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
                itemsIndexed(messageList) { index: Int, item: Message ->
                    SingleMassage(message = item, index)
                }
            }
            val keyboardController = LocalSoftwareKeyboardController.current


            val messageInput = remember {
                mutableStateOf("")
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth().padding(16.dp),
                value = messageInput.value,
                onValueChange = { value ->
                    messageInput.value = value
                },
                placeholder = {
                    Text(text = "Message..")
                },
                shape = RoundedCornerShape(24.dp),
                trailingIcon = {
                    IconButton(onClick = {
                        messageList.add(
                            Message(
                                messageInput.value,
                                "User",
                                System.currentTimeMillis()
                            )
                        )
                        messageInput.value = ""
                    }) {
                        Icon(imageVector = Icons.Outlined.Send, contentDescription = "Send")
                    }
                }
            )
        }
    }
}