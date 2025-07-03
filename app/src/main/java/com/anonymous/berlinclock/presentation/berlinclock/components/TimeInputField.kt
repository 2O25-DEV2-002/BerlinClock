package com.anonymous.berlinclock.presentation.berlinclock.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.anonymous.berlinclock.util.TIME_SELECTOR_INPUT_MAX_LENGTH

@Composable
fun TimeInputField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes placeholderRes: Int,
    testTag: String,
    max: Int
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (it.isEmpty() || it.isDigitsOnly() && it.length <= TIME_SELECTOR_INPUT_MAX_LENGTH && it.toInt() in 0..max) {
                onValueChange(it)
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        placeholder = { Text(text = stringResource(id = placeholderRes)) },
        singleLine = true,
        modifier = Modifier
            .width(80.dp)
            .padding(2.dp)
            .semantics {
                contentDescription = testTag
            }
    )
}