package com.appninjas.domain.repository

import com.appninjas.domain.model.Address
import com.appninjas.domain.model.Weather
import com.appninjas.domain.utils.Response

interface WeatherRepository {
    suspend fun getWeatherData(lat: Number, lon: Number): Response<Weather>
    suspend fun geocodeCoordinates(lat: Number, lon: Number): Address
}