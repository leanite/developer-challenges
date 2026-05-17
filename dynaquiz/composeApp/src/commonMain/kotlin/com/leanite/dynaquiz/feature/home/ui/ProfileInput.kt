package com.leanite.dynaquiz.feature.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.core.ext.usableString
import com.leanite.dynaquiz.core.ui.theme.DynaquizTheme
import com.leanite.dynaquiz.feature.home.HomeValidation
import com.leanite.dynaquiz.feature.home.res.HomeRes

@Composable
fun ProfileInput(
    nickname: String,
    onNicknameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isNicknameValid = isNicknameValid(nickname)

    Column(modifier = modifier) {
        Text(
            text = HomeRes.ProfileTitle.usableString(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary,
        )

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = nickname,
            onValueChange = onNicknameChange,
            placeholder = { Text(HomeRes.ProfileNameLabel.usableString()) },
            singleLine = true,
            isError = !isNicknameValid,
            supportingText = {
                ValidationSupportText(
                    nickname = nickname,
                    isNicknameValid = isNicknameValid
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done,
            ),
            shape = RoundedCornerShape(12.dp),
            colors = profileInputColors(),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

private fun isNicknameValid(nickname: String): Boolean {
    return nickname.length >= HomeValidation.MIN_NICKNAME_LENGTH
}

@Composable
private fun ValidationSupportText(
    nickname: String,
    isNicknameValid: Boolean
) {
    val counterColor = if (!isNicknameValid) {
        MaterialTheme.colorScheme.error
    } else {
        Color.White
    }
    Text(
        text = "${nickname.length}/${HomeValidation.MAX_NICKNAME_LENGTH} caracteres",
        color = counterColor,
    )
}

@Composable
private fun profileInputColors() : TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedBorderColor = MaterialTheme.colorScheme.secondary,
        unfocusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
        focusedPlaceholderColor = Color.White.copy(alpha = 0.5f),
        unfocusedPlaceholderColor = Color.White.copy(alpha = 0.5f),
        cursorColor = MaterialTheme.colorScheme.secondary,
        errorBorderColor = MaterialTheme.colorScheme.error,
        errorSupportingTextColor = MaterialTheme.colorScheme.error,
        errorCursorColor = MaterialTheme.colorScheme.error,
    )
}

@Preview
@Composable
private fun ProfileInputPreview() {
    DynaquizTheme {
        ProfileInput(
            nickname = "LEA",
            onNicknameChange = {},
        )
    }
}
