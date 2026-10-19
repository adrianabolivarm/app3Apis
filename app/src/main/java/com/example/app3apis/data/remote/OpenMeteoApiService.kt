package com.example.app3apis.data.remote

import com.example.app3apis.data.model.WeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoApiService {
    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") lat: Double = -16.5,
        @Query("longitude") lon: Double = -68.15,
        @Query("current_weather") currentWeather: Boolean = true
    ): WeatherResponseDto
}
