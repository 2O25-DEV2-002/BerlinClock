package com.anonymous.berlinclock.domain.model

import com.anonymous.berlinclock.util.BottomHourLamps
import com.anonymous.berlinclock.util.BottomMinuteLamps
import com.anonymous.berlinclock.util.SecondLamp
import com.anonymous.berlinclock.util.TopHourLamps
import com.anonymous.berlinclock.util.TopMinuteLamps

data class BerlinClock(
    val secondLamp: SecondLamp,
    val topHourLamps: TopHourLamps,
    val bottomHourLamps: BottomHourLamps,
    val topMinuteLamps: TopMinuteLamps,
    val bottomMinuteLamps: BottomMinuteLamps,
    val normalTime: String
)