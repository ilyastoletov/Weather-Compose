package com.appninjas.domain.repository

import com.appninjas.domain.model.UserLocation
import com.appninjas.domain.model.Weather
import com.appninjas.domain.utils.Response

interface WeatherRepository {
    suspend fun getWeatherData(location: UserLocation): Response<Weather>
}