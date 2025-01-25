package com.example.hegemonycompose.data

import com.example.hegemonycompose.ui.state.PlayerClass
import com.example.hegemonycompose.ui.state.PlayerData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(private val playerDao: PlayerDao) {

    val players: Flow<List<PlayerData>> =
        playerDao.findAll().map { entities ->
            entities.map {
                it.toModel()
            }
        }

    suspend fun create(playerData: PlayerData) {
        playerDao.create(playerData.toEntity())
    }

    suspend fun update(playerData: PlayerData) {
        val dbPlayerData = playerDao.findByClass(playerData.playerClass).first()
        dbPlayerData.revenue = playerData.revenue
        dbPlayerData.capital = playerData.capital
        playerDao.update(dbPlayerData)
    }

    suspend fun incrementRevenue(playerClass: PlayerClass, incrementValue: Int) {
        playerDao.incrementRevenue(playerClass, incrementValue)
    }

    suspend fun incrementCapital(playerClass: PlayerClass, incrementValue: Int) {
        playerDao.incrementCapital(playerClass, incrementValue)
    }

    suspend fun decrementRevenue(playerClass: PlayerClass, decrementValue: Int) {
        playerDao.decrementRevenue(playerClass, decrementValue)
    }

    suspend fun decrementCapital(playerClass: PlayerClass, decrementValue: Int) {
        playerDao.decrementCapital(playerClass, decrementValue)
    }
}