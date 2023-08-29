package com.appninjas.data.network.dto

import com.appninjas.domain.model.Weather
import com.appninjas.domain.model.WeatherPartly
import com.google.gson.annotations.SerializedName

data class WeatherResponseDto(
    val fact: WeatherFact,
    val parts: List<WeatherParts>
) {
    companion object {
        fun WeatherResponseDto.toWeather(): Weather = Weather(
            iconLink = fact.iconLink,
            conditionLabel = fact.condition.toFullConditionString(),
            degreesCount = fact.temp.toInt(),
            partlyPrediction = parts.toPartlyWeatherList()
        )

        private fun String.toFullConditionString(): String = when(this) {
                "clear" -> "Ясно"
                "partly-cloudy" -> "Малооблачно"
                "cloudy" -> "Облачно с прояснениями"
                "overcast" -> "Пасмурно"
                "light-rain" -> "Небольшой дождь"
                "rain" -> "Дождь"
                "heavy-rain", "showers" -> "Сильный дождь"
                "wet-snow" -> "Мокрый снег"
                "light-snow", "snow" -> "Снег"
                "snow-showers" -> "Снегопад"
                "hail" -> "Град"
                "thunderstorm", "thunderstorm-with-rain", "thunderstorm-with-hail" -> "Гроза"
                else -> "Ошибка определения"
            }

        private fun List<WeatherParts>.toPartlyWeatherList(): List<WeatherPartly> {
            return this.map { part ->
                WeatherPartly(
                    part.temp,
                    part.feelsLike,
                    part.partOfDay.toRussianPartOfDay(),
                    part.iconLink) }
        }

        private fun String.toRussianPartOfDay(): String = when(this) {
            "morning" -> "Утро"
            "day" -> "День"
            "evening" -> "Вечер"
            "night" -> "Ночь"
            else -> "Ошибка"
        }
    }
}

data class WeatherParts(
    val temp: Number,
    @SerializedName("feels_like")
    val feelsLike: Number,
    @SerializedName("part_of_day")
    val partOfDay: String,
    @SerializedName("icon_link")
    val iconLink: String
)

data class WeatherFact(
    val temp: Number,
    @SerializedName("feels_like")
    val feelsLike: Number,
    val condition: String,
    @SerializedName("icon_link")
    val iconLink: String
)