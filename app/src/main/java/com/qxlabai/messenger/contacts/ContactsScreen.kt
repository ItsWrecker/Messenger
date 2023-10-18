package com.qxlabai.messenger.contacts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.qxlabai.domain.entity.Contact
import com.qxlabai.messenger.conversation.ContactThumb
import com.qxlabai.presentation.contacts.ContactsUiState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ContactsScreen(
    uiState: ContactsUiState,
    addContact: Any,
    navigateToChat: (String) -> Unit,
    modifier: Modifier
) {

    var isAddContactDialogVisible by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Hello") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { isAddContactDialogVisible = true }) {
                Icon(imageVector = Icons.Filled.Person, contentDescription = "add")
            }
        },
        containerColor = Color.Transparent,
        modifier = modifier.padding(bottom = 16.dp)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            contacts(
                uiState = uiState,
                navigateToChat = navigateToChat
            )
        }

//        if (isAddContactDialogVisible) {
//            AddContactDialog(
//                addContact = {
//                    addContact(it)
//                    isAddContactDialogVisible = false
//                },
//                onDismissRequest = { isAddContactDialogVisible = false }
//            )
//        }
    }
}

private fun LazyListScope.contacts(
    uiState: ContactsUiState,
    navigateToChat: (String) -> Unit,
) {
    when (uiState) {
        ContactsUiState.Loading -> {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize()
                )
            }
        }

        is ContactsUiState.Success -> {
            items(uiState.contacts, key = { it.jid }) { contact ->
                ContactItem(
                    contact = contact,
                    onContactClick = navigateToChat
                )
                Divider(color = MaterialTheme.colorScheme.surfaceVariant)
            }
        }
    }
}

@Composable
private fun ContactItem(
    contact: Contact,
    onContactClick: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .height(80.dp)
            .clickable { onContactClick(contact.jid) }
            .padding(horizontal = 16.dp)
    ) {
        ContactThumb(firstLetter = contact.firstLetter)

        Text(
            text = contact.jid,
            modifier = Modifier.fillMaxWidth()
        )
    }
}