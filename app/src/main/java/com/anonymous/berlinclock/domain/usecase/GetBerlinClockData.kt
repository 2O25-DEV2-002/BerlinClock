package com.anonymous.berlinclock.domain.usecase

import com.anonymous.berlinclock.util.HOUR_LAMP_COUNT
import com.anonymous.berlinclock.util.HOUR_MAX_VALUE
import com.anonymous.berlinclock.util.HourLamps
import com.anonymous.berlinclock.util.LampColour
import com.anonymous.berlinclock.util.MESSAGE_INPUT_BETWEEN_0_AND_23
import com.anonymous.berlinclock.util.MESSAGE_INPUT_BETWEEN_0_AND_59
import com.anonymous.berlinclock.util.SecondLamp
import com.anonymous.berlinclock.util.TIME_MAX_VALUE
import com.anonymous.berlinclock.util.TIME_MIN_VALUE
import com.anonymous.berlinclock.util.TOP_HOUR_LAMP_VALUE
import com.anonymous.berlinclock.util.isEven

class GetBerlinClockData {

    fun getSeconds(seconds: Int): SecondLamp {
        require(seconds in TIME_MIN_VALUE..TIME_MAX_VALUE) { MESSAGE_INPUT_BETWEEN_0_AND_59 }
        return if (seconds.isEven()) LampColour.YELLOW else LampColour.OFF
    }

    fun getTopHours(hour: Int): HourLamps {
        require(hour in TIME_MIN_VALUE..HOUR_MAX_VALUE) {
            MESSAGE_INPUT_BETWEEN_0_AND_23
        }
        val litLampsCount = hour / TOP_HOUR_LAMP_VALUE
        return MutableList(HOUR_LAMP_COUNT) { index ->
            if (index < litLampsCount) LampColour.RED else LampColour.OFF
        }
    }

    fun getBottomHour(hour: Int): List<LampColour> {
        require(hour in TIME_MIN_VALUE..HOUR_MAX_VALUE) {
            MESSAGE_INPUT_BETWEEN_0_AND_23
        }
        val lamps = MutableList(HOUR_LAMP_COUNT) { LampColour.OFF }
        if (hour == 1) {
            lamps[0] = LampColour.RED
        } else if (hour == 2) {
            lamps[0] = LampColour.RED
            lamps[1] = LampColour.RED
        } else if (hour == 3) {
            lamps[0] = LampColour.RED
            lamps[1] = LampColour.RED
            lamps[2] = LampColour.RED
        }
        return lamps
    }
}