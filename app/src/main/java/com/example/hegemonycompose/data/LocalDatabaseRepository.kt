package com.example.hegemonycompose.data

import com.example.hegemonycompose.ui.state.PlayerClass
import com.example.hegemonycompose.ui.state.PlayerData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDatabaseRepository @Inject constructor(private val playerDao: PlayerDao) :
    FirebaseDataListener {

    fun findAll(): Flow<List<PlayerEntity>> {
        return playerDao.findAll()
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

    override fun onDataChanged(players: List<PlayerEntity>) {
        CoroutineScope(Dispatchers.IO).launch {
            for (player in players) {
                playerDao.update(player)
            }
        }
    }
}