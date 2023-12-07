package com.qxlabai.messenger.features.contacts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumedWindowInsets
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
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qxlabai.messenger.core.model.data.Contact
import com.qxlabai.messenger.core.designsystem.component.DialogueDivider
import com.qxlabai.messenger.core.designsystem.component.DialogueLoadingWheel
import com.qxlabai.messenger.core.designsystem.component.MessengerTopAppBar
import com.qxlabai.messenger.core.designsystem.component.NavigationBarsHeight
import com.qxlabai.messenger.core.ui.ContactThumb
import com.qxlabai.messenger.features.contacts.ContactsUiState.Loading
import com.qxlabai.messenger.features.contacts.ContactsUiState.Success
import com.qxlabai.messenger.features.contacts.R.string.add
import com.qxlabai.messenger.features.contacts.R.string.contacts_title

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ContactsRoute(
    modifier: Modifier = Modifier,
    navigateToChat: (String) -> Unit,
    viewModel: ContactsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ContactsScreen(
        uiState = uiState,
        addContact = viewModel::addContact,
        navigateToChat = navigateToChat,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ContactsScreen(
    uiState: ContactsUiState,
    addContact: (String) -> Unit,
    navigateToChat: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isAddContactDialogVisible by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            MessengerTopAppBar(
                titleRes = contacts_title,
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
                Icon(imageVector = Filled.PersonAdd, contentDescription = stringResource(add))
            }
        },
        containerColor = Color.Transparent,
        modifier = modifier.padding(bottom = NavigationBarsHeight)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumedWindowInsets(innerPadding)
        ) {
            contacts(
                uiState = uiState,
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

private fun LazyListScope.contacts(
    uiState: ContactsUiState,
    navigateToChat: (String) -> Unit,
) {
    when (uiState) {
        Loading -> {
            item {
                DialogueLoadingWheel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize()
                )
            }
        }
        is Success -> {
            items(uiState.contacts, key = { it.jid }) { contact ->
                ContactItem(
                    contact = contact,
                    onContactClick = navigateToChat
                )
                DialogueDivider()
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
