package com.anonymous.berlinclock.presentation.berlinclock.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.anonymous.berlinclock.presentation.berlinclock.ClockState
import com.anonymous.berlinclock.util.BottomHourLamps
import com.anonymous.berlinclock.util.BottomMinuteLamps
import com.anonymous.berlinclock.util.LampColour
import com.anonymous.berlinclock.util.TestTags
import com.anonymous.berlinclock.util.TopHourLamps
import com.anonymous.berlinclock.util.TopMinuteLamps
import androidx.core.graphics.toColorInt
import com.anonymous.berlinclock.util.getLampTag

@Composable
fun BerlinClock(clockState: ClockState) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        SecondsLamp(clockState.secondLamp)
        DisplayHourLamps(clockState)
        DisplayMinuteLamps(clockState)
    }
}

@Composable
fun SecondsLamp(lamp: LampColour) {
    Box(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .size(80.dp)
            .clip(CircleShape)
            .border(2.dp, Color.DarkGray, CircleShape)
            .testTag(TestTags.SECOND_LAMP.getLampTag(lamp.name, lamp.color))
            .background(Color(lamp.color.toColorInt()))
    )
}

@Composable
fun DisplayHourLamps(clockState: ClockState) {
    val modifier = Modifier.fillMaxWidth()
    TopHourLamps(modifier, clockState.topHourLamps)
    BottomHourLamps(modifier, clockState.bottomHourLamps)
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
                val lamp = lamps[it]
                Lamp(
                    tag = "${TestTags.TOP_HOUR_LAMP}$it".getLampTag(lamp.name, lamp.color),
                    lamp = lamp
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
                val lamp = lamps[it]
                Lamp(
                    tag = "${TestTags.BOTTOM_HOUR_LAMP}$it".getLampTag(lamp.name, lamp.color),
                    lamp = lamp
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
                val lamp = lamps[it]
                Lamp(
                    tag = "${TestTags.TOP_MIN_LAMP}${it}".getLampTag(lamp.name, lamp.color),
                    lamp = lamp
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
                val lamp = lamps[it]
                Lamp(
                    tag = "${TestTags.BOTTOM_MIN_LAMP}$it".getLampTag(lamp.name, lamp.color),
                    lamp = lamp
                )
            }
        }
    }
}