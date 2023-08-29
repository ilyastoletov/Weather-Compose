package com.appninjas.domain.usecase.weather

import com.appninjas.domain.repository.WeatherRepository

class GetWeatherUseCase(private val repository: WeatherRepository) {
    suspend fun invoke(lat: Number, lon: Number) = repository.getWeatherData(lat, lon)
}