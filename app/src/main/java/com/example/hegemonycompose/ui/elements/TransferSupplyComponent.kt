package com.example.hegemonycompose.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.hegemonycompose.ui.state.GameData
import com.example.hegemonycompose.ui.state.PlayerClass
import com.example.hegemonycompose.ui.state.PlayerClass.WORKING
import com.inidamleader.ovtracker.util.compose.AutoSizeText
import java.lang.Integer.parseInt

@Composable
fun TransferSupplyComponent(gameData: GameData, addMoney: (PlayerClass, Int) -> Unit) {
    var money by remember { mutableStateOf("") }
    var destinationPlayers by remember { mutableStateOf<List<PlayerClass>>(emptyList()) }
    Column(Modifier.padding(10.dp)) {
        Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .weight(1f)
                    .padding(end = 10.dp, start = 10.dp)
            ) {
                AutoSizeText(
                    "Selecciona cantidad y destinatario: ",
                    maxLines = 1
                )
            }
            Box(Modifier.weight(1f)) {
                OutlinedTextField(
                    value = money,
                    maxLines = 1,
                    onValueChange = { money = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
        Row(Modifier.weight(1f)) {
            for (playerToShow in gameData.players) {
                if (playerToShow.playerClass != WORKING) {
                    PlayerDataComponent(
                        playerToShow,
                        Modifier
                            .weight(1f)
                            .padding(5.dp),
                        isSelectable = true,
                        onClick = { newSelectedPlayer ->
                            destinationPlayers =
                                updateDestinationPlayers(destinationPlayers, newSelectedPlayer)
                        }
                    )
                }
            }
        }
        Row(Modifier.weight(1f)) {
            PlayerDataComponent(
                gameData.players.find { it.playerClass == WORKING }!!,
                Modifier
                    .weight(1f)
                    .padding(5.dp),
                isSelectable = true,
                onClick = { newSelectedPlayer ->
                    destinationPlayers =
                        updateDestinationPlayers(destinationPlayers, newSelectedPlayer)
                }
            )
            SupplyComponent(
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
            )
            Column(Modifier.weight(1f)) {
                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {

                }
                Button(modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                    onClick = {
                        onClickAddMoney(
                            money,
                            destinationPlayers,
                            addMoney
                        )
                    }
                ) {
                    AutoSizeText("Enviar")
                }
            }
        }
    }
}

private fun updateDestinationPlayers(
    destinationPlayers: List<PlayerClass>,
    newSelectedPlayer: PlayerClass
): List<PlayerClass> {
    return if (destinationPlayers.contains(newSelectedPlayer)) {
        destinationPlayers.filter { it != newSelectedPlayer }
    } else {
        destinationPlayers + newSelectedPlayer
    }
}

private fun onClickAddMoney(
    moneyString: String,
    destinationPlayersClass: List<PlayerClass>,
    addMoney: (PlayerClass, Int) -> Unit
) {
    if (moneyString != "") {
        val money = parseInt(moneyString)
        for (playerClass: PlayerClass in destinationPlayersClass) {
            addMoney(playerClass, money)
        }
    }
}