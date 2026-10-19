package com.example.app3apis.data.repository

import com.example.app3apis.data.mapper.ArticMapper
import com.example.app3apis.data.remote.ArticApiService
import com.example.app3apis.domain.model.ArtworkModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ArticRepository(
    private val mapper: ArticMapper = ArticMapper()
) {
    private val api: ArticApiService = Retrofit.Builder()
        .baseUrl("https://api.artic.edu/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ArticApiService::class.java)

    suspend fun getArtworks(): List<ArtworkModel> {
        val response = api.getArtworks(limit = 10)
        val list = response.data ?: emptyList()
        return list.map { mapper.mapToDomain(it) }
    }
}
