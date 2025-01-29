package com.example.hegemonycompose.ui.state

data class GameData(
    val players: List<PlayerData> = listOf(),
    val toastMessage: String = ""
)

data class PlayerData(
    val playerClass: PlayerClass,
    val revenue: Int,
    val capital: Int
)

enum class PlayerClass {
    WORKING, MIDDLE, CAPITALIST, STATE, SUPPLY
}