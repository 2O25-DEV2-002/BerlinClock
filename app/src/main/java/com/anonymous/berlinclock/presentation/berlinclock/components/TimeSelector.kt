package com.anonymous.berlinclock.presentation.berlinclock.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.anonymous.berlinclock.R
import com.anonymous.berlinclock.util.HOUR_MAX_VALUE
import com.anonymous.berlinclock.util.TIME_DELIMITER
import com.anonymous.berlinclock.util.TIME_MAX_VALUE
import com.anonymous.berlinclock.util.TestTags

@Composable
fun TimeSelector(
    showBerlinTime: (String) -> Unit
) {
    var selectedHour by remember { mutableStateOf("") }
    var selectedMinute by remember { mutableStateOf("") }
    var selectedSecond by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .semantics {
                contentDescription = TestTags.TIME_SELECTOR
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TimeInputField(
                value = selectedHour,
                onValueChange = { selectedHour = it },
                placeholderRes = R.string.hour,
                testTag = TestTags.HOUR_SELECTOR,
                max = HOUR_MAX_VALUE
            )
            Spacer(modifier = Modifier.width(10.dp))
            TimeInputField(
                value = selectedMinute,
                onValueChange = { selectedMinute = it },
                placeholderRes = R.string.minute,
                testTag = TestTags.MINUTE_SELECTOR,
                max = TIME_MAX_VALUE
            )
            Spacer(modifier = Modifier.width(10.dp))
            TimeInputField(
                value = selectedSecond,
                onValueChange = { selectedSecond = it },
                placeholderRes = R.string.second,
                testTag = TestTags.SECOND_SELECTOR,
                max = TIME_MAX_VALUE
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                showBerlinTime("$selectedHour$TIME_DELIMITER$selectedMinute$TIME_DELIMITER$selectedSecond")
                focusManager.clearFocus()
            },
            modifier = Modifier
                .semantics {
                    contentDescription = TestTags.SHOW_BERLIN_TIME_BUTTON
                },
            enabled = selectedHour.isNotEmpty() &&
                    selectedMinute.isNotEmpty() &&
                    selectedSecond.isNotEmpty()
        ) {
            Text(stringResource(R.string.show_berlin_time))
        }
    }
}