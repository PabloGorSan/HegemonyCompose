package com.example.hegemonycompose.domain

import com.example.hegemonycompose.ui.state.PlayerClass
import com.example.hegemonycompose.ui.state.PlayerData
import javax.inject.Inject

class TransferMoneyUseCase @Inject constructor(
    private val decrementPlayerCapitalUseCase: DecrementPlayerCapitalUseCase,
    private val decrementPlayerRevenueUseCase: DecrementPlayerRevenueUseCase,
    private val incrementPlayerRevenueUseCase: IncrementPlayerRevenueUseCase
) {

    suspend operator fun invoke(
        playerData: PlayerData,
        amount: Int,
        receivers: List<PlayerClass>,
        isCapitalUsed: Boolean,
        isSupplyUsed: Boolean
    ) {
        require(checkIsMoneyAvailable(playerData, amount, receivers, isCapitalUsed, isSupplyUsed))
        for (receiverClass: PlayerClass in receivers) {
            transferMoney(playerData, receiverClass, amount, isCapitalUsed)
        }
        if (isSupplyUsed) transferMoneyToSupply(playerData, amount, isCapitalUsed)
    }

    private fun checkIsMoneyAvailable(
        playerData: PlayerData,
        amount: Int,
        receivers: List<PlayerClass>,
        isCapitalUsed: Boolean,
        isSupplyUsed: Boolean
    ): Boolean {
        val payOptionMoney = if (isCapitalUsed) playerData.capital else playerData.revenue
        return (receivers.isNotEmpty() && payOptionMoney >= amount * receivers.size) ||
                (isSupplyUsed && payOptionMoney >= amount * (receivers.size + 1))
    }

    private suspend fun transferMoney(
        playerData: PlayerData,
        receiverPlayer: PlayerClass,
        amount: Int,
        isCapitalUsed: Boolean
    ) {
        if (isCapitalUsed) {
            decrementPlayerCapitalUseCase(playerData, amount)
        } else {
            decrementPlayerRevenueUseCase(playerData, amount)
        }
        incrementPlayerRevenueUseCase(receiverPlayer, amount)
    }

    private suspend fun transferMoneyToSupply(
        playerData: PlayerData,
        amount: Int,
        isCapitalUsed: Boolean
    ) {
        if (isCapitalUsed) {
            decrementPlayerCapitalUseCase(playerData, amount)
        } else {
            decrementPlayerRevenueUseCase(playerData, amount)
        }
    }
}