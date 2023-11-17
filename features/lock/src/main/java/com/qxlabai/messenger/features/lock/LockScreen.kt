package com.qxlabai.messenger.features.lock

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qxlabai.messenger.core.common.utils.isValidPasscode

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun LockRoute(
    navigateToConversations: () -> Unit, viewModel: LockViewModel = hiltViewModel(),
    navigateToAuth: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LockScreen(
        uiState = uiState,
        navigateToConversations = navigateToConversations,
        navigateToAuth = navigateToAuth,
        modifier = Modifier,
        onVerifyClick = viewModel::verifyPasscode,
        onPasscodeTyping = viewModel::passcodeTyping,
        onErase = viewModel::eraseEverything,
    )
}

@Composable
fun LockScreen(
    uiState: LockState,
    navigateToConversations: () -> Unit,
    navigateToAuth: () -> Unit,
    modifier: Modifier,
    onVerifyClick: (String) -> Unit,
    onPasscodeTyping: () -> Unit,
    onErase: () -> Unit,
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
        if (uiState is LockState.OnErased) {
            navigateToAuth()
        }
    })


    if (uiState is LockState.Reset) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp)
                .safeContentPadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                modifier = Modifier.height(2.dp)
            )
            Text(text = "Erasing the data!!")
            onErase()
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp)
                .safeContentPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            passcodeHasError = (uiState is LockState.InvalidPasscode)
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(id = if (uiState is LockState.FirstLogin) R.string.set_passcode else R.string.passcode_title),
                fontWeight = FontWeight.ExtraBold
            )

            InputField(
                value = passcode,
                onValueChange = {
                    setPasscode(it.take(6))
                    passcodeHasError = false
                    onPasscodeTyping()
                    if (it.length > 5) focusManager.clearFocus(true)
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next, keyboardType = KeyboardType.NumberPassword
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                passcodeHasError = passcodeHasError || uiState is LockState.InvalidPasscode,
                errorRes = R.string.invalid_passcode,
                modifier = modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))


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

            if (uiState is LockState.FirstLogin) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Cyan,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        ) {
                            append(stringResource(id = R.string.disclaimer))
                        }
                        append("\n")

                        withStyle(
                            style = SpanStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Light
                            )
                        ) {
                            append(stringResource(id = R.string.set_passcode_guide))
                        }
                    },
                    textDecoration = TextDecoration.None
                )
            }
            if (uiState is LockState.AttemptsWarming) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Cyan,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        ) {
                            append(stringResource(id = R.string.disclaimer))
                        }
                        append("\n")

                        withStyle(
                            style = SpanStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Light
                            )
                        ) {
                            append(stringResource(id = R.string.warning))
                        }
                    },
                    textDecoration = TextDecoration.None
                )
            }
        }
    }


}

@Composable
fun VerifyButton(
    uiState: LockState, modifier: Modifier = Modifier, onClick: () -> Unit, enabled: Boolean
) {
    Button(
        onClick = onClick, enabled = enabled, colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContentColor = MaterialTheme.colorScheme.onSecondary
        ), modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 50.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(if (uiState is LockState.FirstLogin) R.string.set_passcode else R.string.verify).uppercase(),
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
    passcodeHasError: Boolean,
    @StringRes errorRes: Int,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    Column {
        BasicTextField(value = value,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            modifier = modifier.fillMaxWidth(),
            maxLines = 1,
            decorationBox = {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    repeat(6) {
                        CharView(index = it, text = value)
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            })
        if (passcodeHasError) {

            Spacer(modifier = Modifier.padding(vertical = 16.dp))
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
private fun CharView(
    index: Int, text: String
) {
    val isFocused = text.length == index
    val char = when {
        index == text.length -> "?"
        index > text.length -> ""
        else -> text[index].toString()
    }
    Text(
        modifier = Modifier

            .border(
                2.dp, when {
                    isFocused -> Color.LightGray
                    else -> Color.DarkGray
                }, RoundedCornerShape(12.dp)
            )
            .padding(8.dp)
            .wrapContentSize(Alignment.Center)
            .requiredSize(24.dp),
        text = char,
        color = if (isFocused) Color.LightGray else Color.DarkGray,
        textAlign = TextAlign.Center,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace,
        fontSize = 18.sp
    )
}
