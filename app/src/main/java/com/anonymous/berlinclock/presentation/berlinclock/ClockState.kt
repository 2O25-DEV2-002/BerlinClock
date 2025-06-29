package com.anonymous.berlinclock.presentation.berlinclock

import com.anonymous.berlinclock.util.BottomHourLamps
import com.anonymous.berlinclock.util.HOUR_LAMP_COUNT
import com.anonymous.berlinclock.util.LampColour
import com.anonymous.berlinclock.util.SecondLamp
import com.anonymous.berlinclock.util.TopHourLamps

data class ClockState(
    val secondLamp: SecondLamp = LampColour.OFF,
    val topHourLamps: TopHourLamps = List(HOUR_LAMP_COUNT) { LampColour.OFF },
    val bottomHourLamps: BottomHourLamps = List(HOUR_LAMP_COUNT) { LampColour.OFF }
)