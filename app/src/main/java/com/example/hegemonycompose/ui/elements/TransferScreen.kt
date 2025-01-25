package com.example.hegemonycompose.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hegemonycompose.ui.state.GameData
import com.example.hegemonycompose.ui.state.GameUiState
import com.example.hegemonycompose.ui.state.GameViewModel
import com.example.hegemonycompose.ui.state.PlayerClass
import com.example.hegemonycompose.ui.state.PlayerClass.CAPITALIST
import com.example.hegemonycompose.ui.state.PlayerClass.MIDDLE
import com.example.hegemonycompose.ui.state.PlayerClass.STATE
import com.example.hegemonycompose.ui.state.PlayerClass.SUPPLY
import com.example.hegemonycompose.ui.state.PlayerClass.WORKING
import com.example.hegemonycompose.ui.state.PlayerData
import com.inidamleader.ovtracker.util.compose.AutoSizeText
import java.lang.Integer.parseInt


@Composable
fun TransferScreen(playerClass: PlayerClass, viewModel: GameViewModel) {
    val gameState by viewModel.uiState.collectAsState()
    Surface(modifier = Modifier.fillMaxSize()) {
        when (gameState) {
            is GameUiState.Loading -> CircularProgressIndicator()
            is GameUiState.Error -> Text((gameState as GameUiState.Error).msg)
            is GameUiState.Success -> {
                val gameData: GameData = (gameState as GameUiState.Success).gameData
                if (playerClass == SUPPLY) {
                    TransferSupplyComponent(gameData, { d, m -> viewModel.addMoney(d, m) })
                } else {
                    TransferComponent(
                        playerClass,
                        gameData,
                        { o, d, m, i -> viewModel.transferMoney(o, d, m, i) },
                        { o, m, i -> viewModel.addMoney(o, m, i) },
                        { o, m, i -> viewModel.removeMoney(o, m, i) }
                    )
                }
            }
        }
    }
}


@Composable
fun TransferComponent(
    playerClass: PlayerClass,
    gameData: GameData,
    transferMoney: (PlayerClass, PlayerClass, Int, Boolean) -> Unit,
    addMoney: (PlayerClass, Int, Boolean) -> Unit,
    removeMoney: (PlayerClass, Int, Boolean) -> Unit
) {
    var money by remember { mutableStateOf("") }
    var destinationPlayers by remember { mutableStateOf<List<PlayerClass>>(emptyList()) }
    var isSupplySelected by remember { mutableStateOf(false) }
    var isCapitalUsed by remember { mutableStateOf(false) }
    val originPlayer = gameData.players.find { it.playerClass == playerClass }!!
    Column(Modifier.padding(10.dp)) {
        Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .weight(2f)
                    .padding(end = 10.dp, start = 10.dp)
            ) {
                AutoSizeText(
                    "Selecciona cantidad y destinatario: ",
                    maxLines = 1
                )
            }
            Box(
                Modifier
                    .weight(0.8f)
                    .padding(end = 10.dp)
            ) {
                OutlinedTextField(
                    value = money,
                    maxLines = 1,
                    onValueChange = { money = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Box(Modifier.weight(1f)) {
                if (playerClass == CAPITALIST) {
                    RadioButtonSingleSelection(changeOption = { isCapitalUsed = it })
                }
            }
        }
        Row(Modifier.weight(1f)) {
            for (playerToShow in gameData.players) {
                if (playerToShow.playerClass != playerClass) {
                    PlayerDataComponent(
                        playerToShow,
                        Modifier
                            .weight(1f)
                            .padding(5.dp),
                        isSelectable = true,
                        onClick = { newSelectedPlayer ->
                            if (destinationPlayers.contains(newSelectedPlayer)) {
                                destinationPlayers =
                                    destinationPlayers.filter { it != newSelectedPlayer }
                            } else {
                                destinationPlayers = destinationPlayers + newSelectedPlayer
                            }
                        }
                    )
                }
            }
        }
        Row(Modifier.weight(1f)) {
            SupplyComponent(
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp),
                isSelectable = true,
                onClick = {
                    isSupplySelected = !isSupplySelected
                }
            )
            PlayerDataComponent(
                originPlayer,
                Modifier
                    .weight(1f)
                    .padding(5.dp)
            )
            Column(Modifier.weight(1f)) {
                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    if (originPlayer.playerClass == CAPITALIST) {
                        Row {
                            Button(modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .padding(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                                onClick = {
                                    onClickMoveMoney(
                                        money,
                                        originPlayer,
                                        true,
                                        addMoney,
                                        removeMoney
                                    )
                                }) {
                                AutoSizeText("Ingresos -> Capital", maxLines = 1)
                            }
                            Button(modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .padding(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                                onClick = {
                                    onClickMoveMoney(
                                        money,
                                        originPlayer,
                                        false,
                                        addMoney,
                                        removeMoney
                                    )
                                }) {
                                AutoSizeText("Capital -> Ingresos", maxLines = 1)
                            }
                        }
                    }
                }
                Button(modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(10.dp),
                    onClick = {
                        onClickTransfer(
                            money,
                            originPlayer,
                            destinationPlayers,
                            isSupplySelected,
                            isCapitalUsed,
                            transferMoney,
                            removeMoney
                        )
                    }) {
                    AutoSizeText("Enviar")
                }

            }

        }
    }
}

@Composable
fun RadioButtonSingleSelection(modifier: Modifier = Modifier, changeOption: (Boolean) -> Unit) {
    val radioOptions = listOf("Ingresos", "Capital")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    Column(Modifier.padding(end = 10.dp)) {
        AutoSizeText(text = "Método de pago", maxLines = 1)
        Row(modifier.selectableGroup(), verticalAlignment = Alignment.CenterVertically) {
            radioOptions.forEach { text ->
                Row(
                    Modifier
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = {
                                onOptionSelected(text)
                                changeOption(selectedOption == "Ingresos")
                            },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (text == selectedOption),
                        onClick = null
                    )
                    AutoSizeText(text = text, maxLines = 1)
                }
            }
        }
    }
}

