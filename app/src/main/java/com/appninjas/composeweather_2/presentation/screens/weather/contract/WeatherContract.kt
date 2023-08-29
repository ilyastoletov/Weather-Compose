package com.appninjas.composeweather_2.presentation.screens.weather.contract

import com.appninjas.composeweather_2.presentation.core.ViewEffect
import com.appninjas.composeweather_2.presentation.core.ViewEvent
import com.appninjas.composeweather_2.presentation.core.ViewState
import com.appninjas.domain.model.Address
import com.appninjas.domain.model.Weather

object WeatherContract {

    sealed class Event : ViewEvent {
        data class LoadWeather(val lat: Number, val lon: Number) : Event()
        data class GeocodeCoordinates(val lat: Number, val lon: Number) : Event()
    }

    sealed class State : ViewState {
        object Loading : State()
        data class WeatherDataLoaded(val weather: Weather) : State()
        data class GeocodeAddressLoaded(val address: Address) : State()
        object NetworkFailure : State()
    }

    sealed class Effect : ViewEffect {
        object ShowErrorToast : Effect()
    }

}