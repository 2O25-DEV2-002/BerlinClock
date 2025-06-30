package com.anonymous.berlinclock.presentation.berlinclock.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anonymous.berlinclock.R
import com.anonymous.berlinclock.util.TestTags
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun ToggleButton() {
    Row(
        modifier = Modifier
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.automatic_clock),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier,
            textAlign = TextAlign.Center
        )

        var isChecked by remember { mutableStateOf(true) }

        Switch(
            checked = isChecked,
            onCheckedChange = { isChecked = it },
            modifier = Modifier
                .padding(16.dp)
                .semantics { contentDescription = TestTags.TOGGLE }
        )
    }
}