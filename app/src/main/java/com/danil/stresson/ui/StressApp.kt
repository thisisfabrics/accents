package com.danil.stresson.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.danil.stresson.ui.screens.Game
import com.danil.stresson.ui.screens.Settings

@Composable
fun Stress(
    viewModel: StressViewModel,
    navController: NavHostController = rememberNavController()
) {
    val uiState by viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        NavHost(
            navController = navController,
            startDestination = StressViewModel.Screen.Main.name
        ) {
            composable(route = StressViewModel.Screen.Main.name) {
                Game(viewModel, uiState, navController)
            }
            composable(route = StressViewModel.Screen.Settings.name) {
                Settings(navController)
            }
        }
    }
}


