package com.example.app3apis.data.mapper

import com.example.app3apis.data.model.WeatherResponseDto
import com.example.app3apis.domain.model.WeatherModel

class WeatherMapper {
    fun mapToDomain(dto: WeatherResponseDto): WeatherModel {
        val temp = dto.currentWeather?.temperature ?: 0.0
        val wind = dto.currentWeather?.windspeed ?: 0.0
        val code = dto.currentWeather?.weathercode ?: 0
        val condition = when (code) {
            0 -> "Sunny / Clear"
            1, 2, 3 -> "Partly Cloudy"
            45, 48 -> "Foggy"
            51, 53, 55, 61, 63 -> "Rainy"
            else -> "Fair"
        }
        return WeatherModel(
            temperature = temp,
            windspeed = wind,
            condition = condition,
            location = "La Paz"
        )
    }
}
