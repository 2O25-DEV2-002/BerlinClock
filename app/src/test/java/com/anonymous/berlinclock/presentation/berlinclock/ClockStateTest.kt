package com.anonymous.berlinclock.presentation.berlinclock

import com.anonymous.berlinclock.util.LampColour
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ClockStateTest {
    private lateinit var clockState: ClockState

    @Test
    fun `check secondLamp is initially OFF`() {
        clockState = ClockState()
        assertThat(clockState.secondLamp).isEqualTo(LampColour.OFF)
    }
}