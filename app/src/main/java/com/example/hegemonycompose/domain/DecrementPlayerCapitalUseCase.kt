package com.example.hegemonycompose.domain

import com.example.hegemonycompose.data.GameRepository
import com.example.hegemonycompose.ui.state.PlayerClass
import javax.inject.Inject

class DecrementPlayerCapitalUseCase @Inject constructor(private val gameRepository: GameRepository) {

    suspend operator fun invoke(playerClass: PlayerClass, incrementValue: Int) {
        gameRepository.decrementCapital(playerClass, incrementValue)
    }
}