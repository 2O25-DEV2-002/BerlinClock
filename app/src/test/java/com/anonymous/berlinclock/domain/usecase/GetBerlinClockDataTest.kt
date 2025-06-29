package com.anonymous.berlinclock.domain.usecase

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

class GetBerlinClockDataTest {

    private lateinit var getBerlinClockData: GetBerlinClockData

    @Test
    fun `getSeconds throws exception when input is negative`() {
        getBerlinClockData = GetBerlinClockData()
        val exception = assertThrows(IllegalArgumentException::class.java) {
            getBerlinClockData.getSeconds(-1)
        }
        assertThat(exception).hasMessageThat().contains("Seconds should be 0 or greater")
    }

    @Test
    fun `getSeconds throws exception when input is greater than 59`() {
        getBerlinClockData = GetBerlinClockData()
        val exception = assertThrows(IllegalArgumentException::class.java) {
            getBerlinClockData.getSeconds(60)
        }
        assertThat(exception).hasMessageThat().contains("Seconds should be 0 or greater")
    }
}