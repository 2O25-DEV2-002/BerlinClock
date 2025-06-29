package com.anonymous.berlinclock.domain.usecase

import com.anonymous.berlinclock.util.HOUR_MAX_VALUE
import com.anonymous.berlinclock.util.LampColour
import com.anonymous.berlinclock.util.MESSAGE_INPUT_BETWEEN_0_AND_23
import com.anonymous.berlinclock.util.MESSAGE_INPUT_BETWEEN_0_AND_59
import com.anonymous.berlinclock.util.TIME_MAX_VALUE
import com.anonymous.berlinclock.util.TIME_MIN_VALUE
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test
import kotlin.test.assertFailsWith

class GetBerlinClockDataTest {

    private lateinit var getBerlinClockData: GetBerlinClockData

    @Test
    fun `getSeconds throws exception when input is negative`() {
        getBerlinClockData = GetBerlinClockData()
        val exception = assertThrows(IllegalArgumentException::class.java) {
            getBerlinClockData.getSeconds(TIME_MIN_VALUE - 1)
        }
        assertThat(exception).hasMessageThat().contains(MESSAGE_INPUT_BETWEEN_0_AND_59)
    }

    @Test
    fun `getSeconds throws exception when input is greater than 59`() {
        getBerlinClockData = GetBerlinClockData()
        val exception = assertThrows(IllegalArgumentException::class.java) {
            getBerlinClockData.getSeconds(TIME_MAX_VALUE + 1)
        }
        assertThat(exception).hasMessageThat().contains(MESSAGE_INPUT_BETWEEN_0_AND_59)
    }

    @Test
    fun `getSeconds returns lamp is OFF for all the odd seconds`() {
        getBerlinClockData = GetBerlinClockData()
        (TIME_MIN_VALUE + 1..TIME_MAX_VALUE step 2).forEach {
            val result = getBerlinClockData.getSeconds(it)
            assertThat(result).isEqualTo(LampColour.OFF)
        }
    }

    @Test
    fun `getSeconds returns lamp is ON for all the even seconds`() {
        getBerlinClockData = GetBerlinClockData()
        (TIME_MIN_VALUE..TIME_MAX_VALUE - 1 step 2).forEach {
            val result = getBerlinClockData.getSeconds(it)
            assertThat(result).isEqualTo(LampColour.YELLOW)
        }
    }

    @Test
    fun `getTopHours throws exception when input is negative`() {
        getBerlinClockData = GetBerlinClockData()
        val exception = assertFailsWith<IllegalArgumentException> {
            getBerlinClockData.getTopHours(TIME_MIN_VALUE - 1)
        }
        assertThat(exception).hasMessageThat().contains(MESSAGE_INPUT_BETWEEN_0_AND_23)
    }

    @Test
    fun `getTopHours throws exception when input greater than 23`() {
        getBerlinClockData = GetBerlinClockData()
        val exception = assertFailsWith<IllegalArgumentException> {
            getBerlinClockData.getTopHours(HOUR_MAX_VALUE + 1)
        }
        assertThat(exception).hasMessageThat().contains(MESSAGE_INPUT_BETWEEN_0_AND_23)
    }
}