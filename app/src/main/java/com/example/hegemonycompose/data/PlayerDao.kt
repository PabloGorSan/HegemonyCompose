package com.example.hegemonycompose.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.hegemonycompose.ui.state.PlayerClass
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {

    @Query("SELECT * FROM PlayerEntity")
    fun findAll(): Flow<List<PlayerEntity>>

    @Query("SELECT * FROM PlayerEntity where playerClass = :playerClass")
    fun findByClass(playerClass: PlayerClass): Flow<PlayerEntity>

    @Insert
    suspend fun create(player: PlayerEntity)

    @Update
    suspend fun update(player: PlayerEntity)

    @Query("UPDATE PlayerEntity SET revenue = revenue + :incrementValue WHERE playerClass = :playerClass")
    suspend fun incrementRevenue(playerClass: PlayerClass, incrementValue: Int)

    @Query("UPDATE PlayerEntity SET capital = capital + :incrementValue WHERE playerClass = :playerClass")
    suspend fun incrementCapital(playerClass: PlayerClass, incrementValue: Int)

    @Query("UPDATE PlayerEntity SET revenue = revenue - :decrementValue WHERE playerClass = :playerClass")
    suspend fun decrementRevenue(playerClass: PlayerClass, decrementValue: Int)

    @Query("UPDATE PlayerEntity SET capital = capital - :decrementValue WHERE playerClass = :playerClass")
    suspend fun decrementCapital(playerClass: PlayerClass, decrementValue: Int)
}