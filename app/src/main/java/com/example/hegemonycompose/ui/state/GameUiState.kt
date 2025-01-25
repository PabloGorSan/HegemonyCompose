package com.example.hegemonycompose.ui.state

sealed class GameUiState {
    object Loading : GameUiState()
    data class Success(val gameData: GameData) : GameUiState()
    data class Error(val msg: String) : GameUiState()

}