package com.example.app3apis.data.repository

import com.example.app3apis.data.remote.SQLiteCloudApi
import com.example.app3apis.data.remote.SqlBody
import com.example.app3apis.domain.model.ArtworkModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SQLiteCloudRepository {

    private val api: SQLiteCloudApi = Retrofit.Builder()
        .baseUrl("https://ct9r2rqcvk.g5.gateway.sqlite.cloud/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SQLiteCloudApi::class.java)

    suspend fun obtenerFavoritos(): List<ArtworkModel> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.obtenerFavoritos()
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.map { entity ->
                    ArtworkModel(
                        id = entity.id,
                        title = entity.title,
                        artist = entity.artist,
                        imageUrl = entity.imageUrl,
                        date = entity.date,
                        museum = entity.museum
                    )
                }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun agregarFavorito(artwork: ArtworkModel) = withContext(Dispatchers.IO) {
        try {
            val body = SqlBody(
                id = artwork.id,
                title = artwork.title,
                artist = artwork.artist,
                imageUrl = artwork.imageUrl,
                date = artwork.date,
                museum = artwork.museum,
                agregadoEn = System.currentTimeMillis()
            )
            api.agregarFavorito(body)
        } catch (e: Exception) {
            // handle error
        }
    }

    suspend fun eliminarFavorito(artwork: ArtworkModel) = withContext(Dispatchers.IO) {
        try {
            val body = SqlBody(id = artwork.id)
            api.eliminarFavorito(body)
        } catch (e: Exception) {
            // handle error
        }
    }
}
