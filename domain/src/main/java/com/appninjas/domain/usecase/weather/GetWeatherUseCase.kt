package com.appninjas.domain.usecase.weather

import com.appninjas.domain.model.UserLocation
import com.appninjas.domain.repository.WeatherRepository

class GetWeatherUseCase(private val repository: WeatherRepository) {
    suspend fun invoke(location: UserLocation) = repository.getWeatherData(location)
}