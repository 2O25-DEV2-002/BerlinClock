package com.anonymous.berlinclock.presentation.berlinclock

sealed class ClockEvent {
    data object StartAutomaticClock : ClockEvent()
}