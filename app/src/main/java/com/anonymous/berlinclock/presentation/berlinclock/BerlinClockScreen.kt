package com.anonymous.berlinclock.presentation.berlinclock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anonymous.berlinclock.R
import com.anonymous.berlinclock.util.TestTags
import com.anonymous.berlinclock.presentation.berlinclock.components.ToggleButton
import com.anonymous.berlinclock.util.TestTags.NORMAL_TIME
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.getValue
import com.anonymous.berlinclock.presentation.berlinclock.components.BerlinClock
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.anonymous.berlinclock.presentation.berlinclock.components.TimeSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerlinClockScreen(
    state: StateFlow<ClockState>,
    onEvent: (ClockEvent) -> Unit
) {
    val clockState by state.collectAsStateWithLifecycle()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag(TestTags.TOP_BAR)
                        )
                    },
                )
            },
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(padding),
                contentAlignment = Alignment.TopCenter
            ) {
                var showTimeSelector by remember { mutableStateOf(false) }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    ToggleButton { isToggleOn ->
                        showTimeSelector = !isToggleOn
                        if (!isToggleOn) {
                            onEvent(ClockEvent.StopAutomaticClock)
                        }
                    }
                    if (showTimeSelector) {
                        TimeSelector {
                            onEvent(ClockEvent.UpdateClock(it))
                        }
                    } else {
                        onEvent(ClockEvent.StartAutomaticClock)
                    }
                    NormalTime(clockState.normalTime)
                    BerlinClock(clockState = clockState)
                }
            }
        }
    }
}

@Composable
fun NormalTime(time: String) {
    Text(
        modifier = Modifier
            .padding(15.dp)
            .semantics {
                contentDescription = NORMAL_TIME
            },
        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
        text = time,
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true)
@Composable
fun ClockPreview() {
    BerlinClockScreen(
        state = MutableStateFlow(
            ClockState()
        ), onEvent = {}
    )
}