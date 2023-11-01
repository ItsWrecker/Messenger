package com.qxlabai.messenger.features.conversations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qxlabai.messenger.core.model.data.Conversation
import com.qxlabai.messenger.core.common.utils.formatted
import com.qxlabai.messenger.core.designsystem.component.DialogueDivider
import com.qxlabai.messenger.core.designsystem.component.DialogueLoadingWheel
import com.qxlabai.messenger.core.designsystem.component.MessengerTopAppBar
import com.qxlabai.messenger.core.designsystem.component.NavigationBarsHeight
import com.qxlabai.messenger.core.model.data.Contact
import com.qxlabai.messenger.core.ui.ContactThumb
import com.qxlabai.messenger.features.conversations.ConversationsUiState.Loading
import com.qxlabai.messenger.features.conversations.ConversationsUiState.Success


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ConversationsRoute(
    navigateToChat: (String) -> Unit,
    navigateToSetting: (Unit) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ConversationsViewModel = hiltViewModel(),
    contactViewModel: ContactsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val contactsUiState by contactViewModel.uiState.collectAsStateWithLifecycle()

    ConversationsScreen(
        uiState = uiState,
        contactsUiState = contactsUiState,
        navigateToChat = navigateToChat,
        modifier = modifier,
        addContact = contactViewModel::addContact,
        navigateToSetting = navigateToSetting
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ConversationsScreen(
    uiState: ConversationsUiState,
    contactsUiState: ContactsUiState,
    navigateToChat: (String) -> Unit,
    modifier: Modifier = Modifier,
    addContact: (String) -> Unit,
    navigateToSetting: (Unit) -> Unit
) {
    var isAddContactDialogVisible by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            MessengerTopAppBar(
                titleRes = R.string.conversations_title,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                ),
                actionIcon = ImageVector.vectorResource(R.drawable.baseline_more_vert_24),
                navigationIcon = ImageVector.vectorResource(R.drawable.baseline_menu_24),
                onNavigationClick = {
                    navigateToSetting(Unit)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { isAddContactDialogVisible = true }) {
                Icon(imageVector = Icons.Filled.PersonAdd, contentDescription = "stringResource()")
            }
        },
        containerColor = Color.Transparent,
        modifier = modifier.padding(bottom = (NavigationBarsHeight) - 56.dp)
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumedWindowInsets(innerPadding)
        ) {
            conversations(
                uiState = uiState,
                contactsUiState = contactsUiState,
                navigateToChat = navigateToChat
            )
        }

        if (isAddContactDialogVisible) {
            AddContactDialog(
                addContact = {
                    addContact(it)
                    isAddContactDialogVisible = false
                },
                onDismissRequest = { isAddContactDialogVisible = false }
            )
        }
    }
}

private fun LazyListScope.conversations(
    uiState: ConversationsUiState,
    contactsUiState: ContactsUiState,
    navigateToChat: (String) -> Unit
) {
    when (uiState) {
        Loading -> {
            item {
                DialogueLoadingWheel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize()
                        .testTag("conversations:loading")
                )
            }
        }

        is Success -> {
            item { DialogueDivider() }


            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)),
                    //elevation = CardDefaults.cardElevation(1.dp)

                ) {
                    Text(
                        text = stringResource(id = R.string.contacts_title),
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp),
                        fontWeight = FontWeight.Bold
                    )
                    LazyRow(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(vertical = 8.dp)
                    ) {
                        contacts(uiState = contactsUiState, navigateToChat)
                    }
                }
            }
            if (uiState.conversations.isNotEmpty()){
                item {
                    Text(
                        text = stringResource(id = R.string.chat_title),
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
                items(uiState.conversations, key = { it.peerJid }) { conversation ->
                    ConversationItem(
                        conversation = conversation,
                        onConversationClick = navigateToChat
                    )
                    DialogueDivider()
                }
            }
        }
    }
}

@Composable
private fun ConversationItem(
    conversation: Conversation,
    onConversationClick: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .height(80.dp)
            .clickable { onConversationClick(conversation.peerJid) }
            .padding(horizontal = 16.dp)
    ) {
        ContactThumb(firstLetter = conversation.firstLetter)

        ConversationContent(
            conversation = conversation,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Composable
private fun ConversationContent(
    modifier: Modifier = Modifier,
    conversation: Conversation
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = conversation.peerLocalPart,
                style = MaterialTheme.typography.bodyLarge
            )
            conversation.lastMessage?.let {
                Text(
                    text = it.sendTime.formatted,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            conversation.subtitle?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
            if (conversation.unreadMessagesCount > 0) {
                MessagesCount(conversation.unreadMessagesCount)
            }
        }
    }
}

@Composable
private fun MessagesCount(
    unreadMessagesCount: Int
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Text(
            text = unreadMessagesCount.toString(),
            style = MaterialTheme.typography.labelMedium
        )
    }
}


private fun LazyListScope.contacts(
    uiState: ContactsUiState,
    navigateToChat: (String) -> Unit,
) {
    when (uiState) {
        ContactsUiState.Loading -> item {
            LinearProgressIndicator()
        }

        is ContactsUiState.Success -> {

            if (uiState.contacts.isEmpty()) {
                item {
                    Text(
                        text = stringResource(id = R.string.contact_hind),
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Justify
                    )
                }
            } else {
                items(uiState.contacts, key = { it.jid }) { contact ->
                    ContactItem(
                        contact = contact,
                        onContactClick = navigateToChat
                    )
                }
            }
        }
    }
}

@Composable
private fun ContactItem(
    contact: Contact,
    onContactClick: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            //.height(80.dp)
            .clickable { onContactClick(contact.jid) }
            .padding(horizontal = 16.dp)
    ) {
        ContactThumb(firstLetter = contact.firstLetter, smallShape = true)

        Text(
            text = contact.jid.split("@").first(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
