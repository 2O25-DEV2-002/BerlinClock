package com.anonymous.berlinclock.presentation.berlinclock

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.anonymous.berlinclock.MainActivity
import com.anonymous.berlinclock.presentation.berlinclock.navgraph.BerlinClockNavGraph
import com.anonymous.berlinclock.ui.theme.BerlinClockTheme
import com.anonymous.berlinclock.util.TestTags
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BerlinClockScreenTest {

    @get:Rule()
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
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
}