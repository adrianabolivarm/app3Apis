package com.example.app3apis.data.repository

import android.util.Log
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

    companion object {
        val favoritosMemoria = mutableListOf<ArtworkModel>()
    }

    suspend fun obtenerFavoritos(): List<ArtworkModel> = withContext(Dispatchers.IO) {
        // Retornamos la memoria local garantizada para que la UI pinte el corazón y la lista al instante
        return@withContext favoritosMemoria.toList()
    }

    suspend fun agregarFavorito(artwork: ArtworkModel) = withContext(Dispatchers.IO) {
        // 1. Actualizar memoria local para que el corazón se ponga rojo al instante
        if (!favoritosMemoria.any { it.id == artwork.id }) {
            favoritosMemoria.add(artwork)
        }

        // 2. Intentar sincronizar con la Edge Function en la nube
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
            // Ignorar error de red para no bloquear la app
        }
    }

    suspend fun eliminarFavorito(artwork: ArtworkModel) = withContext(Dispatchers.IO) {
        // 1. Actualizar memoria local para que el corazón vuelva a blanco al instante
        favoritosMemoria.removeAll { it.id == artwork.id }

        // 2. Intentar sincronizar con la Edge Function en la nube
        try {
            val body = SqlBody(id = artwork.id)
            api.eliminarFavorito(body)
        } catch (e: Exception) {
            // Ignorar error de red
        }
    }
}
