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
import com.anonymous.berlinclock.util.TOP_MINUTE_LAMP_COUNT
import com.anonymous.berlinclock.util.TopHourLamps
import com.anonymous.berlinclock.util.TopMinuteLamps
import com.anonymous.berlinclock.util.getQuotient
import com.anonymous.berlinclock.util.getReminder
import com.anonymous.berlinclock.util.isEven

class GetBerlinClockData {

    fun getSeconds(seconds: Int): SecondLamp {
        checkValidInputBounds(seconds)
        return if (seconds.isEven()) LampColour.YELLOW else LampColour.OFF
    }

    fun getTopHours(hour: Int): TopHourLamps {
        checkValidInputBounds(hour, HOUR_MAX_VALUE, MESSAGE_INPUT_BETWEEN_0_AND_23)
        val litLampsCount = hour.getQuotient(TOP_HOUR_LAMP_VALUE)
        return MutableList(HOUR_LAMP_COUNT) { index ->
            if (index < litLampsCount) LampColour.RED else LampColour.OFF
        }
    }

    fun getBottomHour(hour: Int): BottomHourLamps {
        checkValidInputBounds(hour, HOUR_MAX_VALUE, MESSAGE_INPUT_BETWEEN_0_AND_23)
        val litLampsCount = hour.getReminder(TOP_HOUR_LAMP_VALUE)
        return MutableList(HOUR_LAMP_COUNT) { index ->
            if (index < litLampsCount) LampColour.RED else LampColour.OFF
        }
    }

    fun getTopMinute(minutes: Int): TopMinuteLamps {
        checkValidInputBounds(minutes)
        val lamps = MutableList(TOP_MINUTE_LAMP_COUNT) { LampColour.OFF }
        if (minutes in 5..9) {
            lamps[0] = LampColour.YELLOW
        } else if (minutes in 10..14) {
            lamps[0] = LampColour.YELLOW
            lamps[1] = LampColour.YELLOW
        } else if (minutes in 15..19) {
            lamps[0] = LampColour.YELLOW
            lamps[1] = LampColour.YELLOW
            lamps[2] = LampColour.RED
        }
        return lamps
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