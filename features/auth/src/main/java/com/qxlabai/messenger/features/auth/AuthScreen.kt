package com.qxlabai.messenger.features.auth

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qxlabai.features.auth.R
import com.qxlabai.messenger.core.common.utils.isValidJid
import com.qxlabai.messenger.features.auth.AuthUiState.Error
import com.qxlabai.messenger.features.auth.AuthUiState.Loading
import com.qxlabai.messenger.features.auth.AuthUiState.Success

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AuthRoute(
    navigateToLockScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),

) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AuthScreen(
        uiState = uiState,
        onLoginClick = viewModel::login,
        onRegisterClick = viewModel::register,
        modifier = modifier,
        navigateToLockScreen = navigateToLockScreen
    )
}

@Composable
fun AuthScreen(
    uiState: AuthUiState,
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: (String, String) -> Unit,
    navigateToLockScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (jid, setJid) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }

    var jidHasError by remember { mutableStateOf(false) }
    var passwordHasError by remember { mutableStateOf(false) }

    var passwordVisible by remember { mutableStateOf(false) }

    var isRegister by remember {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current

    LaunchedEffect(uiState) {
        if (uiState is Success) {
//            navigateToConversations()
            navigateToLockScreen()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp)
            .safeContentPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = stringResource(if (isRegister) R.string.register_title else R.string.login_title),
            fontWeight = FontWeight.ExtraBold
        )
        if (uiState is Error) {
            GeneralError(uiState.message)
        }

        InputField(
            value = jid,
            onValueChange = {
                setJid(it)
                jidHasError = false
            },
            labelRes = R.string.uuid,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            jidHasError = jidHasError,
            errorRes = R.string.uuid_id_not_valid,
            modifier = modifier.fillMaxWidth(),
            leadingIcon = { Icon(imageVector = Filled.Person, contentDescription = "person") },
            enabled = uiState != Loading,
            placeholder = R.string.uuid_placeholder
        )

        InputField(
            value = password,
            onValueChange = {
                setPassword(it)
                passwordHasError = false
            },
            labelRes = R.string.password,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                VisibilityIcon(
                    passwordVisible = passwordVisible,
                    onPasswordVisibilityChange = { passwordVisible = it }
                )
            },
            jidHasError = passwordHasError,
            errorRes = R.string.error_password_not_valid,
            modifier = modifier.fillMaxWidth(),
            leadingIcon = { Icon(imageVector = Filled.Lock, contentDescription = "person") },
            enabled = uiState != Loading,
           placeholder = R.string.password_placeholder
        )

        Spacer(modifier = Modifier.height(16.dp))


        if (isRegister) {
            RegisterButton(uiState = uiState, onClick = {
                jidHasError = !jid.isValidJid
                passwordHasError = password.isEmpty()
                jidHasError = !jid.isValidJid
                passwordHasError = password.isEmpty()
                if (!jidHasError && !passwordHasError) {
                    onRegisterClick(jid, password)
                    focusManager.clearFocus()
                }
            }, enabled = uiState != Loading)


        } else {
            LoginButton(
                uiState = uiState,
                onClick = {
                    jidHasError = !jid.isValidJid
                    passwordHasError = password.isEmpty()
                    if (!jidHasError && !passwordHasError) {
                        onLoginClick(jid, password)
                        focusManager.clearFocus()
                    }
                },
                enabled = uiState != Loading,
            )
        }
        Text(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .clickable {
                    isRegister = !isRegister
                },
            text = stringResource(id = if (isRegister) R.string.login_hint else R.string.register_hint),
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            fontStyle = FontStyle.Italic,
            textDecoration = TextDecoration.Underline,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun GeneralError(errorMessage: String) {
    Text(text = errorMessage, color = Color.Red)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InputField(
    value: String,
   @StringRes placeholder:Int,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    @StringRes labelRes: Int,
    jidHasError: Boolean,
    @StringRes errorRes: Int,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean
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
            isError = jidHasError,
            modifier = modifier.fillMaxWidth(),
            leadingIcon = leadingIcon,
            enabled = enabled,
            placeholder = { Text(text = stringResource(id = placeholder))}
        )
        if (jidHasError) {
            Text(
                text = stringResource(errorRes),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
private fun VisibilityIcon(
    passwordVisible: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit
) {
    val image = if (passwordVisible)
        Filled.Visibility
    else Filled.VisibilityOff

    val descriptionResId = if (passwordVisible) R.string.hide_password else R.string.show_password

    IconButton(onClick = { onPasswordVisibilityChange(!passwordVisible) }) {
        Icon(
            imageVector = image,
            contentDescription = stringResource(descriptionResId)
        )
    }
}

@Composable
fun RegisterButton(
    uiState: AuthUiState,
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
                text = stringResource(R.string.register).uppercase(),
                modifier = Modifier.align(Alignment.Center)
            )
            if (uiState == Loading) {
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

@Composable
fun LoginButton(
    uiState: AuthUiState,
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
                text = stringResource(R.string.login).uppercase(),
                modifier = Modifier.align(Alignment.Center)
            )
            if (uiState == Loading) {
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
