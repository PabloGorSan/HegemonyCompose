package com.example.hegemonycompose.domain

import com.example.hegemonycompose.data.GameRepository
import com.example.hegemonycompose.ui.state.PlayerData
import javax.inject.Inject

class DecrementPlayerCapitalUseCase @Inject constructor(private val gameRepository: GameRepository) {
    
    suspend operator fun invoke(playerData: PlayerData, incrementValue: Int) {
        require(playerData.capital >= incrementValue) {
            "Not enough capital to decrement: {}<{}".format(
                playerData.capital,
                incrementValue
            )
        }
        gameRepository.decrementCapital(playerData.playerClass, incrementValue)
    }
}