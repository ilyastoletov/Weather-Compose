package com.appninjas.composeweather_2.presentation.screens.weather.contract

import androidx.lifecycle.viewModelScope
import com.appninjas.composeweather_2.presentation.core.BaseViewModel
import com.appninjas.domain.model.UserLocation
import com.appninjas.domain.usecase.weather.GetWeatherUseCase
import com.appninjas.domain.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val getWeatherUseCase: GetWeatherUseCase) :
    BaseViewModel<WeatherContract.Event, WeatherContract.State, WeatherContract.Effect>() {

    override fun setInitialState(): WeatherContract.State = WeatherContract.State.Loading

    override fun handleEvents(event: WeatherContract.Event) = when(event) {
        is WeatherContract.Event.LoadWeather -> getWeatherData(event.location)
    }

    private fun getWeatherData(location: UserLocation) {
       viewModelScope.launch(dispatcher) {
            when(val weatherResponse = getWeatherUseCase.invoke(location)) {
                is Response.Success.Data -> setState {
                    WeatherContract.State.WeatherDataLoaded(weather = weatherResponse.data)
                }
                is Response.Success.Empty -> { setState { WeatherContract.State.NetworkFailure }; setEffect { WeatherContract.Effect.ShowErrorToast("Произошла ошибка сети") } }
                is Response.Error -> { setEffect { WeatherContract.Effect.ShowErrorToast(weatherResponse.errorMessage) }; setState { WeatherContract.State.NetworkFailure } }
            }
        }
    }

}