package com.anonymous.berlinclock.presentation.berlinclock

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.espresso.Espresso
import com.anonymous.berlinclock.MainActivity
import com.anonymous.berlinclock.presentation.berlinclock.navgraph.BerlinClockNavGraph
import com.anonymous.berlinclock.ui.theme.BerlinClockTheme
import com.anonymous.berlinclock.util.BOTTOM_MINUTE_LAMP_COUNT
import com.anonymous.berlinclock.util.EMPTY_STRING
import com.anonymous.berlinclock.util.HOUR_LAMP_COUNT
import com.anonymous.berlinclock.util.HOUR_MAX_VALUE
import com.anonymous.berlinclock.util.LampColour
import com.anonymous.berlinclock.util.TIME_MAX_VALUE
import com.anonymous.berlinclock.util.TIME_MIN_VALUE
import com.anonymous.berlinclock.util.TOP_MINUTE_LAMP_COUNT
import com.anonymous.berlinclock.util.TestTags.BOTTOM_HOUR_LAMP
import com.anonymous.berlinclock.util.TestTags.BOTTOM_MIN_LAMP
import com.anonymous.berlinclock.util.TestTags.HOUR_SELECTOR
import com.anonymous.berlinclock.util.TestTags.MINUTE_SELECTOR
import com.anonymous.berlinclock.util.TestTags.NORMAL_TIME
import com.anonymous.berlinclock.util.TestTags.SECOND_LAMP
import com.anonymous.berlinclock.util.TestTags.SECOND_SELECTOR
import com.anonymous.berlinclock.util.TestTags.SHOW_BERLIN_TIME_BUTTON
import com.anonymous.berlinclock.util.TestTags.TIME_SELECTOR
import com.anonymous.berlinclock.util.TestTags.TOGGLE
import com.anonymous.berlinclock.util.TestTags.TOP_BAR
import com.anonymous.berlinclock.util.TestTags.TOP_HOUR_LAMP
import com.anonymous.berlinclock.util.TestTags.TOP_MIN_LAMP
import com.anonymous.berlinclock.util.getLampTag
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.joda.time.DateTime
import org.joda.time.DateTimeUtils
import org.joda.time.format.DateTimeFormat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class BerlinClockScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        setContentForActivity()
    }

    private fun setContentForActivity() {
        composeRule.activity.setContent {
            BerlinClockTheme {
                BerlinClockNavGraph()
            }
        }
    }

    @Test
    fun validateTopBarIsVisible() {
        composeRule.onNodeWithTag(TOP_BAR).assertIsDisplayed()
    }

    @Test
    fun validateToggleSwitchIsVisible() {
        composeRule.onNodeWithContentDescription(TOGGLE).assertIsDisplayed()
    }

    @Test
    fun validateBerlinClockIsVisibleInitially() {
        val secondLampName = "YELLOW"
        val secondLampColor = "#FFFF00"

        composeRule.onNodeWithContentDescription(TOGGLE).performClick()
        composeRule.onNodeWithContentDescription(TOGGLE).assertIsOff()

        composeRule.onNodeWithTag(NORMAL_TIME).assertIsDisplayed()
        verifyLampDetails(lampName = secondLampName, lampColor = secondLampColor)
        verifyLampDetails(HOUR_LAMP_COUNT, TOP_HOUR_LAMP)
        verifyLampDetails(HOUR_LAMP_COUNT, BOTTOM_HOUR_LAMP)
        verifyLampDetails(TOP_MINUTE_LAMP_COUNT, TOP_MIN_LAMP)
        verifyLampDetails(BOTTOM_MINUTE_LAMP_COUNT, BOTTOM_MIN_LAMP)
    }

    @Test
    fun validateAllLampsAreOffWithWhiteBgColorExceptSecondLampForManualInput() {
        val inputHour = "00"
        val inputMin = "00"
        val inputSec = "00"
        val timeString = "00:00:00"
        val secondLamp = LampColour.YELLOW
        val topHourLamps = MutableList(HOUR_LAMP_COUNT) { LampColour.OFF }
        val bottomHourLamps = MutableList(HOUR_LAMP_COUNT) { LampColour.OFF }
        val topMinLamps = MutableList(TOP_MINUTE_LAMP_COUNT) { LampColour.OFF }
        val bottomMinLamps = MutableList(BOTTOM_MINUTE_LAMP_COUNT) { LampColour.OFF }

        callShowBerlinTimeManually(inputHour, inputMin, inputSec)

        composeRule.onNodeWithTag(NORMAL_TIME).assertTextEquals(timeString)
        verifyLampDetails(lampName = secondLamp.name, lampColor = secondLamp.color)
        verifyLampDetails(topHourLamps, TOP_HOUR_LAMP)
        verifyLampDetails(bottomHourLamps, BOTTOM_HOUR_LAMP)
        verifyLampDetails(topMinLamps, TOP_MIN_LAMP)
        verifyLampDetails(bottomMinLamps, BOTTOM_MIN_LAMP)
    }

    @Test
    fun validateTimeSelectorIsDisplayedWhenAutomaticClockIsOff() {
        composeRule.onNodeWithContentDescription(TOGGLE).performClick()
        composeRule.onNodeWithContentDescription(TOGGLE).assertIsOff()
        timeSelectorUiComponents.forEach {
            composeRule.onNodeWithContentDescription(it).assertIsDisplayed()
        }
    }

    @Test
    fun validateTimeSelectorIsHiddenAndAutomaticToggleIsOnInitially() {
        composeRule.onNodeWithContentDescription(TOGGLE).assertIsOn()
        timeSelectorUiComponents.forEach {
            composeRule.onNodeWithContentDescription(it).assertDoesNotExist()
        }
    }

    @Test
    fun validateShowBerlinTimeButtonIsDisabledUntilAllThreeHourMinuteAndSecondFieldsAreFilled() {
        composeRule.onNodeWithContentDescription(TOGGLE).performClick()
        composeRule.onNodeWithContentDescription(TOGGLE).assertIsOff()
        timeSelectorInputFields.forEach { contentDesc ->
            composeRule.onNodeWithContentDescription(contentDesc).performTextInput(EMPTY_STRING)
        }
        composeRule.onNodeWithContentDescription(SHOW_BERLIN_TIME_BUTTON)
            .assertIsNotEnabled()
        composeRule.onNodeWithContentDescription(HOUR_SELECTOR).performTextInput("1")
        composeRule.onNodeWithContentDescription(SHOW_BERLIN_TIME_BUTTON)
            .assertIsNotEnabled()
        composeRule.onNodeWithContentDescription(MINUTE_SELECTOR).performTextInput("1")
        composeRule.onNodeWithContentDescription(SHOW_BERLIN_TIME_BUTTON)
            .assertIsNotEnabled()
        composeRule.onNodeWithContentDescription(SECOND_SELECTOR).performTextInput("1")
        composeRule.onNodeWithContentDescription(SHOW_BERLIN_TIME_BUTTON)
            .assertIsEnabled()
    }

    @Test
    fun checkTimeSelectorFieldsAreUpdatingForInput0() {
        val expectedValue = "0"
        composeRule.onNodeWithContentDescription(TOGGLE).performClick()
        timeSelectorInputFields.forEach {
            composeRule.onNodeWithContentDescription(it).assertIsDisplayed()
            composeRule.onNodeWithContentDescription(it).performTextReplacement(expectedValue)
            composeRule.onNodeWithContentDescription(it).assertTextEquals(expectedValue)
        }
    }

    @Test
    fun checkTimeSelectorFieldsAreUpdatingForInput1() {
        val expectedValue = "1"
        composeRule.onNodeWithContentDescription(TOGGLE).performClick()
        timeSelectorInputFields.forEach {
            composeRule.onNodeWithContentDescription(it).assertIsDisplayed()
            composeRule.onNodeWithContentDescription(it).performTextReplacement(expectedValue)
            composeRule.onNodeWithContentDescription(it).assertTextEquals(expectedValue)
        }
    }

    @Test
    fun checkTimeSelectorFieldsAcceptsOnlyDigits() {
        val inputValues = listOf(",", ".", "ab")
        val digitInput = "14"
        composeRule.onNodeWithContentDescription(TOGGLE).performClick()
        timeSelectorInputFields.forEach {
            composeRule.onNodeWithContentDescription(it).assertIsDisplayed()
            composeRule.onNodeWithContentDescription(it).performTextReplacement(digitInput)
            composeRule.onNodeWithContentDescription(it).assertTextEquals(digitInput)
        }
        timeSelectorInputFields.forEach {
            inputValues.forEach { input ->
                composeRule.onNodeWithContentDescription(it).assertIsDisplayed()
                composeRule.onNodeWithContentDescription(it).performTextReplacement(input)
                composeRule.onNodeWithContentDescription(it).assertTextEquals(digitInput)
            }
        }
    }

    @Test
    fun checkTimeSelectorFieldsAcceptsOnlyMaxTwoDigitNumbers() {
        composeRule.onNodeWithContentDescription(TOGGLE).performClick()
        timeSelectorInputFields.forEach {
            listOf("0", "00").forEach { input ->
                composeRule.onNodeWithContentDescription(it)
                    .performTextReplacement(input.toString())
                composeRule.onNodeWithContentDescription(it).assertTextEquals(input)
            }
        }
        timeSelectorInputFields.forEach {
            composeRule.onNodeWithContentDescription(it).performTextReplacement("000")
            composeRule.onNodeWithContentDescription(it).assertTextEquals("00")
        }
    }

    @Test
    fun checkTimeSelectorFieldMaxValues() {
        val hourMaxValue = HOUR_MAX_VALUE
        val minuteMaxValue = TIME_MAX_VALUE
        val secondsMaxValue = TIME_MAX_VALUE
        composeRule.onNodeWithContentDescription(TOGGLE).performClick()
        HOUR_SELECTOR.let {
            (hourMaxValue..24).forEach { input ->
                composeRule.onNodeWithContentDescription(it)
                    .performTextReplacement(input.toString())
                composeRule.onNodeWithContentDescription(it)
                    .assertTextEquals(hourMaxValue.toString())
            }
        }

        MINUTE_SELECTOR.let {
            (minuteMaxValue..60).forEach { input ->
                composeRule.onNodeWithContentDescription(it)
                    .performTextReplacement(input.toString())
                composeRule.onNodeWithContentDescription(it)
                    .assertTextEquals(minuteMaxValue.toString())
            }
        }

        SECOND_SELECTOR.let {
            (secondsMaxValue..60).forEach { input ->
                composeRule.onNodeWithContentDescription(it)
                    .performTextReplacement(input.toString())
                composeRule.onNodeWithContentDescription(it)
                    .assertTextEquals(secondsMaxValue.toString())
            }
        }
    }

    @Test
    fun checkTimeSelectorFieldMinValues() {
        val minValue = TIME_MIN_VALUE
        composeRule.onNodeWithContentDescription(TOGGLE).performClick()
        timeSelectorInputFields.forEach {
            (minValue downTo -1).forEach { input ->
                composeRule.onNodeWithContentDescription(it)
                    .performTextReplacement(input.toString())
                composeRule.onNodeWithContentDescription(it)
                    .assertTextEquals(minValue.toString())
            }
        }
    }

    @Test
    fun checkTimeSelectorFieldsHandleTextClearance() {
        composeRule.onNodeWithContentDescription(TOGGLE).performClick()
        timeSelectorInputFields.forEach {
            composeRule.onNodeWithContentDescription(it)
                .performTextReplacement("0")
            composeRule.onNodeWithContentDescription(it)
                .performTextClearance()
        }
    }

    @Test
    fun validateLampColorsForARandomTime() {
        val inputHour = "23"
        val inputMin = "59"
        val inputSec = "00"
        val timeString = "23:59:00"
        val secondLamp = LampColour.YELLOW
        val topHourLamps = MutableList(HOUR_LAMP_COUNT) { LampColour.RED }
        val bottomHourLamps = MutableList(HOUR_LAMP_COUNT) { LampColour.RED }
        bottomHourLamps[3] = LampColour.OFF
        val topMinLamps = MutableList(TOP_MINUTE_LAMP_COUNT) { LampColour.YELLOW }
        (2..8 step 3).forEach {
            topMinLamps[it] = LampColour.RED
        }
        val bottomMinLamps = MutableList(BOTTOM_MINUTE_LAMP_COUNT) { LampColour.YELLOW }

        callShowBerlinTimeManually(inputHour, inputMin, inputSec)
        Espresso.closeSoftKeyboard()

        composeRule.onNodeWithTag(NORMAL_TIME).assertTextEquals(timeString)
        verifyLampDetails(lampName = secondLamp.name, lampColor = secondLamp.color)
        verifyLampDetails(topHourLamps, TOP_HOUR_LAMP)
        verifyLampDetails(bottomHourLamps, BOTTOM_HOUR_LAMP)
        verifyLampDetails(topMinLamps, TOP_MIN_LAMP)
        verifyLampDetails(bottomMinLamps, BOTTOM_MIN_LAMP)
    }

    @Test
    fun validateAutomaticClockStartAndStopScenario() {
        val timeString = "01:20:29"
        val millis = 1706212229312
        val secondLamp = LampColour.OFF
        val topHourLamps = MutableList(HOUR_LAMP_COUNT) { LampColour.OFF }
        val bottomHourLamps = MutableList(HOUR_LAMP_COUNT) { LampColour.OFF }
        bottomHourLamps[0] = LampColour.RED
        val topMinLamps = MutableList(TOP_MINUTE_LAMP_COUNT) { LampColour.OFF }
        topMinLamps[0] = LampColour.YELLOW
        topMinLamps[1] = LampColour.YELLOW
        topMinLamps[2] = LampColour.RED
        topMinLamps[3] = LampColour.YELLOW
        val bottomMinLamps = MutableList(BOTTOM_MINUTE_LAMP_COUNT) { LampColour.OFF }
        bottomMinLamps[0] = LampColour.OFF
        DateTimeUtils.setCurrentMillisFixed(millis)
        mockkStatic(DateTimeFormat::class)
        every { DateTimeFormat.forPattern(any()).print(DateTime()) } returns timeString
        composeRule.onNodeWithContentDescription(TOGGLE).assertIsOn()

        composeRule.onNodeWithTag(NORMAL_TIME).assertTextEquals(timeString)
        verifyLampDetails(lampName = secondLamp.name, lampColor = secondLamp.color)
        verifyLampDetails(topHourLamps, TOP_HOUR_LAMP)
        verifyLampDetails(bottomHourLamps, BOTTOM_HOUR_LAMP)
        verifyLampDetails(topMinLamps, TOP_MIN_LAMP)
        verifyLampDetails(bottomMinLamps, BOTTOM_MIN_LAMP)

        val timeStringStop = "00:00:00"
        val secondLampStop = LampColour.YELLOW
        val topHourLampsStop = MutableList(HOUR_LAMP_COUNT) { LampColour.OFF }
        val bottomHourLampsStop = MutableList(HOUR_LAMP_COUNT) { LampColour.OFF }
        val topMinLampsStop = MutableList(TOP_MINUTE_LAMP_COUNT) { LampColour.OFF }
        val bottomMinLampsStop = MutableList(BOTTOM_MINUTE_LAMP_COUNT) { LampColour.OFF }

        composeRule.onNodeWithContentDescription(TOGGLE).performClick()
        composeRule.onNodeWithContentDescription(TOGGLE).assertIsOff()

        composeRule.onNodeWithTag(NORMAL_TIME).assertTextEquals(timeStringStop)
        verifyLampDetails(lampName = secondLampStop.name, lampColor = secondLampStop.color)
        verifyLampDetails(topHourLampsStop, TOP_HOUR_LAMP)
        verifyLampDetails(bottomHourLampsStop, BOTTOM_HOUR_LAMP)
        verifyLampDetails(topMinLampsStop, TOP_MIN_LAMP)
        verifyLampDetails(bottomMinLampsStop, BOTTOM_MIN_LAMP)
        unmockkStatic(DateTimeFormat::class)
    }

    private fun verifyLampDetails(
        lamps: MutableList<LampColour>,
        tagPrefix: String
    ) {
        lamps.forEachIndexed { i, lamp ->
            composeRule.onNodeWithTag("${tagPrefix}${i}".getLampTag(lamp.name, lamp.color))
                .assertIsDisplayed()
        }
    }

    private fun verifyLampDetails(
        count: Int,
        tagPrefix: String,
        lampName: String = "OFF",
        lampColor: String = "#FFFFFF"
    ) {
        repeat(count) {
            composeRule.onNodeWithTag("${tagPrefix}$it".getLampTag(lampName, lampColor))
                .assertIsDisplayed()
        }
    }

    private fun verifyLampDetails(
        tagPrefix: String = SECOND_LAMP,
        lampName: String,
        lampColor: String
    ) {
        composeRule.onNodeWithTag(tagPrefix.getLampTag(lampName, lampColor)).assertIsDisplayed()
    }

    private fun callShowBerlinTimeManually(
        inputHour: String,
        inputMin: String,
        inputSec: String
    ) {
        composeRule.onNodeWithContentDescription(TOGGLE).performClick()
        composeRule.onNodeWithContentDescription(HOUR_SELECTOR).performTextInput(inputHour)
        composeRule.onNodeWithContentDescription(MINUTE_SELECTOR).performTextInput(inputMin)
        composeRule.onNodeWithContentDescription(SECOND_SELECTOR).performTextInput(inputSec)
        composeRule.onNodeWithContentDescription(SHOW_BERLIN_TIME_BUTTON).performClick()
    }

    companion object {
        val timeSelectorUiComponents =
            listOf(
                TIME_SELECTOR,
                HOUR_SELECTOR,
                MINUTE_SELECTOR,
                SECOND_SELECTOR,
                SHOW_BERLIN_TIME_BUTTON
            )

        val timeSelectorInputFields = listOf(
            HOUR_SELECTOR,
            MINUTE_SELECTOR,
            SECOND_SELECTOR,
        )
    }
}