private fun onClickTransfer(
    moneyString: String,
    originPlayer: PlayerData,
    destinationPlayersClass: List<PlayerClass>,
    isSupplySelected: Boolean,
    isCapitalUsed: Boolean,
    transferMoney: (PlayerClass, PlayerClass, Int, Boolean) -> Unit,
    removeMoney: (PlayerClass, Int, Boolean) -> Unit
) {
    if (moneyString != "") {
        val money = parseInt(moneyString)
        val payOptionMoney = if (isCapitalUsed) originPlayer.capital else originPlayer.revenue
        if ((destinationPlayersClass.isNotEmpty() && payOptionMoney >= money * destinationPlayersClass.size) ||
            isSupplySelected && payOptionMoney >= money * (destinationPlayersClass.size + 1)
        ) {
            for (playerClass: PlayerClass in destinationPlayersClass) {
                transferMoney(originPlayer.playerClass, playerClass, money, isCapitalUsed)
            }
            if (isSupplySelected) removeMoney(originPlayer.playerClass, money, isCapitalUsed)
        }
    }
}

private fun onClickMoveMoney(
    moneyString: String,
    originPlayer: PlayerData,
    moneyToCapital: Boolean,
    addMoney: (PlayerClass, Int, Boolean) -> Unit,
    removeMoney: (PlayerClass, Int, Boolean) -> Unit
) {
    if (moneyString != "") {
        val money = parseInt(moneyString)
        val payOptionMoney = if (moneyToCapital) originPlayer.revenue else originPlayer.capital
        if (payOptionMoney >= money) {
            addMoney(originPlayer.playerClass, money, moneyToCapital)
            removeMoney(originPlayer.playerClass, money, !moneyToCapital)
        }
    }
}


@Preview(device = "spec:width=711dp,height=1291dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape")
@Composable
fun TransferScreenPreview() {
    Surface {
        TransferComponent(
            CAPITALIST,
            getPreviewData(),
            { o, d, m, i -> { } },
            { o, m, i -> { } },
            { o, m, i -> { } })
    }
}

@Preview(device = "spec:width=711dp,height=1291dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape")
@Composable
fun TransferSupplyScreenPreview() {
    Surface {
        TransferSupplyComponent(getPreviewData(), { o, m -> { } })
    }
}

private fun getPreviewData(): GameData {
    val player1 = PlayerData(WORKING, 30, 0)
    val player2 = PlayerData(MIDDLE, 40, 0)
    val player3 = PlayerData(STATE, 80, 0)
    val player4 = PlayerData(CAPITALIST, 120, 30)
    return GameData(listOf(player1, player2, player3, player4))
}