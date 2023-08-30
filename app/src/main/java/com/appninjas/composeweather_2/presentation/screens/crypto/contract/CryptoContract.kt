package com.appninjas.composeweather_2.presentation.screens.crypto.contract

import com.appninjas.composeweather_2.presentation.core.ViewEffect
import com.appninjas.composeweather_2.presentation.core.ViewEvent
import com.appninjas.composeweather_2.presentation.core.ViewState
import com.appninjas.domain.model.Crypto

object CryptoContract {

    sealed class Event : ViewEvent {
        object LoadCryptoData : Event()
    }

    sealed class State : ViewState {
        object Loading : State()
        data class CryptoDataLoaded(val cryptoList: List<Crypto>) : State()
        object NetworkFailure : State()
    }

    sealed class Effect : ViewEffect {
        object ShowErrorToast : Effect()
    }

}