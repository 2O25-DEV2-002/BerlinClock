package com.anonymous.berlinclock.presentation.berlinclock

import com.anonymous.berlinclock.util.BOTTOM_MINUTE_LAMP_COUNT
import com.anonymous.berlinclock.util.BottomHourLamps
import com.anonymous.berlinclock.util.HOUR_LAMP_COUNT
import com.anonymous.berlinclock.util.LampColour
import com.anonymous.berlinclock.util.SecondLamp
import com.anonymous.berlinclock.util.TOP_MINUTE_LAMP_COUNT
import com.anonymous.berlinclock.util.TopHourLamps
import com.anonymous.berlinclock.util.TopMinuteLamps

data class ClockState(
    val secondLamp: SecondLamp = LampColour.OFF,
    val topHourLamps: TopHourLamps = List(HOUR_LAMP_COUNT) { LampColour.OFF },
    val bottomHourLamps: BottomHourLamps = List(HOUR_LAMP_COUNT) { LampColour.OFF },
    val topMinuteLamps: TopMinuteLamps = List(TOP_MINUTE_LAMP_COUNT) { LampColour.OFF },
    val bottomMinuteLamps: BottomHourLamps = List(BOTTOM_MINUTE_LAMP_COUNT) { LampColour.OFF },
)