package com.example.hegemonycompose.domain

import com.example.hegemonycompose.data.GameRepository
import com.example.hegemonycompose.ui.state.PlayerData
import javax.inject.Inject

class UpdatePlayerUseCase @Inject constructor(private val gameRepository: GameRepository) {

    suspend operator fun invoke(playerData: PlayerData) {
        gameRepository.updatePlayer(playerData)
    }
}