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
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import com.anonymous.berlinclock.MainActivity
import com.anonymous.berlinclock.presentation.berlinclock.navgraph.BerlinClockNavGraph
import com.anonymous.berlinclock.ui.theme.BerlinClockTheme
import com.anonymous.berlinclock.util.BOTTOM_MINUTE_LAMP_COUNT
import com.anonymous.berlinclock.util.EMPTY_STRING
import com.anonymous.berlinclock.util.HOUR_LAMP_COUNT
import com.anonymous.berlinclock.util.TOP_MINUTE_LAMP_COUNT
import com.anonymous.berlinclock.util.TestTags
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
        composeRule.onNodeWithTag(TestTags.TOP_BAR).assertIsDisplayed()
    }

    @Test
    fun validateToggleSwitchIsVisible() {
        composeRule.onNodeWithContentDescription(TestTags.TOGGLE).assertIsDisplayed()
    }

    @Test
    fun validateBerlinClockIsVisibleInitially() {
        composeRule.onNodeWithTag(TestTags.NORMAL_TIME).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTags.SECOND_LAMP).assertIsDisplayed()
        repeat(HOUR_LAMP_COUNT) {
            composeRule.onNodeWithTag("${TestTags.TOP_HOUR_LAMP}$it").assertIsDisplayed()
            composeRule.onNodeWithTag("${TestTags.BOTTOM_HOUR_LAMP}$it").assertIsDisplayed()
        }
        repeat(TOP_MINUTE_LAMP_COUNT) {
            composeRule.onNodeWithTag("${TestTags.TOP_MIN_LAMP}$it").assertIsDisplayed()
        }
        repeat(BOTTOM_MINUTE_LAMP_COUNT) {
            composeRule.onNodeWithTag("${TestTags.BOTTOM_MIN_LAMP}$it").assertIsDisplayed()
        }
    }

    @Test
    fun validateTimeSelectorIsDisplayedWhenAutomaticClockIsOff() {
        composeRule.onNodeWithContentDescription(TestTags.TOGGLE).performClick()
        composeRule.onNodeWithContentDescription(TestTags.TOGGLE).assertIsOff()
        timeSelectorUiComponents.forEach {
            composeRule.onNodeWithContentDescription(it).assertIsDisplayed()
        }
    }

    @Test
    fun validateTimeSelectorIsHiddenAndAutomaticToggleIsOnInitially() {
        composeRule.onNodeWithContentDescription(TestTags.TOGGLE).assertIsOn()
        timeSelectorUiComponents.forEach {
            composeRule.onNodeWithContentDescription(it).assertDoesNotExist()
        }
    }

    @Test
    fun validateShowBerlinTimeButtonIsDisabledUntilAllThreeHourMinuteAndSecondFieldsAreFilled() {
        composeRule.onNodeWithContentDescription(TestTags.TOGGLE).performClick()
        composeRule.onNodeWithContentDescription(TestTags.TOGGLE).assertIsOff()
        TestTags.let {
            timeSelectorInputFields.forEach { contentDesc ->
                composeRule.onNodeWithContentDescription(contentDesc).performTextInput(EMPTY_STRING)
            }
            composeRule.onNodeWithContentDescription(TestTags.SHOW_BERLIN_TIME_BUTTON)
                .assertIsNotEnabled()
            composeRule.onNodeWithContentDescription(it.HOUR_SELECTOR).performTextInput("1")
            composeRule.onNodeWithContentDescription(TestTags.SHOW_BERLIN_TIME_BUTTON)
                .assertIsNotEnabled()
            composeRule.onNodeWithContentDescription(it.MINUTE_SELECTOR).performTextInput("1")
            composeRule.onNodeWithContentDescription(TestTags.SHOW_BERLIN_TIME_BUTTON)
                .assertIsNotEnabled()
            composeRule.onNodeWithContentDescription(it.SECOND_SELECTOR).performTextInput("1")
            composeRule.onNodeWithContentDescription(TestTags.SHOW_BERLIN_TIME_BUTTON)
                .assertIsEnabled()
        }
    }

    @Test
    fun checkSTimeSelectorFieldsAreUpdatingForInput0() {
        val expectedValue = "0"
        composeRule.onNodeWithContentDescription(TestTags.TOGGLE).performClick()
        timeSelectorInputFields.forEach {
            composeRule.onNodeWithContentDescription(it).assertIsDisplayed()
            composeRule.onNodeWithContentDescription(it).performTextReplacement(expectedValue)
            composeRule.onNodeWithContentDescription(it).assertTextEquals(expectedValue)
        }
    }

    @Test
    fun checkSTimeSelectorFieldsAreUpdatingForInput1() {
        val expectedValue = "1"
        composeRule.onNodeWithContentDescription(TestTags.TOGGLE).performClick()
        timeSelectorInputFields.forEach {
            composeRule.onNodeWithContentDescription(it).assertIsDisplayed()
            composeRule.onNodeWithContentDescription(it).performTextReplacement(expectedValue)
            composeRule.onNodeWithContentDescription(it).assertTextEquals(expectedValue)
        }
    }

    @Test
    fun checkSTimeSelectorFieldsAcceptsOnlyDigits() {
        val inputValues = listOf(",", ".", "ab")
        val digitInput = "14"
        composeRule.onNodeWithContentDescription(TestTags.TOGGLE).performClick()
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

    companion object {
        val timeSelectorUiComponents = TestTags.let {
            listOf(
                it.TIME_SELECTOR,
                it.HOUR_SELECTOR,
                it.MINUTE_SELECTOR,
                it.SECOND_SELECTOR,
                it.SHOW_BERLIN_TIME_BUTTON
            )
        }

        val timeSelectorInputFields = listOf(
            TestTags.HOUR_SELECTOR,
            TestTags.MINUTE_SELECTOR,
            TestTags.SECOND_SELECTOR,
        )
    }
}