package com.qxlabai.messenger.features.conversations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.qxlabai.messenger.core.common.utils.isValidJid
import com.qxlabai.messenger.features.conversations.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddContactDialog(
    modifier: Modifier = Modifier,
    addContact: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val (newContact, setNewContact) = rememberSaveable { mutableStateOf("") }
    val (contactHasError, setContactHasError) = rememberSaveable { mutableStateOf(false) }

    AlertDialog(
        title = {
            Text(text = stringResource(R.string.add_contact_title))
        },
        text = {
            AddContactContent(
                contact = newContact,
                setContact = setNewContact,
                contactHasError = contactHasError,
                setContactHasError = setContactHasError
            )
        },
        confirmButton = {
            AddContactConfirmButton(
                contact = newContact,
                addContact = addContact,
                setContactHasError = setContactHasError
            )
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.cancel))
            }
        },
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddContactContent(
    contact: String,
    setContact: (String) -> Unit,
    contactHasError: Boolean,
    setContactHasError: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    LocalView.current.viewTreeObserver.addOnWindowFocusChangeListener {
        if (it) focusRequester.requestFocus()
    }

    Column {
        OutlinedTextField(
            value = contact,
            onValueChange = {
                setContact(it)
                setContactHasError(false)
            },
            label = { Text(text = stringResource(R.string.new_contact)) },
            isError = contactHasError,
            modifier = modifier
                .testTag("newContactTextField")
                .focusRequester(focusRequester)
        )
        AnimatedVisibility(visible = contactHasError) {
            Text(
                text = stringResource(R.string.error_contact_is_not_valid_jabber_id),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
private fun AddContactConfirmButton(
    contact: String,
    addContact: (String) -> Unit,
    setContactHasError: (Boolean) -> Unit
) {
    TextButton(
        onClick = {
            val contactHasError = !contact.isValidJid
            setContactHasError(contactHasError)
            if (!contactHasError) {
                addContact(contact)
            }
        },
        modifier = Modifier.testTag("addContactButton")
    ) {
        Text(text = stringResource(R.string.add))
    }
}
