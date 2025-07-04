package com.anonymous.berlinclock.domain.usecase

import app.cash.turbine.test
import com.anonymous.berlinclock.domain.model.BerlinClock
import com.anonymous.berlinclock.util.BOTTOM_MINUTE_LAMP_COUNT
import com.anonymous.berlinclock.util.HOUR_LAMP_COUNT
import com.anonymous.berlinclock.util.HOUR_MAX_VALUE
import com.anonymous.berlinclock.util.LampColour
import com.anonymous.berlinclock.util.MESSAGE_INPUT_BETWEEN_0_AND_23
import com.anonymous.berlinclock.util.MESSAGE_INPUT_BETWEEN_0_AND_59
import com.anonymous.berlinclock.util.TIME_FORMAT
import com.anonymous.berlinclock.util.TIME_MAX_VALUE
import com.anonymous.berlinclock.util.TIME_MIN_VALUE
import com.anonymous.berlinclock.util.TOP_MINUTE_LAMP_COUNT
import com.anonymous.berlinclock.util.getTimeMillis
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import org.joda.time.DateTime
import org.joda.time.DateTimeUtils
import org.joda.time.format.DateTimeFormat
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class GetBerlinClockDataTest {

    private lateinit var getBerlinClockData: GetBerlinClockData

    @Before
    fun setup() {
        getBerlinClockData = GetBerlinClockData()
    }

    @Test
    fun `getSeconds throws exception when input is negative`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            getBerlinClockData.getSeconds(TIME_MIN_VALUE - 1)
        }
        assertThat(exception).hasMessageThat().contains(MESSAGE_INPUT_BETWEEN_0_AND_59)
    }

    @Test
    fun `getSeconds throws exception when input is greater than 59`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            getBerlinClockData.getSeconds(TIME_MAX_VALUE + 1)
        }
        assertThat(exception).hasMessageThat().contains(MESSAGE_INPUT_BETWEEN_0_AND_59)
    }

    @Test
    fun `getSeconds returns lamp is OFF for all the odd seconds`() {
        (TIME_MIN_VALUE + 1..TIME_MAX_VALUE step 2).forEach {
            val result = getBerlinClockData.getSeconds(it)
            assertThat(result).isEqualTo(LampColour.OFF)
        }
    }

    @Test
    fun `getSeconds returns lamp is ON for all the even seconds`() {
        (TIME_MIN_VALUE..TIME_MAX_VALUE - 1 step 2).forEach {
            val result = getBerlinClockData.getSeconds(it)
            assertThat(result).isEqualTo(LampColour.YELLOW)
        }
    }

    @Test
    fun `getTopHours throws exception when input is negative`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            getBerlinClockData.getTopHours(TIME_MIN_VALUE - 1)
        }
        assertThat(exception).hasMessageThat().contains(MESSAGE_INPUT_BETWEEN_0_AND_23)
    }

    @Test
    fun `getTopHours throws exception when input greater than 23`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            getBerlinClockData.getTopHours(HOUR_MAX_VALUE + 1)
        }
        assertThat(exception).hasMessageThat().contains(MESSAGE_INPUT_BETWEEN_0_AND_23)
    }

    @Test
    fun `getTopHours returns all lamps are OFF at midnight - 0 hour`() {
        val expectedResult = List(HOUR_LAMP_COUNT) { LampColour.OFF }
        assertThat(getBerlinClockData.getTopHours(hour = 0)).isEqualTo(expectedResult)
    }

    @Test
    fun `getTopHours returns first lamp as RED when the hours is 5`() {
        val expectedResult = MutableList(HOUR_LAMP_COUNT) { LampColour.OFF }
        expectedResult[0] = LampColour.RED
        assertThat(getBerlinClockData.getTopHours(hour = 5)).isEqualTo(expectedResult)
    }

    @Test
    fun `getTopHours returns first lamp as RED when the hour is from 5 to 9`() {
        val expectedResult = MutableList(HOUR_LAMP_COUNT) { LampColour.OFF }
        expectedResult[0] = LampColour.RED
        (5..9).forEach {
            assertThat(getBerlinClockData.getTopHours(it)).isEqualTo(expectedResult)
        }
    }

    @Test
    fun `getTopHours returns first two lamps as RED when the hours is 10`() {
        val expectedResult = MutableList(HOUR_LAMP_COUNT) { LampColour.OFF }
        expectedResult.apply {
            this[0] = LampColour.RED
            this[1] = LampColour.RED
        }
        assertThat(getBerlinClockData.getTopHours(hour = 10)).isEqualTo(expectedResult)
    }

    @Test
    fun `getTopHours returns first two lamp as RED when the hour is from 10 to 14`() {
        val expectedResult = MutableList(HOUR_LAMP_COUNT) { LampColour.OFF }
        expectedResult.apply {
            this[0] = LampColour.RED
            this[1] = LampColour.RED
        }
        (10..14).forEach {
            assertThat(getBerlinClockData.getTopHours(it)).isEqualTo(expectedResult)
        }
    }

    @Test
    fun `getTopHours returns first three lamp as RED when the hour is from 15 to 19`() {
        val expectedResult = MutableList(HOUR_LAMP_COUNT) { LampColour.OFF }
        expectedResult.apply {
            this[0] = LampColour.RED
            this[1] = LampColour.RED
            this[2] = LampColour.RED
        }
        (15..19).forEach {
            assertThat(getBerlinClockData.getTopHours(it)).isEqualTo(expectedResult)
        }
    }

    @Test
    fun `getTopHours returns all top hour lamp as RED when the hour is from 20 to 23`() {
        val expectedResult = MutableList(HOUR_LAMP_COUNT) { LampColour.RED }
        (20..23).forEach {
            assertThat(getBerlinClockData.getTopHours(it)).isEqualTo(expectedResult)
        }
    }

    @Test
    fun `getBottomHour throws exception when input is negative`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            getBerlinClockData.getBottomHour(TIME_MIN_VALUE - 1)
        }
        assertThat(exception).hasMessageThat().contains(MESSAGE_INPUT_BETWEEN_0_AND_23)
    }

    @Test
    fun `getBottomHour throws exception when input greater than 23`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            getBerlinClockData.getBottomHour(HOUR_MAX_VALUE + 1)
        }
        assertThat(exception).hasMessageThat().contains(MESSAGE_INPUT_BETWEEN_0_AND_23)
    }

    @Test
    fun `getBottomHour returns all lamps are OFF when the reminder for the hours divided by 5 is 0`() {
        val expectedResult = List(HOUR_LAMP_COUNT) { LampColour.OFF }
        (0..23 step 5).forEach {
            assertThat(getBerlinClockData.getBottomHour(it)).isEqualTo(expectedResult)
        }
    }

    @Test
    fun `getBottomHour returns first lamp as RED when the reminder for the hours divided by 5 is 1`() {
        val expectedResult = MutableList(HOUR_LAMP_COUNT) { LampColour.OFF }
        expectedResult[0] = LampColour.RED
        (1..23 step 5).forEach {
            assertThat(getBerlinClockData.getBottomHour(it)).isEqualTo(expectedResult)
        }
    }

    @Test
    fun `getBottomHour returns first two bottom lamp as RED when the reminder for the hours divided by 5 is 2`() {
        val expectedResult = MutableList(HOUR_LAMP_COUNT) { LampColour.OFF }
        expectedResult.apply {
            this[0] = LampColour.RED
            this[1] = LampColour.RED
        }
        (2..23 step 5).forEach {
            assertThat(getBerlinClockData.getBottomHour(it)).isEqualTo(expectedResult)
        }
    }

    @Test
    fun `getBottomHour returns first two bottom lamp as RED when the reminder for the hours divided by 5 is 3`() {
        val expectedResult = MutableList(HOUR_LAMP_COUNT) { LampColour.OFF }
        expectedResult.apply {
            this[0] = LampColour.RED
            this[1] = LampColour.RED
            this[2] = LampColour.RED
        }
        (3..23 step 5).forEach {
            assertThat(getBerlinClockData.getBottomHour(it)).isEqualTo(expectedResult)
        }
    }

    @Test
    fun `getBottomHour returns all the four bottom lamp as RED when reminder for the hours divided by 5 is 4`() {
        val expectedResult = MutableList(HOUR_LAMP_COUNT) { LampColour.RED }
        (4..23 step 5).forEach {
            assertThat(getBerlinClockData.getBottomHour(it)).isEqualTo(expectedResult)
        }
    }

    @Test
    fun `getTopMinute throws exception when input is negative`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            getBerlinClockData.getTopMinute(TIME_MIN_VALUE - 1)
        }
        assertThat(exception).hasMessageThat().contains(MESSAGE_INPUT_BETWEEN_0_AND_59)
    }

    @Test
    fun `getTopMinute throws exception when input greater than 59`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            getBerlinClockData.getTopMinute(TIME_MAX_VALUE + 1)
        }
        assertThat(exception).hasMessageThat().contains(MESSAGE_INPUT_BETWEEN_0_AND_59)
    }

    @Test
    fun `getTopMinute returns all the lamps are OFF when minutes is in the range from 0 to 4`() {
        val expectedResult = MutableList(TOP_MINUTE_LAMP_COUNT) { LampColour.OFF }
        (0..4).forEach {
            assertThat(getBerlinClockData.getTopMinute(it)).isEqualTo(expectedResult)
        }
    }

    @Test
    fun `getTopMinute returns first lamp as YELLOW when minutes is in the range from 5 to 9`() {
        val expectedResult = MutableList(TOP_MINUTE_LAMP_COUNT) { LampColour.OFF }
        expectedResult[0] = LampColour.YELLOW
        (5..9).forEach {
            assertThat(getBerlinClockData.getTopMinute(it)).isEqualTo(expectedResult)
        }
    }

    @Test
    fun `getTopMinute returns first two lamps as YELLOW when minutes is in the range from 10 to 14`() {
        val expectedResult = MutableList(TOP_MINUTE_LAMP_COUNT) { LampColour.OFF }
        expectedResult.apply {
            this[0] = LampColour.YELLOW
            this[1] = LampColour.YELLOW
        }
        (10..14).forEach {
            assertThat(getBerlinClockData.getTopMinute(it)).isEqualTo(expectedResult)
        }
    }

    @Test
    fun `getTopMinute returns first two lamps as YELLOW and third lamp as RED when minutes is in the range from 15 to 19`() {
        val expectedResult = MutableList(TOP_MINUTE_LAMP_COUNT) { LampColour.OFF }
        expectedResult.apply {
            this[0] = LampColour.YELLOW
            this[1] = LampColour.YELLOW
            this[2] = LampColour.RED
        }
        (15..19).forEach {
            assertThat(getBerlinClockData.getTopMinute(it)).isEqualTo(expectedResult)
        }
    }

    @Test
    fun `getTopMinute returns first four lamps are ON and third lamp as RED when minutes is in the range from 20 to 24`() {
        val expectedResult = MutableList(TOP_MINUTE_LAMP_COUNT) { LampColour.OFF }
        expectedResult.apply {
            this[0] = LampColour.YELLOW
            this[1] = LampColour.YELLOW
            this[2] = LampColour.RED
            this[3] = LampColour.YELLOW
        }
        (20..24).forEach {
            assertThat(getBerlinClockData.getTopMinute(it)).isEqualTo(expectedResult)
        }
    }

    @Test
    fun `getTopMinute returns first five lamps are ON and third lamp as RED when minutes is in the range from 25 to 29`() {
        val expectedResult = MutableList(TOP_MINUTE_LAMP_COUNT) { LampColour.OFF }
        expectedResult.apply {
            this[0] = LampColour.YELLOW
            this[1] = LampColour.YELLOW
            this[2] = LampColour.RED
            this[3] = LampColour.YELLOW
            this[4] = LampColour.YELLOW
        }
        (25..29).forEach {
            assertThat(getBerlinClockData.getTopMinute(it)).isEqualTo(expectedResult)
        }
    }

    @Test
    fun `getTopMinute returns first six lamps are ON and third and sixth lamps as RED when minutes is in the range from 30 to 34`() {
        val expectedResult = MutableList(TOP_MINUTE_LAMP_COUNT) { LampColour.OFF }
        expectedResult.apply {
            this[0] = LampColour.YELLOW
            this[1] = LampColour.YELLOW
            this[2] = LampColour.RED
            this[3] = LampColour.YELLOW
            this[4] = LampColour.YELLOW
            this[5] = LampColour.RED
        }
        (30..34).forEach {
            assertThat(getBerlinClockData.getTopMinute(it)).isEqualTo(expectedResult)
        }
    }

    @Test
    fun `getBottomMinute throws exception when input is negative`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            getBerlinClockData.getBottomMinute(TIME_MIN_VALUE - 1)
        }
        assertThat(exception).hasMessageThat().contains(MESSAGE_INPUT_BETWEEN_0_AND_59)
    }

    @Test
    fun `getBottomMinute throws exception when input greater than 59`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            getBerlinClockData.getBottomMinute(TIME_MAX_VALUE + 1)
        }
        assertThat(exception).hasMessageThat().contains(MESSAGE_INPUT_BETWEEN_0_AND_59)
    }

    @Test
    fun `getBottomMinute returns all the lamps are OFF when reminder for the minutes divided by 5 is 0`() {
        val expectedResult = MutableList(BOTTOM_MINUTE_LAMP_COUNT) { LampColour.OFF }
        (0..59 step 5).forEach {
            assertThat(getBerlinClockData.getBottomMinute(it)).isEqualTo(expectedResult)
        }
    }

    @Test
    fun `getBottomMinute returns first lamp as YELLOW when reminder for the minutes divided by 5 is 1`() {
        val expectedResult = MutableList(BOTTOM_MINUTE_LAMP_COUNT) { LampColour.OFF }
        expectedResult[0] = LampColour.YELLOW
        (1..59 step 5).forEach {
            assertThat(getBerlinClockData.getBottomMinute(it)).isEqualTo(expectedResult)
        }
    }

    @Test
    fun `getBottomMinute returns first two lamp as YELLOW when reminder for the minutes divided by 5 is 2`() {
        val expectedResult = MutableList(BOTTOM_MINUTE_LAMP_COUNT) { LampColour.OFF }
        for (i in 0..1) {
            expectedResult[i] = LampColour.YELLOW
        }
        (2..59 step 5).forEach {
            assertThat(getBerlinClockData.getBottomMinute(it)).isEqualTo(expectedResult)
        }
    }

    @Test
    fun `getBottomMinute returns first three lamp as YELLOW when reminder for the minutes divided by 5 is 3`() {
        val expectedResult = MutableList(BOTTOM_MINUTE_LAMP_COUNT) { LampColour.OFF }
        for (i in 0..2) {
            expectedResult[i] = LampColour.YELLOW
        }
        (3..59 step 5).forEach {
            assertThat(getBerlinClockData.getBottomMinute(it)).isEqualTo(expectedResult)
        }
    }

    @Test
    fun `getBottomMinute returns all the four lamps as YELLOW when reminder for the minutes divided by 5 is 4`() {
        val expectedResult = MutableList(BOTTOM_MINUTE_LAMP_COUNT) { LampColour.OFF }
        for (i in 0..3) {
            expectedResult[i] = LampColour.YELLOW
        }
        (4..59 step 5).forEach {
            assertThat(getBerlinClockData.getBottomMinute(it)).isEqualTo(expectedResult)
        }
    }

    @Test
    fun `returns aggregate berlin time with all lamp as OFF and seconds as YELLOW when time is 0 in string format`() {
        val timeString = "00:00:00"
        val expectedResult = BerlinClock(
            secondLamp = LampColour.YELLOW,
            topHourLamps = MutableList(HOUR_LAMP_COUNT) { LampColour.OFF },
            bottomMinuteLamps = MutableList(HOUR_LAMP_COUNT) { LampColour.OFF },
            topMinuteLamps = MutableList(TOP_MINUTE_LAMP_COUNT) { LampColour.OFF },
            bottomHourLamps = MutableList(BOTTOM_MINUTE_LAMP_COUNT) { LampColour.OFF },
            normalTime = timeString
        )
        val berlinClock = getBerlinClockData(time = timeString)
        assertThat(berlinClock).isEqualTo(expectedResult)
    }

    @Test
    fun `returns corresponding aggregate berlin time for random time in string format`() {
        val timeString = "21:16:01"
        val topHourLamps = MutableList(HOUR_LAMP_COUNT) { LampColour.RED }
        val bottomHourLamps = MutableList(HOUR_LAMP_COUNT) { LampColour.OFF }
        bottomHourLamps[0] = LampColour.RED
        val topMinuteLamps = MutableList(TOP_MINUTE_LAMP_COUNT) { LampColour.OFF }
        topMinuteLamps[0] = LampColour.YELLOW
        topMinuteLamps[1] = LampColour.YELLOW
        topMinuteLamps[2] = LampColour.RED
        val bottomMinuteLamps = MutableList(BOTTOM_MINUTE_LAMP_COUNT) { LampColour.OFF }
        bottomMinuteLamps[0] = LampColour.YELLOW
        val expectedResult = BerlinClock(
            secondLamp = LampColour.OFF,
            topHourLamps = topHourLamps,
            bottomHourLamps = bottomHourLamps,
            topMinuteLamps = topMinuteLamps,
            bottomMinuteLamps = bottomMinuteLamps,
            normalTime = timeString
        )
        val berlinClock = getBerlinClockData(time = timeString)
        assertThat(berlinClock).isEqualTo(expectedResult)
    }

    @Test
    fun `returns the berlin time corresponding to the system time automatically`() = runTest {
        val timeString = "01:20:29"
        val millis = timeString.getTimeMillis(TIME_FORMAT)
        val topHourLamps = MutableList(HOUR_LAMP_COUNT) { LampColour.OFF }
        val bottomHourLamps = MutableList(HOUR_LAMP_COUNT) { LampColour.OFF }
        bottomHourLamps[0] = LampColour.RED
        val topMinuteLamps = MutableList(TOP_MINUTE_LAMP_COUNT) { LampColour.OFF }
        topMinuteLamps[0] = LampColour.YELLOW
        topMinuteLamps[1] = LampColour.YELLOW
        topMinuteLamps[2] = LampColour.RED
        topMinuteLamps[3] = LampColour.YELLOW
        val bottomMinuteLamps = MutableList(BOTTOM_MINUTE_LAMP_COUNT) { LampColour.OFF }
        bottomMinuteLamps[0] = LampColour.OFF

        val expectedResult = BerlinClock(
            secondLamp = LampColour.OFF,
            topHourLamps = topHourLamps,
            bottomHourLamps = bottomHourLamps,
            topMinuteLamps = topMinuteLamps,
            bottomMinuteLamps = bottomMinuteLamps,
            normalTime = timeString
        )
        DateTimeUtils.setCurrentMillisFixed(millis)
        mockkStatic(DateTimeFormat::class)
        every { DateTimeFormat.forPattern(any()).print(DateTime()) } returns timeString

        getBerlinClockData().test<BerlinClock> {
            repeat(5) {
                assertThat(awaitItem()).isEqualTo(expectedResult)
            }
            cancelAndIgnoreRemainingEvents()
        }
        unmockkStatic(DateTimeFormat::class)
    }
}