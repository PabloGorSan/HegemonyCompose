package com.example.hegemonycompose.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hegemonycompose.domain.DecrementPlayerCapitalUseCase
import com.example.hegemonycompose.domain.DecrementPlayerRevenueUseCase
import com.example.hegemonycompose.domain.GetPlayersUseCase
import com.example.hegemonycompose.domain.IncrementPlayerCapitalUseCase
import com.example.hegemonycompose.domain.IncrementPlayerRevenueUseCase
import com.example.hegemonycompose.domain.SavePlayerUseCase
import com.example.hegemonycompose.domain.TransferMoneyUseCase
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
    private val transferMoneyUseCase: TransferMoneyUseCase,
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
        originPlayer: PlayerData,
        receivers: List<PlayerClass>,
        amount: Int,
        isCapitalUsed: Boolean = false,
        isSupplyUsed: Boolean = false
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                transferMoneyUseCase(
                    originPlayer,
                    amount,
                    receivers,
                    isCapitalUsed,
                    isSupplyUsed
                )
            } catch (e: IllegalArgumentException) {
                showNotEnoughMoneyToast(isCapitalUsed)
            }
        }
    }

    fun addMoney(playerClass: PlayerClass, amount: Int, moneyToCapital: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!moneyToCapital) {
                addRevenue(playerClass, amount)
            } else {
                addCapital(playerClass, amount)
            }
        }
    }

    private suspend fun addRevenue(playerClass: PlayerClass, amount: Int) {
        try {
            incrementPlayerRevenueUseCase(playerClass, amount)
        } catch (e: Exception) {
            _uiState.value = GameUiState.Error(e.message ?: "Unknown error")
        }
    }

    private suspend fun addCapital(playerClass: PlayerClass, amount: Int) {
        try {
            incrementPlayerCapitalUseCase(playerClass, amount)
        } catch (e: Exception) {
            _uiState.value = GameUiState.Error(e.message ?: "Unknown error")
        }
    }

    fun removeMoney(playerClass: PlayerClass, amount: Int, moneyToCapital: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!moneyToCapital) {
                removeRevenue(playerClass, amount)
            } else {
                removeCapital(playerClass, amount)
            }
        }
    }

    private suspend fun removeRevenue(playerClass: PlayerClass, amount: Int) {
        try {
            decrementPlayerRevenueUseCase(getCurrentPlayerDataByClass(playerClass), amount)
        } catch (e: IllegalArgumentException) {
            showNotEnoughMoneyToast(false)
        } catch (e: Exception) {
            _uiState.value = GameUiState.Error(e.message ?: "Unknown error")
        }
    }

    private suspend fun removeCapital(playerClass: PlayerClass, amount: Int) {
        try {
            decrementPlayerCapitalUseCase(getCurrentPlayerDataByClass(playerClass), amount)
        } catch (e: IllegalArgumentException) {
            showNotEnoughMoneyToast(true)
        } catch (e: Exception) {
            _uiState.value = GameUiState.Error(e.message ?: "Unknown error")
        }
    }

    private fun showNotEnoughMoneyToast(isCapitalUsed: Boolean = false) {
        val currentState = (_uiState.value as GameUiState.Success)
        val message: String =
            if (isCapitalUsed) "No hay suficiente Capital" else "No hay suficientes Ingresos"
        _uiState.value =
            currentState.copy(gameData = currentState.gameData.copy(toastMessage = message))
    }

    fun clearToast() {
        val currentState = _uiState.value
        if (currentState is GameUiState.Success) {
            _uiState.value =
                currentState.copy(gameData = currentState.gameData.copy(toastMessage = ""))
        }
    }

    private suspend fun populateDatabase(data: GameData) {
        for (player in data.players) {
            savePlayerUseCase(player)
        }
    }

    private fun getCurrentPlayerDataByClass(playerClass: PlayerClass): PlayerData {
        return (uiState.value as GameUiState.Success).gameData.players.find { it.playerClass == playerClass }!!
    }
}