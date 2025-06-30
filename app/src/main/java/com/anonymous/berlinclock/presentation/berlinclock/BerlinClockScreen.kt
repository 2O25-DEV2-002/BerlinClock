package com.anonymous.berlinclock.presentation.berlinclock

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.anonymous.berlinclock.util.BottomHourLamps
import com.anonymous.berlinclock.util.BottomMinuteLamps
import com.anonymous.berlinclock.util.TestTags.BOTTOM_HOUR_LAMP
import com.anonymous.berlinclock.util.TestTags.BOTTOM_MIN_LAMP
import com.anonymous.berlinclock.util.TestTags.NORMAL_TIME
import com.anonymous.berlinclock.util.TestTags.SECOND_LAMP
import com.anonymous.berlinclock.util.TestTags.TOP_HOUR_LAMP
import com.anonymous.berlinclock.util.TestTags.TOP_MIN_LAMP
import com.anonymous.berlinclock.util.TopHourLamps
import com.anonymous.berlinclock.util.TopMinuteLamps
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerlinClockScreen(
    state: StateFlow<ClockState>
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    ToggleButton()
                    NormalTime()
                    SecondsLamp()
                    DisplayHourLamps(clockState)
                    DisplayMinuteLamps(clockState)
                }
            }
        }
    }
}

@Composable
fun NormalTime() {
    Text(
        modifier = Modifier
            .padding(15.dp)
            .testTag(NORMAL_TIME),
        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
        text = "Time",
        textAlign = TextAlign.Center
    )
}

@Composable
fun SecondsLamp() {
    Box(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .size(80.dp)
            .clip(CircleShape)
            .border(2.dp, Color.DarkGray, CircleShape)
            .testTag(SECOND_LAMP)
            .background(Color.White)
    )
}

@Composable
fun Lamp(
    tag: String,
    size: Int = 60
) {
    Column(modifier = Modifier.padding(2.dp)) {
        Box(
            modifier = Modifier
                .testTag(tag)
                .padding(horizontal = 0.dp, vertical = 5.dp)
                .fillMaxWidth()
                .size(size.dp)
                .border(2.dp, Color.DarkGray, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
        )
    }
}

@Composable
fun TopHourLamps(modifier: Modifier, lamps: TopHourLamps) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(lamps.size) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center
            ) {
                Lamp(
                    tag = "$TOP_HOUR_LAMP$it"
                )
            }

        }
    }
}

@Composable
fun BottomHourLamps(modifier: Modifier, lamps: BottomHourLamps) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(lamps.size) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center
            ) {

                Lamp(
                    tag = "$BOTTOM_HOUR_LAMP$it"
                )
            }
        }
    }
}

@Composable
fun DisplayHourLamps(clockState: ClockState) {
    val modifier = Modifier.fillMaxWidth()
    TopHourLamps(modifier, clockState.topHourLamps)
    BottomHourLamps(modifier, clockState.bottomHourLamps)
}

@Composable
fun TopMinuteLamps(lamps: TopMinuteLamps) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(lamps.size) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center
            ) {
                Lamp(
                    tag = "$TOP_MIN_LAMP$it"
                )
            }
        }
    }
}

@Composable
fun BottomMinuteLamps(lamps: BottomMinuteLamps) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(lamps.size) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center
            ) {
                Lamp(
                    tag = "$BOTTOM_MIN_LAMP$it"
                )
            }
        }
    }
}

@Composable
fun DisplayMinuteLamps(clockState: ClockState) {
    TopMinuteLamps(clockState.topMinuteLamps)
    BottomMinuteLamps(clockState.bottomMinuteLamps)
}


@Preview(showBackground = true)
@Composable
fun ClockPreview() {
    BerlinClockScreen(
        state = MutableStateFlow(
            ClockState()
        )
    )
}
