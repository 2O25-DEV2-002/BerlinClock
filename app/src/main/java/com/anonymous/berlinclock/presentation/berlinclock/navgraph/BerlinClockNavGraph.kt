package com.anonymous.berlinclock.presentation.berlinclock.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anonymous.berlinclock.presentation.berlinclock.BerlinClockScreen

@Composable
fun BerlinClockNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.BerlinClockScreen.route
    ) {
        composable(Screen.BerlinClockScreen.route) {
            BerlinClockScreen()
        }
    }
}