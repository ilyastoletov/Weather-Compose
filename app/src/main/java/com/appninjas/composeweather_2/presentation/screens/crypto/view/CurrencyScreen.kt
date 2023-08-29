package com.appninjas.composeweather_2.presentation.screens.crypto.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appninjas.domain.model.Crypto

@Composable
fun CurrencyScreen() {

    val testCryptoList: List<Crypto> = listOf(
        Crypto(name = "Bitcoin (BTC)", course = 24132),
        Crypto(name = "Ethereum (ETH)", course = 1712),
        Crypto(name = "Ethereum Classic (ETC)", course = 112312)
    )

    LazyColumn(modifier = Modifier.fillMaxSize().padding(start = 10.dp, end = 10.dp, top = 15.dp)) {
        item { Text(text = "Криптовалюта", fontSize = 25.sp, fontWeight = FontWeight.SemiBold) }
        item { Spacer(modifier = Modifier.height(15.dp)) }
        items(testCryptoList) { item -> CryptoItem(model = item) }
    }
}

@Composable
fun CryptoItem(model: Crypto) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = model.name,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = model.course.toString() + " $",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}