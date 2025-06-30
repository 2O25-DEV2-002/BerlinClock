package com.anonymous.berlinclock.presentation.berlinclock.navgraph

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anonymous.berlinclock.presentation.berlinclock.BerlinClockScreen
import com.anonymous.berlinclock.presentation.berlinclock.BerlinClockViewModel

@Composable
fun BerlinClockNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.BerlinClockScreen.route
    ) {
        composable(Screen.BerlinClockScreen.route) {
            val viewModel = hiltViewModel<BerlinClockViewModel>()
            val state = viewModel.clockState
            BerlinClockScreen(state)
        }
    }
}