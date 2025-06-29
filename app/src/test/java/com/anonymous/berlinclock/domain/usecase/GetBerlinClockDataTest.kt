package com.anonymous.berlinclock.domain.usecase

import org.junit.Assert.assertThrows
import org.junit.Test

class GetBerlinClockDataTest {

    private val getBerlinClockData = GetBerlinClockData()

    @Test
    fun `getSeconds throws exception when input is negative`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            getBerlinClockData.getSeconds(-1)
        }
        assert(exception.message!!.contains("Seconds should be 0 or greater"))
    }

    @Test
    fun `getSeconds throws exception when input is greater than 59`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            getBerlinClockData.getSeconds(60)
        }
        assert(exception.message!!.contains("Seconds should be 0 or greater"))
    }
}