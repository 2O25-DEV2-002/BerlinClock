package com.anonymous.berlinclock.presentation.berlinclock.navgraph

sealed class Screen(val route: String) {
    data object BerlinClockScreen : Screen("clock_screen")
}