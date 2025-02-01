package com.example.hegemonycompose.data

import com.example.hegemonycompose.BuildConfig
import com.example.hegemonycompose.ui.state.PlayerClass
import com.example.hegemonycompose.ui.state.PlayerData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    private val localDatabaseRepository: LocalDatabaseRepository,
    private val firebaseRepository: FirebaseRepository
) {
    val players: Flow<List<PlayerData>> =
        localDatabaseRepository.findAll().map { entities ->
            entities.map {
                it.toModel()
            }
        }

    init {
        if (BuildConfig.DB_TYPE == "FIREBASE") {
            firebaseRepository.observeFirebaseRealtimeChanges()
        }
    }

    suspend fun createPlayer(playerData: PlayerData) {
        localDatabaseRepository.create(playerData)
    }

    suspend fun updatePlayer(playerData: PlayerData) {
        localDatabaseRepository.update(playerData)
    }

    suspend fun incrementRevenue(playerClass: PlayerClass, incrementValue: Int) {
        if (BuildConfig.DB_TYPE == "FIREBASE") {
            firebaseRepository.incrementRevenue(playerClass, incrementValue)
        } else {
            localDatabaseRepository.incrementRevenue(playerClass, incrementValue)
        }
    }

    suspend fun incrementCapital(playerClass: PlayerClass, incrementValue: Int) {
        if (BuildConfig.DB_TYPE == "FIREBASE") {
            firebaseRepository.incrementCapital(playerClass, incrementValue)
        } else {
            localDatabaseRepository.incrementCapital(playerClass, incrementValue)
        }
    }

    suspend fun decrementRevenue(playerClass: PlayerClass, decrementValue: Int) {
        if (BuildConfig.DB_TYPE == "FIREBASE") {
            firebaseRepository.decrementRevenue(playerClass, decrementValue)
        } else {
            localDatabaseRepository.decrementRevenue(playerClass, decrementValue)
        }
    }

    suspend fun decrementCapital(playerClass: PlayerClass, decrementValue: Int) {
        if (BuildConfig.DB_TYPE == "FIREBASE") {
            firebaseRepository.decrementCapital(playerClass, decrementValue)
        } else {
            localDatabaseRepository.decrementCapital(playerClass, decrementValue)
        }
    }
}