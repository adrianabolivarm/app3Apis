package com.example.app3apis.data.repository

import com.example.app3apis.data.mapper.WeatherMapper
import com.example.app3apis.data.remote.OpenMeteoApiService
import com.example.app3apis.domain.model.WeatherModel
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class WeatherRepository(
    private val mapper: WeatherMapper = WeatherMapper()
) {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(3, TimeUnit.SECONDS)
        .readTimeout(3, TimeUnit.SECONDS)
        .build()

    private val api: OpenMeteoApiService = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(OpenMeteoApiService::class.java)

    suspend fun getWeather(): WeatherModel {
        return try {
            val dto = api.getWeather()
            mapper.mapToDomain(dto)
        } catch (e: Exception) {
            // Fallback weather if network times out or fails
            WeatherModel(
                temperature = 18.5,
                windspeed = 12.0,
                condition = "Sunny / Clear",
                location = "La Paz"
            )
        }
    }
}
