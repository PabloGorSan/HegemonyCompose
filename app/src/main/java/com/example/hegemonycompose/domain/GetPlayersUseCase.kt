package com.example.hegemonycompose.domain

import com.example.hegemonycompose.data.GameRepository
import com.example.hegemonycompose.ui.state.PlayerData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPlayersUseCase @Inject constructor(private val gameRepository: GameRepository) {
    operator fun invoke(): Flow<List<PlayerData>> = gameRepository.players
}