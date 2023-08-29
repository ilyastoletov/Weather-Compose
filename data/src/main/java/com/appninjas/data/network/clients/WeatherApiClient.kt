package com.appninjas.data.network.clients

import com.appninjas.data.network.dto.WeatherResponseDto
import com.appninjas.data.utils.NetworkConfig
import com.appninjas.domain.model.Address
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiClient {

    @GET(NetworkConfig.Weather.Geocoder.GET_ADDRESS_ROUTE)
    suspend fun getAddress(@Query("lat") lat: Number, @Query("lon") lon: Number): Address

    @GET(NetworkConfig.Weather.GET_WEATHER_ROUTE)
    suspend fun getWeather(@Query("lat") lat: Number, @Query("lon") lon: Number): WeatherResponseDto

}