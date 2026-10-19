package com.example.app3apis.data.repository

import com.example.app3apis.data.mapper.MetMapper
import com.example.app3apis.data.remote.MetApiService
import com.example.app3apis.domain.model.ArtworkModel
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MetRepository(
    private val mapper: MetMapper = MetMapper()
) {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(3, TimeUnit.SECONDS)
        .readTimeout(3, TimeUnit.SECONDS)
        .build()

    private val api: MetApiService = Retrofit.Builder()
        .baseUrl("https://collectionapi.metmuseum.org/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MetApiService::class.java)

    suspend fun getMetArtworks(): List<ArtworkModel> {
        return try {
            val reliableIds = listOf(436532, 459080, 437133, 544455, 436121)
            val artworks = mutableListOf<ArtworkModel>()
            for (id in reliableIds) {
                try {
                    val obj = api.getObjectDetails(id)
                    if (!obj.title.isNullOrBlank()) {
                        artworks.add(mapper.mapToDomain(obj))
                    }
                } catch (e: Exception) {
                    // skip
                }
            }
            if (artworks.isNotEmpty()) {
                artworks
            } else {
                getFallbackMetArtworks()
            }
        } catch (e: Exception) {
            getFallbackMetArtworks()
        }
    }

    private fun getFallbackMetArtworks(): List<ArtworkModel> {
        return listOf(
            ArtworkModel(
                id = 436532,
                title = "Wheat Fields with Cypresses",
                artist = "Vincent van Gogh",
                imageUrl = "https://images.metmuseum.org/CRDImages/ep/web-large/DP145328.jpg",
                date = "1889",
                museum = "The Metropolitan Museum of Art"
            ),
            ArtworkModel(
                id = 459080,
                title = "The Dance Class",
                artist = "Edgar Degas",
                imageUrl = "https://images.metmuseum.org/CRDImages/ph/web-large/DP-14660-001.jpg",
                date = "1874",
                museum = "The Metropolitan Museum of Art"
            )
        )
    }
}
