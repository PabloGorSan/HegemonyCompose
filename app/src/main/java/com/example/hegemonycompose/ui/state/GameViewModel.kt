package com.example.hegemonycompose.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hegemonycompose.domain.DecrementPlayerCapitalUseCase
import com.example.hegemonycompose.domain.DecrementPlayerRevenueUseCase
import com.example.hegemonycompose.domain.GetPlayersUseCase
import com.example.hegemonycompose.domain.IncrementPlayerCapitalUseCase
import com.example.hegemonycompose.domain.IncrementPlayerRevenueUseCase
import com.example.hegemonycompose.domain.SavePlayerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val getPlayersUseCase: GetPlayersUseCase,
    private val savePlayerUseCase: SavePlayerUseCase,
    private val incrementPlayerRevenueUseCase: IncrementPlayerRevenueUseCase,
    private val decrementPlayerRevenueUseCase: DecrementPlayerRevenueUseCase,
    private val incrementPlayerCapitalUseCase: IncrementPlayerCapitalUseCase,
    private val decrementPlayerCapitalUseCase: DecrementPlayerCapitalUseCase,
) :
    ViewModel() {

    private val _uiState = MutableStateFlow<GameUiState>(GameUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val initialData = GameData(
        listOf(
            PlayerData(PlayerClass.WORKING, 30, 0),
            PlayerData(PlayerClass.MIDDLE, 40, 0),
            PlayerData(PlayerClass.STATE, 80, 0),
            PlayerData(PlayerClass.CAPITALIST, 120, 30)
        )
    )

    init {
        initGameData()
    }

    private fun initGameData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getPlayersUseCase().collect {
                    if (it.isEmpty()) {
                        populateDatabase(initialData)
                    } else {
                        _uiState.value = GameUiState.Success(GameData(it))
                    }
                }
            } catch (e: Exception) {
                _uiState.value = GameUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun transferMoney(
        originPlayer: PlayerClass,
        destinationPlayer: PlayerClass,
        quantity: Int,
        isCapitalUsed: Boolean = false
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            addRevenue(destinationPlayer, quantity)
            if (!isCapitalUsed) {
                removeRevenue(originPlayer, quantity)
            } else {
                removeCapital(originPlayer, quantity)
            }
        }
    }

    fun addMoney(playerClass: PlayerClass, quantity: Int, moneyToCapital: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!moneyToCapital) {
                addRevenue(playerClass, quantity)
            } else {
                addCapital(playerClass, quantity)
            }
        }
    }

    private suspend fun addRevenue(playerClass: PlayerClass, quantity: Int) {
        try {
            incrementPlayerRevenueUseCase(playerClass, quantity)
        } catch (e: Exception) {
            _uiState.value = GameUiState.Error(e.message ?: "Unknown error")
        }
    }

    private suspend fun addCapital(playerClass: PlayerClass, quantity: Int) {
        try {
            incrementPlayerCapitalUseCase(playerClass, quantity)
        } catch (e: Exception) {
            _uiState.value = GameUiState.Error(e.message ?: "Unknown error")
        }
    }

    fun removeMoney(playerClass: PlayerClass, quantity: Int, moneyToCapital: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!moneyToCapital) {
                removeRevenue(playerClass, quantity)
            } else {
                removeCapital(playerClass, quantity)
            }
        }
    }

    private suspend fun removeRevenue(playerClass: PlayerClass, quantity: Int) {
        try {
            decrementPlayerRevenueUseCase(playerClass, quantity)
        } catch (e: Exception) {
            _uiState.value = GameUiState.Error(e.message ?: "Unknown error")
        }
    }

    private suspend fun removeCapital(playerClass: PlayerClass, quantity: Int) {
        try {
            decrementPlayerCapitalUseCase(playerClass, quantity)
        } catch (e: Exception) {
            _uiState.value = GameUiState.Error(e.message ?: "Unknown error")
        }
    }

    private suspend fun populateDatabase(data: GameData) {
        for (player in data.players) {
            savePlayerUseCase(player)
        }
    }
}