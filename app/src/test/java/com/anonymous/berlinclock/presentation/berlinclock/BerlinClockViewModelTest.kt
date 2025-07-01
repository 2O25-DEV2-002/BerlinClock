package com.anonymous.berlinclock.presentation.berlinclock

import com.anonymous.berlinclock.MainDispatcherRule
import com.anonymous.berlinclock.domain.model.BerlinClock
import com.anonymous.berlinclock.domain.usecase.GetBerlinClockData
import com.anonymous.berlinclock.util.BOTTOM_MINUTE_LAMP_COUNT
import com.anonymous.berlinclock.util.BottomHourLamps
import com.anonymous.berlinclock.util.BottomMinuteLamps
import com.anonymous.berlinclock.util.HOUR_LAMP_COUNT
import com.anonymous.berlinclock.util.LampColour
import com.anonymous.berlinclock.util.SecondLamp
import com.anonymous.berlinclock.util.TOP_MINUTE_LAMP_COUNT
import com.anonymous.berlinclock.util.TopHourLamps
import com.anonymous.berlinclock.util.TopMinuteLamps
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BerlinClockViewModelTest {

    private lateinit var berlinClockViewModel: BerlinClockViewModel
    private var getBerlinClockDataUseCase = mockk<GetBerlinClockData>()
    private lateinit var secondLamp: SecondLamp
    private lateinit var topHourLamps: TopHourLamps
    private lateinit var bottomHourLamps: BottomHourLamps
    private lateinit var topMinuteLamps: TopMinuteLamps
    private lateinit var bottomMinuteLamps: BottomMinuteLamps
    private lateinit var normalTime: String
    private lateinit var expectedClockState: BerlinClock

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        secondLamp = LampColour.YELLOW
        topHourLamps = List(HOUR_LAMP_COUNT) { LampColour.RED }
        bottomHourLamps = List(HOUR_LAMP_COUNT) { LampColour.RED }
        topMinuteLamps = MutableList(TOP_MINUTE_LAMP_COUNT) { LampColour.YELLOW }
        bottomMinuteLamps = List(BOTTOM_MINUTE_LAMP_COUNT) { LampColour.YELLOW }
        normalTime = "11:12:08"
        berlinClockViewModel = BerlinClockViewModel(getBerlinClockDataUseCase)
        expectedClockState = BerlinClock(
            secondLamp = secondLamp,
            topHourLamps = topHourLamps,
            bottomHourLamps = bottomHourLamps,
            topMinuteLamps = topMinuteLamps,
            bottomMinuteLamps = bottomMinuteLamps,
            normalTime = normalTime
        )
    }

    @Test
    fun `check berlin clock lamps are updating for the automatic clock scenario`() = runTest {
        every { getBerlinClockDataUseCase() } returns flowOf(expectedClockState)
        berlinClockViewModel.onEvent(ClockEvent.StartAutomaticClock)
        val clockState = berlinClockViewModel.clockState.value
        verifyClockState(clockState)
    }

    @Test
    fun `check berlin clock lamps are updating for the manual clock scenario`() = runTest {
        every { getBerlinClockDataUseCase(any()) } returns expectedClockState
        berlinClockViewModel.onEvent(ClockEvent.UpdateClock(normalTime))
        val clockState = berlinClockViewModel.clockState.value
        verifyClockState(clockState)
    }

    @Test
    fun `check stopping automatic clock scenario`() = runTest {
        every { getBerlinClockDataUseCase() } returns flowOf(expectedClockState)
        berlinClockViewModel.onEvent(ClockEvent.StartAutomaticClock)
        val clockState = berlinClockViewModel.clockState.value
        verifyClockState(clockState)
        berlinClockViewModel.onEvent(ClockEvent.StopAutomaticClock)
        assertThat(berlinClockViewModel.clockState.value == ClockState()).isTrue()
    }

    private fun verifyClockState(clockState: ClockState) {
        clockState.let {
            assertThat(
                it.secondLamp == secondLamp &&
                        it.topHourLamps == topHourLamps &&
                        it.bottomHourLamps == bottomHourLamps &&
                        it.topMinuteLamps == topMinuteLamps &&
                        it.bottomMinuteLamps == bottomMinuteLamps &&
                        it.normalTime == normalTime
            ).isTrue()
        }
    }
}