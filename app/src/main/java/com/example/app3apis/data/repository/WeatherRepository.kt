package com.example.app3apis.data.repository

import com.example.app3apis.data.mapper.WeatherMapper
import com.example.app3apis.data.remote.OpenMeteoApiService
import com.example.app3apis.domain.model.WeatherModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRepository(
    private val mapper: WeatherMapper = WeatherMapper()
) {
    private val api: OpenMeteoApiService = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(OpenMeteoApiService::class.java)

    suspend fun getWeather(): WeatherModel {
        val dto = api.getWeather()
        return mapper.mapToDomain(dto)
    }
}
