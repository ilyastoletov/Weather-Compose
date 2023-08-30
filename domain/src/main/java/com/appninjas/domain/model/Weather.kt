package com.appninjas.domain.model

data class Weather(
    val iconLink: String,
    val conditionLabel: String,
    val degreesCount: Int,
    val feelsLike: Number,
    val partlyPrediction: List<WeatherPartly>
)
