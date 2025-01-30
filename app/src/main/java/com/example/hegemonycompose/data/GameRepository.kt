package com.example.hegemonycompose.data

import android.util.Log
import com.example.hegemonycompose.ui.state.PlayerClass
import com.example.hegemonycompose.ui.state.PlayerData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    private val playerDao: PlayerDao,
    private val firebaseDb: FirebaseDatabase
) {

    val players: Flow<List<PlayerData>> =
        playerDao.findAll().map { entities ->
            entities.map {
                it.toModel()
            }
        }

    init {
        observeFirebaseRealtimeChanges()
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

    /*
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
    */
    suspend fun incrementRevenue(playerClass: PlayerClass, incrementValue: Int) {
        updateFirebaseValue(playerClass, "revenue", incrementValue, true)
    }

    suspend fun incrementCapital(playerClass: PlayerClass, incrementValue: Int) {
        updateFirebaseValue(playerClass, "capital", incrementValue, true)
    }

    suspend fun decrementRevenue(playerClass: PlayerClass, decrementValue: Int) {
        updateFirebaseValue(playerClass, "revenue", decrementValue, false)
    }

    suspend fun decrementCapital(playerClass: PlayerClass, decrementValue: Int) {
        updateFirebaseValue(playerClass, "capital", decrementValue, false)
    }

    private suspend fun updateFirebaseValue(
        playerClass: PlayerClass,
        field: String,
        value: Int,
        isIncrement: Boolean
    ) {
        val playerRef = firebaseDb.getReference("game_data").child(playerClass.name)

        playerRef.child(field).runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val currentValue = currentData.getValue(Int::class.java) ?: 0
                currentData.value = if (isIncrement) currentValue + value else currentValue - value
                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                if (error != null) {
                    Log.e(
                        "FirebaseUpdate",
                        "Error updating $field for $playerClass: ${error.message}"
                    )
                }
            }
        })
    }

    private fun observeFirebaseRealtimeChanges() {
        firebaseDb.getReference("game_data").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val players = mapSnapshotToEntities(snapshot)
                    updateLocalDatabase(players)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DataRepository", "Firebase sync error: ${error.message}")
            }
        })
    }

    private fun mapSnapshotToEntities(snapshot: DataSnapshot): List<PlayerEntity> {
        val entities = mutableListOf<PlayerEntity>()
        snapshot.children.forEach { gameSnapshot ->
            val playerClass = PlayerClass.valueOf(gameSnapshot.key.orEmpty())
            val revenue = gameSnapshot.child("revenue").getValue(Int::class.java) ?: 0
            val capital = gameSnapshot.child("capital").getValue(Int::class.java) ?: 0

            entities.add(
                PlayerEntity(
                    getKey(playerClass),
                    playerClass,
                    revenue,
                    capital
                )
            )
        }
        return entities;
    }

    private fun updateLocalDatabase(players: List<PlayerEntity>) {
        CoroutineScope(Dispatchers.IO).launch {
            for (player in players) {
                playerDao.update(player)
            }
        }
    }

    private fun getKey(playerClass: PlayerClass): Int {
        return when (playerClass) {
            PlayerClass.WORKING -> 1
            PlayerClass.MIDDLE -> 2
            PlayerClass.STATE -> 3
            PlayerClass.CAPITALIST -> 4
            PlayerClass.SUPPLY -> 5
        }
    }


}