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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appninjas.composeweather_2.presentation.screens.crypto.contract.CryptoContract
import com.appninjas.composeweather_2.presentation.screens.crypto.contract.CryptoViewModel
import com.appninjas.composeweather_2.presentation.ui.failure.NetworkFailureScreen
import com.appninjas.composeweather_2.presentation.ui.loading.LoadingScreen
import com.appninjas.domain.model.Crypto

@Composable
fun CurrencyScreen(viewModel: CryptoViewModel = hiltViewModel()) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    CurrencyScreenContent(state = state, onEvent = viewModel::handleEvents)

}

@Composable
private fun CurrencyScreenContent(
    state: CryptoContract.State,
    onEvent: (CryptoContract.Event) -> Unit) {

    onEvent(CryptoContract.Event.LoadCryptoData)

    when(state) {
        is CryptoContract.State.Loading -> LoadingScreen()
        is CryptoContract.State.CryptoDataLoaded -> Content(cryptoList = state.cryptoList)
        is CryptoContract.State.NetworkFailure -> NetworkFailureScreen()
    }

}

@Composable
private fun Content(cryptoList: List<Crypto>) {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(start = 10.dp, end = 10.dp, top = 15.dp)) {
        item { Text(text = "Криптовалюта", fontSize = 25.sp, fontWeight = FontWeight.SemiBold) }
        item { Spacer(modifier = Modifier.height(15.dp)) }
        items(cryptoList) { item -> CryptoItem(model = item) }
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