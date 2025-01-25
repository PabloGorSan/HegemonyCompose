package com.example.hegemonycompose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.hegemonycompose.ui.elements.MainScreen
import com.example.hegemonycompose.ui.elements.TransferScreen
import com.example.hegemonycompose.ui.state.GameViewModel

@Composable
fun Navigator() {
    val navController = rememberNavController()
    val viewModel: GameViewModel = viewModel()

    NavHost(navController = navController, startDestination = MainScreenNav) {
        composable<MainScreenNav> {
            MainScreen(
                { playerClass -> navController.navigate(TransferScreenNav(playerClass)) },
                viewModel
            )
        }
        composable<TransferScreenNav> { backStackEntry ->
            val transferScreen: TransferScreenNav = backStackEntry.toRoute()
            TransferScreen(transferScreen.playerClass, viewModel)
        }
    }
}