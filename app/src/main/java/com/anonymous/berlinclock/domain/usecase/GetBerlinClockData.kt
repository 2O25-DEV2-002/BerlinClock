package com.anonymous.berlinclock.domain.usecase

class GetBerlinClockData {

    fun getSeconds(seconds: Int) {
        require(seconds in 0..59) { "Seconds should be 0 or greater" }
    }
}