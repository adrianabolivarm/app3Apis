package com.example.app3apis.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponseDto(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("current_weather") val currentWeather: CurrentWeatherDto?
)

data class CurrentWeatherDto(

    @SerializedName("temperature") val temperature: Double,
    @SerializedName("windspeed") val windspeed: Double,
    @SerializedName("weathercode") val weathercode: Int
)
