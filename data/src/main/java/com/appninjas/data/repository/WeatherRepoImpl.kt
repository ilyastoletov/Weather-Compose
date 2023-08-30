package com.appninjas.data.repository

import com.appninjas.data.network.clients.WeatherApiClient
import com.appninjas.data.network.dto.WeatherResponseDto.Companion.toWeather
import com.appninjas.domain.model.UserLocation
import com.appninjas.domain.model.Weather
import com.appninjas.domain.repository.WeatherRepository
import com.appninjas.domain.utils.Response
import retrofit2.HttpException
import java.net.ConnectException
import javax.inject.Inject

class WeatherRepoImpl @Inject constructor(private val weatherApiClient: WeatherApiClient): WeatherRepository {

    override suspend fun getWeatherData(location: UserLocation): Response<Weather> {
        return try {
            val networkResponse = weatherApiClient.getWeather(location.lat, location.lon)
            Response.Success.Data(networkResponse.toWeather())
        } catch(e: HttpException) {
            when (e.code()) {
                404 -> Response.Error("Ошибка сети: страница не найдена. Попробуйте позже")
                500 -> Response.Error("Ошибка сервера. Попробуйте зайти позже")
                403 -> Response.Error("Ошибка сети. Скорее всего исчерпан лимит запросов к API сервиса погоды. Попробуйте зайти завтра")
                else -> Response.Error("Неизвестная ошибка сети")
            }
        } catch(e: ConnectException) {
            Response.Error("Не удалось подключится к серверу. Проверьте качество соединения с сетью")
        } finally {
            Response.Success.Empty
        }
    }

}