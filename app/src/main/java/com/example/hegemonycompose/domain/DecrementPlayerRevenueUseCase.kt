package com.example.hegemonycompose.domain

import com.example.hegemonycompose.data.GameRepository
import com.example.hegemonycompose.ui.state.PlayerClass
import javax.inject.Inject

class DecrementPlayerRevenueUseCase @Inject constructor(private val gameRepository: GameRepository) {

    suspend operator fun invoke(playerClass: PlayerClass, incrementValue: Int) {
        gameRepository.decrementRevenue(playerClass, incrementValue)
    }
}