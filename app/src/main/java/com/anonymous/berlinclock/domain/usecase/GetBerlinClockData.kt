package com.anonymous.berlinclock.domain.usecase

import com.anonymous.berlinclock.util.MESSAGE_INPUT_BETWEEN_0_AND_59

class GetBerlinClockData {

    fun getSeconds(seconds: Int): String {
        require(seconds in 0..59) { MESSAGE_INPUT_BETWEEN_0_AND_59 }
        return if (seconds % 2 == 0) "ON" else "OFF"
    }
}