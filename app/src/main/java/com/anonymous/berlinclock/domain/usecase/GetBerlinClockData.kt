package com.anonymous.berlinclock.domain.usecase

import com.anonymous.berlinclock.util.MESSAGE_INPUT_BETWEEN_0_AND_59

class GetBerlinClockData {

    fun getSeconds(seconds: Int) {
        require(seconds in 0..59) { MESSAGE_INPUT_BETWEEN_0_AND_59 }
    }
}