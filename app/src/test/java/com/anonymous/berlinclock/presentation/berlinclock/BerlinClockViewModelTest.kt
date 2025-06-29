package com.anonymous.berlinclock.presentation.berlinclock

import com.anonymous.berlinclock.MainDispatcherRule
import com.anonymous.berlinclock.domain.model.BerlinClock
import com.anonymous.berlinclock.domain.usecase.GetBerlinClockData
import com.anonymous.berlinclock.util.BOTTOM_MINUTE_LAMP_COUNT
import com.anonymous.berlinclock.util.HOUR_LAMP_COUNT
import com.anonymous.berlinclock.util.LampColour
import com.anonymous.berlinclock.util.TOP_MINUTE_LAMP_COUNT
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class BerlinClockViewModelTest {

    private lateinit var berlinClockViewModel: BerlinClockViewModel
    private var getBerlinClockDataUseCase = mockk<GetBerlinClockData>()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `check berlin clock lamps are updating for the automatic clock scenario`() = runTest {
        berlinClockViewModel = BerlinClockViewModel(getBerlinClockDataUseCase)
        val secondLamp = LampColour.YELLOW
        val topHourLamps = List(HOUR_LAMP_COUNT) { LampColour.RED }
        val bottomHourLamps = List(HOUR_LAMP_COUNT) { LampColour.RED }
        val topMinuteLamps = List(TOP_MINUTE_LAMP_COUNT) { LampColour.YELLOW }
        val bottomMinuteLamps = List(BOTTOM_MINUTE_LAMP_COUNT) { LampColour.YELLOW }
        val normalTime = "11:12:08"
        val expectedClockState = BerlinClock(
            secondLamp = secondLamp,
            topHourLamps = topHourLamps,
            bottomHourLamps = bottomHourLamps,
            topMinuteLamps = topMinuteLamps,
            bottomMinuteLamps = bottomMinuteLamps,
            normalTime = normalTime
        )
        every { getBerlinClockDataUseCase() } returns flowOf(expectedClockState)
        berlinClockViewModel.onEvent(ClockEvent.StartAutomaticClock)
        val clockState = berlinClockViewModel.clockState.value
        clockState.let {
            assertThat(it.secondLamp).isEqualTo(secondLamp)
            assertThat(it.topHourLamps).isEqualTo(topHourLamps)
            assertThat(it.bottomHourLamps).isEqualTo(bottomHourLamps)
            assertThat(it.topMinuteLamps).isEqualTo(topMinuteLamps)
            assertThat(it.bottomMinuteLamps).isEqualTo(bottomMinuteLamps)
            assertThat(it.normalTime).isEqualTo(normalTime)
        }
    }

    @Test
    fun `check berlin clock lamps are updating for the manual clock scenario`() = runTest {
        berlinClockViewModel = BerlinClockViewModel(getBerlinClockDataUseCase)
        val secondLamp = LampColour.YELLOW
        val topHourLamps = List(HOUR_LAMP_COUNT) { LampColour.RED }
        val bottomHourLamps = List(HOUR_LAMP_COUNT) { LampColour.RED }
        val topMinuteLamps = List(TOP_MINUTE_LAMP_COUNT) { LampColour.YELLOW }
        val bottomMinuteLamps = List(BOTTOM_MINUTE_LAMP_COUNT) { LampColour.YELLOW }
        val normalTime = "11:12:08"
        val expectedClock = BerlinClock(
            secondLamp = secondLamp,
            topHourLamps = topHourLamps,
            bottomHourLamps = bottomHourLamps,
            topMinuteLamps = topMinuteLamps,
            bottomMinuteLamps = bottomMinuteLamps,
            normalTime = normalTime
        )
        every { getBerlinClockDataUseCase(any()) } returns expectedClock
        berlinClockViewModel.onEvent(ClockEvent.UpdateClock("11:12:08"))
        val clockState = berlinClockViewModel.clockState.value
        clockState.let {
            assertThat(it.secondLamp).isEqualTo(secondLamp)
            assertThat(it.topHourLamps).isEqualTo(topHourLamps)
            assertThat(it.bottomHourLamps).isEqualTo(bottomHourLamps)
            assertThat(it.topMinuteLamps).isEqualTo(topMinuteLamps)
            assertThat(it.bottomMinuteLamps).isEqualTo(bottomMinuteLamps)
            assertThat(it.normalTime).isEqualTo(normalTime)
        }
    }
}