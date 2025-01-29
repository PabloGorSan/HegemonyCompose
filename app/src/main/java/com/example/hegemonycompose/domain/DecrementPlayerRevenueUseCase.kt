package com.example.hegemonycompose.domain

import com.example.hegemonycompose.data.GameRepository
import com.example.hegemonycompose.ui.state.PlayerData
import javax.inject.Inject

class DecrementPlayerRevenueUseCase @Inject constructor(private val gameRepository: GameRepository) {

    suspend operator fun invoke(playerData: PlayerData, incrementValue: Int) {
        require(playerData.revenue >= incrementValue) {
            "Not enough revenue to decrement: {}<{}".format(
                playerData.revenue,
                incrementValue
            )
        }
        gameRepository.decrementRevenue(playerData.playerClass, incrementValue)
    }
}