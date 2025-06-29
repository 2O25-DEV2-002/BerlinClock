package com.anonymous.berlinclock.domain.usecase

import com.anonymous.berlinclock.util.BottomHourLamps
import com.anonymous.berlinclock.util.HOUR_LAMP_COUNT
import com.anonymous.berlinclock.util.HOUR_MAX_VALUE
import com.anonymous.berlinclock.util.LampColour
import com.anonymous.berlinclock.util.MESSAGE_INPUT_BETWEEN_0_AND_23
import com.anonymous.berlinclock.util.MESSAGE_INPUT_BETWEEN_0_AND_59
import com.anonymous.berlinclock.util.SecondLamp
import com.anonymous.berlinclock.util.TIME_MAX_VALUE
import com.anonymous.berlinclock.util.TIME_MIN_VALUE
import com.anonymous.berlinclock.util.TOP_HOUR_LAMP_VALUE
import com.anonymous.berlinclock.util.TopHourLamps
import com.anonymous.berlinclock.util.isEven

class GetBerlinClockData {

    fun getSeconds(seconds: Int): SecondLamp {
        checkValidInputBounds(seconds)
        return if (seconds.isEven()) LampColour.YELLOW else LampColour.OFF
    }

    fun getTopHours(hour: Int): TopHourLamps {
        checkValidInputBounds(hour, HOUR_MAX_VALUE, MESSAGE_INPUT_BETWEEN_0_AND_23)
        val litLampsCount = hour / TOP_HOUR_LAMP_VALUE
        return MutableList(HOUR_LAMP_COUNT) { index ->
            if (index < litLampsCount) LampColour.RED else LampColour.OFF
        }
    }

    fun getBottomHour(hour: Int): BottomHourLamps {
        checkValidInputBounds(hour, HOUR_MAX_VALUE, MESSAGE_INPUT_BETWEEN_0_AND_23)
        val litLampsCount = hour % TOP_HOUR_LAMP_VALUE
        return MutableList(HOUR_LAMP_COUNT) { index ->
            if (index < litLampsCount) LampColour.RED else LampColour.OFF
        }
    }

    fun getTopMinute(minutes: Int) {
        checkValidInputBounds(minutes)
    }

    fun checkValidInputBounds(
        inputTime: Int,
        maxValue: Int = TIME_MAX_VALUE,
        upperBoundMessage: String = MESSAGE_INPUT_BETWEEN_0_AND_59
    ) {
        require(inputTime in TIME_MIN_VALUE..maxValue) {
            upperBoundMessage
        }
    }
}