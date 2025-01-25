package com.example.hegemonycompose.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hegemonycompose.ui.state.GameData
import com.example.hegemonycompose.ui.state.GameUiState
import com.example.hegemonycompose.ui.state.GameViewModel
import com.example.hegemonycompose.ui.state.PlayerClass
import com.example.hegemonycompose.ui.state.PlayerClass.SUPPLY
import com.example.hegemonycompose.ui.state.PlayerData


@Composable
fun MainScreen(navigateToTransfer: (PlayerClass) -> Unit, viewModel: GameViewModel) {
    val gameState by viewModel.uiState.collectAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        when (gameState) {
            is GameUiState.Loading -> CircularProgressIndicator()
            is GameUiState.Error -> Text((gameState as GameUiState.Error).msg)
            is GameUiState.Success -> {
                val gameData: GameData = (gameState as GameUiState.Success).gameData
                MainScreenSuccessComponent(gameData, navigateToTransfer)
            }
        }
    }
}

@Composable
fun MainScreenSuccessComponent(gameData: GameData, navigateToTransfer: (PlayerClass) -> Unit) {
    Column(Modifier.padding(20.dp)) {
        Row(
            Modifier
                .weight(1f)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlayerDataComponent(gameData.players[0], Modifier.weight(1f), navigateToTransfer)
            SupplyComponent(
                modifier = Modifier.weight(0.5f).padding(horizontal = 5.dp),
                isMainScreen = true,
                onClick = {
                    navigateToTransfer(SUPPLY)
                })
            PlayerDataComponent(gameData.players[1], Modifier.weight(1f), navigateToTransfer)
        }
        Spacer(Modifier.weight(0.1f))
        Row(
            Modifier
                .weight(1f)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlayerDataComponent(gameData.players[2], Modifier.weight(1f), navigateToTransfer)
            Button(onClick = { onClick() }, Modifier.weight(0.5f)) {
                Text("Reiniciar")
            }
            PlayerDataComponent(gameData.players[3], Modifier.weight(1f), navigateToTransfer)
        }
    }
}

private fun onClick() {
    TODO("Not yet implemented")
}

@Preview(device = "spec:width=500dp,height=900dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape")
@Composable
fun MainScreenPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        MainScreenSuccessComponent(getPreviewData(), {})
    }
}

private fun getPreviewData(): GameData {
    val player1 = PlayerData(PlayerClass.WORKING, 30, 0)
    val player2 = PlayerData(PlayerClass.MIDDLE, 40, 0)
    val player3 = PlayerData(PlayerClass.STATE, 80, 0)
    val player4 = PlayerData(PlayerClass.CAPITALIST, 120, 30)
    return GameData(listOf(player1, player2, player3, player4))
}