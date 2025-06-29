package com.anonymous.berlinclock.presentation.berlinclock

import com.anonymous.berlinclock.util.LampColour
import com.anonymous.berlinclock.util.SecondLamp

data class ClockState(
    val secondLamp: SecondLamp = LampColour.OFF
)