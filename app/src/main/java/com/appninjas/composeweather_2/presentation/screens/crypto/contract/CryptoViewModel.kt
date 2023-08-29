package com.appninjas.composeweather_2.presentation.screens.crypto.contract

import com.appninjas.composeweather_2.presentation.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

class CryptoViewModel : BaseViewModel<CryptoContract.Event, CryptoContract.State, CryptoContract.Effect>() {

    override fun setInitialState(): CryptoContract.State = CryptoContract.State.Loading

    override fun handleEvents(event: CryptoContract.Event) = when(event) {
        is CryptoContract.Event.LoadCryptoData -> loadCryptoData()
    }

    private fun loadCryptoData() {

    }

}