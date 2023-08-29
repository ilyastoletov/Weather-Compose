package com.appninjas.domain.usecase.weather

import com.appninjas.domain.repository.WeatherRepository

class GeocodeCoordinatesUseCase(private val repository: WeatherRepository) {
    suspend fun invoke(lat: Number, lon: Number) = repository.geocodeCoordinates(lat, lon)
}