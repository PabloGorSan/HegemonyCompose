package com.example.hegemonycompose.data

interface FirebaseDataListener {
    fun onDataChanged(players: List<PlayerEntity>)
}