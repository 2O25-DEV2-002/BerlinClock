package com.anonymous.berlinclock.domain.usecase

import com.anonymous.berlinclock.util.HOUR_MAX_VALUE
import com.anonymous.berlinclock.util.LampColour
import com.anonymous.berlinclock.util.MESSAGE_INPUT_BETWEEN_0_AND_23
import com.anonymous.berlinclock.util.MESSAGE_INPUT_BETWEEN_0_AND_59
import com.anonymous.berlinclock.util.SecondLamp
import com.anonymous.berlinclock.util.TIME_MAX_VALUE
import com.anonymous.berlinclock.util.TIME_MIN_VALUE
import com.anonymous.berlinclock.util.isEven

class GetBerlinClockData {

    fun getSeconds(seconds: Int): SecondLamp {
        require(seconds in TIME_MIN_VALUE..TIME_MAX_VALUE) { MESSAGE_INPUT_BETWEEN_0_AND_59 }
        return if (seconds.isEven()) LampColour.YELLOW else LampColour.OFF
    }

    fun getTopHours(hour: Int): List<LampColour> {
        require(hour in TIME_MIN_VALUE..HOUR_MAX_VALUE) {
            MESSAGE_INPUT_BETWEEN_0_AND_23
        }
        val lamps = MutableList(4) { LampColour.OFF }

        if (hour in 5..9) {
            lamps[0] = LampColour.RED
        } else if (hour in 10..14) {
            lamps[0] = LampColour.RED
            lamps[1] = LampColour.RED
        } else if (hour in 15..19) {
            lamps[0] = LampColour.RED
            lamps[1] = LampColour.RED
            lamps[2] = LampColour.RED
        } else if (hour in 20..23) {
            lamps[0] = LampColour.RED
            lamps[1] = LampColour.RED
            lamps[2] = LampColour.RED
            lamps[3] = LampColour.RED
        }
        return lamps
    }
}