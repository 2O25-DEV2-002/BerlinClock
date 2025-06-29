package com.anonymous.berlinclock.domain.usecase

import com.anonymous.berlinclock.util.LampColour
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

    fun getTopHours(hour: Int) {
        require(hour in TIME_MIN_VALUE..23) {
            "Hours should be between 0 and 23"
        }
    }
}