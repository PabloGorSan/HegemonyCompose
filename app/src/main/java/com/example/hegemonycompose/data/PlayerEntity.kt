package com.example.hegemonycompose.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hegemonycompose.ui.state.PlayerClass
import com.example.hegemonycompose.ui.state.PlayerData

@Entity
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var playerClass: PlayerClass,
    var revenue: Int,
    var capital: Int
)

fun PlayerEntity.toModel(): PlayerData {
    return PlayerData(
        playerClass = this.playerClass,
        revenue = this.revenue,
        capital = this.capital
    )
}

fun PlayerData.toEntity(): PlayerEntity {
    return PlayerEntity(
        playerClass = this.playerClass,
        revenue = this.revenue,
        capital = this.capital
    )
}