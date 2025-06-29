package com.anonymous.berlinclock.presentation.berlinclock

sealed class ClockEvent {
    data object StartAutomaticClock : ClockEvent()
    data class UpdateClock(val time: String) : ClockEvent()
}