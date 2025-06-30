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
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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
        val initialLampColor = "OFF-#FFFFFF"
        composeRule.onNodeWithTag(NORMAL_TIME).assertIsDisplayed()
        composeRule.onNodeWithTag("$SECOND_LAMP-$initialLampColor").assertIsDisplayed()

        repeat(HOUR_LAMP_COUNT) {
            composeRule.onNodeWithTag("${TOP_HOUR_LAMP}$it-$initialLampColor").assertIsDisplayed()
            composeRule.onNodeWithTag("${BOTTOM_HOUR_LAMP}$it-$initialLampColor")
                .assertIsDisplayed()
        }
        repeat(TOP_MINUTE_LAMP_COUNT) {
            composeRule.onNodeWithTag("${TOP_MIN_LAMP}$it-$initialLampColor").assertIsDisplayed()
        }
        repeat(BOTTOM_MINUTE_LAMP_COUNT) {
            composeRule.onNodeWithTag("${BOTTOM_MIN_LAMP}$it-$initialLampColor").assertIsDisplayed()
        }
    }

    @Test
    fun validateAllLampsAreOffWithWhiteBgColorForTheManualInputAtMidnight() {
        //Given
        val inputHour = "00"
        val inputMin = "00"
        val inputSec = "01"
        val timeString = "00:00:01"
        val secondLamp = LampColour.OFF
        val topHourLamps = MutableList(HOUR_LAMP_COUNT) { LampColour.OFF }
        val bottomHourLamps = MutableList(HOUR_LAMP_COUNT) { LampColour.OFF }
        val topMinLamps = MutableList(TOP_MINUTE_LAMP_COUNT) { LampColour.OFF }
        val bottomMinLamps = MutableList(BOTTOM_MINUTE_LAMP_COUNT) { LampColour.OFF }
        //When
        composeRule.onNodeWithContentDescription(TOGGLE).performClick()
        composeRule.onNodeWithContentDescription(HOUR_SELECTOR).performTextInput(inputHour)
        composeRule.onNodeWithContentDescription(MINUTE_SELECTOR).performTextInput(inputMin)
        composeRule.onNodeWithContentDescription(SECOND_SELECTOR).performTextInput(inputSec)
        composeRule.onNodeWithContentDescription(SHOW_BERLIN_TIME_BUTTON).performClick()
        //Then
        composeRule.onNodeWithTag(NORMAL_TIME).assertTextEquals(timeString)
        composeRule.onNodeWithTag("${SECOND_LAMP}-${secondLamp.name}-${secondLamp.color}")
            .assertIsDisplayed()
        repeat(HOUR_LAMP_COUNT) {
            topHourLamps.forEach { lamp ->
                composeRule.onNodeWithTag("${TOP_HOUR_LAMP}${it}-${lamp.name}-${lamp.color}")
                    .assertIsDisplayed()
            }
            bottomHourLamps.forEach { lamp ->
                composeRule.onNodeWithTag("${BOTTOM_HOUR_LAMP}$it-${lamp.name}-${lamp.color}")
                    .assertIsDisplayed()
            }


        }
        repeat(TOP_MINUTE_LAMP_COUNT) {
            topMinLamps.forEach { lamp ->
                composeRule.onNodeWithTag("${TOP_MIN_LAMP}$it-${lamp.name}-${lamp.color}")
                    .assertIsDisplayed()
            }

        }
        repeat(BOTTOM_MINUTE_LAMP_COUNT) {
            bottomMinLamps.forEach { lamp ->
                composeRule.onNodeWithTag("${BOTTOM_MIN_LAMP}$it-${lamp.name}-${lamp.color}")
                    .assertIsDisplayed()
            }
        }
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