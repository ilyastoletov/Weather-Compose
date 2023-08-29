package com.appninjas.domain.model

data class WeatherPartly(
    val temp: Number,
    val feelsLike: Number,
    val partOfDay: String,
    val iconLink: String
)
