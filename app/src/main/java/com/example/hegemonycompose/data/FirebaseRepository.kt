package com.example.hegemonycompose.data

import android.util.Log
import com.example.hegemonycompose.ui.state.PlayerClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepository @Inject constructor(
    private val firebaseDb: FirebaseDatabase,
    private val firebaseDataListener: FirebaseDataListener
) {

    fun incrementRevenue(playerClass: PlayerClass, incrementValue: Int) {
        updateFirebaseValue(playerClass, "revenue", incrementValue, true)
    }

    fun incrementCapital(playerClass: PlayerClass, incrementValue: Int) {
        updateFirebaseValue(playerClass, "capital", incrementValue, true)
    }

    fun decrementRevenue(playerClass: PlayerClass, decrementValue: Int) {
        updateFirebaseValue(playerClass, "revenue", decrementValue, false)
    }

    fun decrementCapital(playerClass: PlayerClass, decrementValue: Int) {
        updateFirebaseValue(playerClass, "capital", decrementValue, false)
    }

    private fun updateFirebaseValue(
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

    fun observeFirebaseRealtimeChanges() {
        firebaseDb.getReference("game_data").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val players = mapSnapshotToEntities(snapshot)
                    firebaseDataListener.onDataChanged(players)
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
        return entities
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