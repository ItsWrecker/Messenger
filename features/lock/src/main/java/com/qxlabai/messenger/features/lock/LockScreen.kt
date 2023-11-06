package com.qxlabai.messenger.features.lock

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qxlabai.messenger.core.common.utils.isValidJid
import com.qxlabai.messenger.core.common.utils.isValidPasscode

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun LockRoute(
    navigateToConversations: () -> Unit,
    viewModel: LockViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LockScreen(
        uiState = uiState,
        navigateToConversations = navigateToConversations,
        modifier = Modifier,
        onVerifyClick = viewModel::verifyPasscode
    )
}

@Composable
fun LockScreen(
    uiState: LockState,
    navigateToConversations: () -> Unit,
    modifier: Modifier,
    onVerifyClick: (String) -> Unit

) {

    val (passcode, setPasscode) = remember {
        mutableStateOf("")
    }
    var passcodeHasError by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(key1 = uiState, block = {
        if (uiState is LockState.PasscodeVerified) {
            navigateToConversations()
        }
    })

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp)
            .safeContentPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {

        Text(text = stringResource(id = R.string.passcode_title), fontWeight = FontWeight.ExtraBold)

        if (uiState is LockState.InvalidPasscode) {
            Text(text = "Invalid Passcode please try again!!", color = Color.Red)
        }

        InputField(
            value = passcode,
            onValueChange = {
                setPasscode(it)
                passcodeHasError = false
            },
            labelRes = R.string.passcode,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            passcodeHasError = passcodeHasError,
            errorRes = R.string.invalid_passcode,
            modifier = modifier.fillMaxWidth()
        )

        VerifyButton(
            uiState = uiState,
            onClick = {
                passcodeHasError = passcode.isValidPasscode.not()
                if (passcodeHasError.not()) {
                    onVerifyClick(passcode)
                }
            },
            enabled = uiState != LockState.Loading,
        )
    }


}

@Composable
fun VerifyButton(
    uiState: LockState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContentColor = MaterialTheme.colorScheme.onSecondary
        ),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 50.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.verify).uppercase(),
                modifier = Modifier.align(Alignment.Center)
            )
            if (uiState == LockState.Loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterEnd)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    @StringRes labelRes: Int,
    passcodeHasError: Boolean,
    @StringRes errorRes: Int,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = stringResource(labelRes)) },
            singleLine = true,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            isError = passcodeHasError,
            modifier = modifier.fillMaxWidth()
        )
        if (passcodeHasError) {
            Text(
                text = stringResource(errorRes),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}