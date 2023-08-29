package com.appninjas.composeweather_2.presentation.screens.weather.contract

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.appninjas.composeweather_2.R
import com.appninjas.composeweather_2.presentation.core.BaseViewModel
import com.appninjas.domain.model.Weather
import com.appninjas.domain.usecase.weather.GeocodeCoordinatesUseCase
import com.appninjas.domain.usecase.weather.GetWeatherUseCase
import com.appninjas.domain.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val geocodeCoordinatesUseCase: GeocodeCoordinatesUseCase,
                                           private val getWeatherUseCase: GetWeatherUseCase) :
    BaseViewModel<WeatherContract.Event, WeatherContract.State, WeatherContract.Effect>() {

    override fun setInitialState(): WeatherContract.State = WeatherContract.State.Loading

    override fun handleEvents(event: WeatherContract.Event) = when(event) {
        is WeatherContract.Event.GeocodeCoordinates -> geocodeCoordinates(lat = event.lat, lon = event.lon)
        is WeatherContract.Event.LoadWeather -> getWeatherData(lat = event.lat, lon = event.lon)
    }

    private fun getWeatherData(lat: Number, lon: Number) {
       viewModelScope.launch(dispatcher) {
            when(val weatherResponse = getWeatherUseCase.invoke(lat, lon)) {
                is Response.Success.Data -> setState {
                    WeatherContract.State.WeatherDataLoaded(weather = weatherResponse.data)
                }
                is Response.Success.Empty -> setState { WeatherContract.State.NetworkFailure }
                is Response.Error -> { setState { WeatherContract.State.NetworkFailure }; setEffect { WeatherContract.Effect.ShowErrorToast } }
            }
        }
    }

    private fun geocodeCoordinates(lat: Number, lon: Number) {
        viewModelScope.launch(dispatcher) {
            val address = geocodeCoordinatesUseCase.invoke(lat, lon)
            setState {
                WeatherContract.State.GeocodeAddressLoaded(address)
            }
        }
    }

}