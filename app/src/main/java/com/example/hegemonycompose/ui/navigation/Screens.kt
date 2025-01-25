package com.example.hegemonycompose.ui.navigation

import com.example.hegemonycompose.ui.state.PlayerClass
import kotlinx.serialization.Serializable

@Serializable
object MainScreenNav

@Serializable
data class TransferScreenNav(val playerClass: PlayerClass)