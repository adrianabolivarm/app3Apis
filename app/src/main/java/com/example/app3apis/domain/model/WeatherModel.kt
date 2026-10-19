package com.example.app3apis.domain.model

data class WeatherModel(
    val temperature: Double,
    val windspeed: Double,
    val condition: String,
    val location: String
)
