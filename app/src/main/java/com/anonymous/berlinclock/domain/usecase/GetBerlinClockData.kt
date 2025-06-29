package com.anonymous.berlinclock.domain.usecase

class GetBerlinClockData {

    fun getSeconds(seconds: Int) {
        require(seconds >= 0) { "Seconds should be 0 or greater" }
    }
}