package com.example.app3apis.data.repository

import com.example.app3apis.data.mapper.ArticMapper
import com.example.app3apis.data.remote.ArticApiService
import com.example.app3apis.domain.model.ArtworkModel
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ArticRepository(
    private val mapper: ArticMapper = ArticMapper()
) {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(3, TimeUnit.SECONDS)
        .readTimeout(3, TimeUnit.SECONDS)
        .build()

    private val api: ArticApiService = Retrofit.Builder()
        .baseUrl("https://api.artic.edu/")
        .client(okHttpClient)
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
                imageUrl = "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=800",
                date = "1884–1886",
                museum = "Art Institute of Chicago"
            ),
            ArtworkModel(
                id = 2,
                title = "Nighthawks",
                artist = "Edward Hopper",
                imageUrl = "https://images.unsplash.com/photo-1577083552431-6e5fd01aa342?w=800",
                date = "1942",
                museum = "Art Institute of Chicago"
            ),
            ArtworkModel(
                id = 3,
                title = "The Bedroom",
                artist = "Vincent van Gogh",
                imageUrl = "https://images.unsplash.com/photo-1582561254395-859a72120015?w=800",
                date = "1889",
                museum = "Art Institute of Chicago"
            )
        )
    }
}
