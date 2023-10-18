package com.qxlabai.messenger.contacts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.qxlabai.presentation.contacts.ContactsViewModel


@Composable
fun ContactsRoute(
    modifier: Modifier = Modifier,
    navigateToChat: (String) -> Unit,
    viewModel: ContactsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    ContactsScreen(
        uiState = uiState,
        addContact = viewModel::addContact,
        navigateToChat = navigateToChat,
        modifier = modifier
    )
}


