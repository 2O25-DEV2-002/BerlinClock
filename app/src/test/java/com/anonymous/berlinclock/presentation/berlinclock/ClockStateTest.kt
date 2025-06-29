package com.anonymous.berlinclock.presentation.berlinclock

import com.anonymous.berlinclock.util.HOUR_LAMP_COUNT
import com.anonymous.berlinclock.util.LampColour
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ClockStateTest {
    private lateinit var clockState: ClockState

    @Before
    fun setup() {
        clockState = ClockState()
    }

    @Test
    fun `check secondLamp is initially OFF`() {
        assertThat(clockState.secondLamp).isEqualTo(LampColour.OFF)
    }

    @Test
    fun `check top hour lamps are initially OFF`() {
        assertThat(clockState.topHourLamps).isEqualTo(List(HOUR_LAMP_COUNT) { LampColour.OFF })
    }

    @Test
    fun `check bottom hour lamps are initially OFF`() {
        assertThat(clockState.bottomHourLamps).isEqualTo(List(HOUR_LAMP_COUNT) { LampColour.OFF })
    }
}