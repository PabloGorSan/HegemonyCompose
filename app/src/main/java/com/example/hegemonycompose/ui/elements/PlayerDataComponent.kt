package com.example.hegemonycompose.ui.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hegemonycompose.R
import com.example.hegemonycompose.ui.state.PlayerClass
import com.example.hegemonycompose.ui.state.PlayerClass.CAPITALIST
import com.example.hegemonycompose.ui.state.PlayerClass.MIDDLE
import com.example.hegemonycompose.ui.state.PlayerClass.STATE
import com.example.hegemonycompose.ui.state.PlayerClass.WORKING
import com.example.hegemonycompose.ui.state.PlayerData
import com.inidamleader.ovtracker.util.compose.AutoSizeText

@Composable
fun PlayerDataComponent(
    playerData: PlayerData,
    modifier: Modifier = Modifier,
    onClick: (PlayerClass) -> Unit = {},
    isSelectable: Boolean = false
) {
    var selected by remember { mutableStateOf(false) }
    val color = if (selected) Color(0xFF8BC34A) else CardDefaults.cardColors().containerColor

    Card(
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, getPlayerColor(playerData.playerClass)),
        modifier = modifier.clickable {
            if (isSelectable) selected = !selected
            onClick(playerData.playerClass)
        },
        colors = CardDefaults.cardColors(
            containerColor = color
        )
    ) {
        Row(
            Modifier.padding(horizontal = 15.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(getImage(playerData.playerClass)),
                playerData.playerClass.toString().lowercase(),
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2f)
            ) {
                Text(getTitle(playerData.playerClass), fontSize = 23.sp)
                RevenueAndCapitalComponent(playerData)
            }

        }
    }
}

private fun getImage(playerClass: PlayerClass): Int {
    return when (playerClass) {
        WORKING -> R.drawable.working
        STATE -> R.drawable.state
        CAPITALIST -> R.drawable.capitalist
        MIDDLE -> R.drawable.middle
        PlayerClass.SUPPLY -> R.drawable.tom
    }
}

private fun getTitle(playerClass: PlayerClass): String {
    //return playerClass.toString().lowercase().replaceFirstChar { char -> char.uppercaseChar() }
    return playerClass.toString()
}

@Composable
fun RevenueAndCapitalComponent(playerData: PlayerData) {
    Card(
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(1.dp, getPlayerColor(playerData.playerClass)),
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 10.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(start = 1.dp)
        ) {
            Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.weight(1f)) {
                    AutoSizeText("Rev: ", maxLines = 1)
                }
                Box(Modifier.weight(2f)) {
                    AutoSizeText(playerData.revenue.toString() + "€", maxLines = 1)
                }
            }
            if (playerData.playerClass == CAPITALIST) {
                Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.weight(1f)) {
                        AutoSizeText("Cap: ", maxLines = 1)
                    }
                    Box(Modifier.weight(2f)) {
                        AutoSizeText(playerData.capital.toString() + "€", maxLines = 1)
                    }
                }
            }
        }
    }
}

private fun getPlayerColor(playerClass: PlayerClass): Color {
    return when (playerClass) {
        WORKING -> Color.Red
        MIDDLE -> Color(0xFFFFB300)
        CAPITALIST -> Color.Blue
        STATE -> Color.Gray
        PlayerClass.SUPPLY -> Color.Green
    }
}

@Preview(device = "spec:width=211dp,height=391dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape")
@Composable
fun PlayerDataComponentPreview() {
    val playerData = PlayerData(CAPITALIST, 30, 50)
    PlayerDataComponent(playerData)
}