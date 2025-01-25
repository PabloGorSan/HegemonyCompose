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
import com.example.hegemonycompose.R
import com.inidamleader.ovtracker.util.compose.AutoSizeText

@Composable
fun SupplyComponent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    isSelectable: Boolean = false,
    isMainScreen: Boolean = false
) {
    var selected by remember { mutableStateOf(false) }
    val color = if (selected) Color(0xFF8BC34A) else CardDefaults.cardColors().containerColor

    Card(
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color.Green),
        modifier = modifier.clickable {
            if (isSelectable) selected = !selected
            onClick()
        },
        colors = CardDefaults.cardColors(
            containerColor = color
        )
    ) {
        if (isMainScreen) {
            MainScreenLayout()
        } else {
            TransferScreenLayout()
        }
    }

}

@Composable
private fun MainScreenLayout() {
    Column(
        Modifier.padding(horizontal = 15.dp, vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.tom),
            "supply",
            modifier = Modifier
                .fillMaxSize()
                .weight(2f)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            SupplyTextComponent(true)
        }

    }
}

@Composable
private fun TransferScreenLayout() {
    Row(
        Modifier.padding(horizontal = 15.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.tom),
            "supply",
            modifier = Modifier
                .fillMaxSize()
                .weight(1.6f)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            SupplyTextComponent()
        }

    }
}


@Composable
fun SupplyTextComponent(isMainScreen: Boolean = false) {
    Column {
        Box(Modifier.weight(1f)) { }
        Card(
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(1.dp, Color.Green),
            modifier = Modifier.weight(if (isMainScreen) 2f else 1f)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(start = 1.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AutoSizeText("SUPPLY", Modifier.padding(5.dp))
            }
        }
        Box(Modifier.weight(1f)) { }
    }

}

@Preview(device = "spec:width=391dp,height=211dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape")
@Composable
fun SupplyDataComponentPreview() {
    SupplyComponent()
}


@Preview(device = "spec:width=211dp,height=391dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape")
@Composable
fun SupplyDataComponentMainScreenPreview() {
    SupplyComponent(isMainScreen = true)
}