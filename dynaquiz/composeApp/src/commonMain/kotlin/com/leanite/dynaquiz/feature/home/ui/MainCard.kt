package com.leanite.dynaquiz.feature.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.leanite.dynaquiz.feature.home.HomeValidation
import com.leanite.dynaquiz.feature.home.NicknameError
import com.leanite.dynaquiz.feature.home.res.HomeRes
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun NicknameField(
    nickname: String,
    nicknameError: NicknameError?,
    onNicknameChange: (String) -> Unit,
) {
    val primary = MaterialTheme.colorScheme.primary
    val errorRed = Color(0xFFE53935)

    Row(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = nickname,
            onValueChange = onNicknameChange,
            label = { Text(stringResource(HomeRes.NicknameLabel)) },
            singleLine = true,
            isError = nicknameError != null && nickname.isNotEmpty(),
            supportingText = {
                if (nickname.isNotEmpty()) {
                    nicknameError?.let { error -> Text(error.toUserMessage()) }
                }
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done,
            ),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = primary,
                unfocusedBorderColor = primary,
                focusedLabelColor = primary,
                unfocusedLabelColor = primary,
                cursorColor = primary,
                errorBorderColor = errorRed,
                errorLabelColor = errorRed,
                errorSupportingTextColor = errorRed,
                errorCursorColor = errorRed,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun RankingCard(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(HomeRes.RankingTitle),
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = stringResource(HomeRes.RankingDescription),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Icon(
                painter = painterResource(HomeRes.Chevron),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.height(16.dp)
            )
        }
    }
}

@Composable
fun MainCard(
    nickname: String,
    nicknameError: NicknameError?,
    onNicknameChange: (String) -> Unit,
    onRankingClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            NicknameField(
                nickname = nickname,
                nicknameError = nicknameError,
                onNicknameChange = onNicknameChange,
            )
            RankingCard(onClick = onRankingClick)
        }
    }
}

@Composable
private fun NicknameError.toUserMessage(): String = when (this) {
    NicknameError.Empty -> stringResource(HomeRes.NicknameErrorEmpty)
    NicknameError.TooShort -> stringResource(
        HomeRes.NicknameErrorTooShort,
        HomeValidation.MIN_NICKNAME_LENGTH,
    )
    NicknameError.TooLong -> stringResource(
        HomeRes.NicknameErrorTooLong,
        HomeValidation.MAX_NICKNAME_LENGTH,
    )
}