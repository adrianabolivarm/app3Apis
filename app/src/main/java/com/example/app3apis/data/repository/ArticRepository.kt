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
        return try {
            val response = api.getArtworks(limit = 10)
            val list = response.data ?: emptyList()
            if (list.isNotEmpty()) {
                list.map { mapper.mapToDomain(it) }
            } else {
                getFallbackArtworks()
            }
        } catch (e: Exception) {
            getFallbackArtworks()
        }
    }

    private fun getFallbackArtworks(): List<ArtworkModel> {
        return listOf(
            ArtworkModel(
                id = 1,
                title = "A Sunday on La Grande Jatte",
                artist = "Georges Seurat",
                imageUrl = "https://www.artic.edu/iiif/2/27df7cbd-7f8e-d1ea-6cbb-335133615e47/full/843,/0/default.jpg",
                date = "1884–1886",
                museum = "Art Institute of Chicago"
            ),
            ArtworkModel(
                id = 2,
                title = "Nighthawks",
                artist = "Edward Hopper",
                imageUrl = "https://www.artic.edu/iiif/2/5b357630-3982-2ea9-446f-f236ae0335e2/full/843,/0/default.jpg",
                date = "1942",
                museum = "Art Institute of Chicago"
            ),
            ArtworkModel(
                id = 3,
                title = "The Bedroom",
                artist = "Vincent van Gogh",
                imageUrl = "https://www.artic.edu/iiif/2/1d82ea1c-d7d8-1127-1f49-514c35e3940c/full/843,/0/default.jpg",
                date = "1889",
                museum = "Art Institute of Chicago"
            )
        )
    }
}
