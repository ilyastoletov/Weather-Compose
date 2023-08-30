package com.appninjas.composeweather_2.presentation.screens.crypto.contract

import androidx.lifecycle.viewModelScope
import com.appninjas.composeweather_2.presentation.core.BaseViewModel
import com.appninjas.domain.usecase.crypto.GetCryptoListUseCase
import com.appninjas.domain.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoViewModel @Inject constructor(private val getCryptoListUseCase: GetCryptoListUseCase) :
    BaseViewModel<CryptoContract.Event, CryptoContract.State, CryptoContract.Effect>() {

    override fun setInitialState(): CryptoContract.State = CryptoContract.State.Loading

    override fun handleEvents(event: CryptoContract.Event) = when(event) {
        is CryptoContract.Event.LoadCryptoData -> loadCryptoData()
    }

    private fun loadCryptoData() {
        viewModelScope.launch(dispatcher) {
            when(val networkResponse = getCryptoListUseCase.invoke()) {
                is Response.Success.Data -> setState {
                    CryptoContract.State.CryptoDataLoaded(networkResponse.data)
                }
                is Response.Success.Empty,
                is Response.Error -> setState { CryptoContract.State.NetworkFailure }
            }
        }
    }

}