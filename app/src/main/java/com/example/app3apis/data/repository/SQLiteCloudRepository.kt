package com.example.app3apis.data.repository

import android.util.Log
import com.example.app3apis.data.local.FavoritoEntity
import com.example.app3apis.data.remote.SQLiteCloudApi
import com.example.app3apis.data.remote.SqlBody
import com.example.app3apis.domain.model.ArtworkModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
        return@withContext try {
            val response = api.obtenerFavoritos()
            if (response.isSuccessful && response.body() != null) {
                val jsonString = response.body()!!.string()
                Log.d("SQLiteCloud", "Respuesta cruda de obtener_favoritos: $jsonString")

                val gson = Gson()
                val listType = object : TypeToken<List<FavoritoEntity>>() {}.type
                val entities: List<FavoritoEntity> = try {
                    gson.fromJson(jsonString, listType) ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }

                val cloudArtworks = entities.map { entity ->
                    ArtworkModel(
                        id = entity.id,
                        title = entity.title,
                        artist = entity.artist,
                        imageUrl = entity.imageUrl,
                        date = entity.date,
                        museum = entity.museum
                    )
                }

                val combined = (favoritosMemoria + cloudArtworks).distinctBy { it.id }
                favoritosMemoria.clear()
                favoritosMemoria.addAll(combined)

                favoritosMemoria.toList()
            } else {
                favoritosMemoria.toList()
            }
        } catch (e: Exception) {
            Log.e("SQLiteCloud", "Error al obtener favoritos de la nube: ${e.localizedMessage}")
            favoritosMemoria.toList()
        }
    }

    suspend fun agregarFavorito(artwork: ArtworkModel) = withContext(Dispatchers.IO) {
        if (!favoritosMemoria.any { it.id == artwork.id }) {
            favoritosMemoria.add(artwork)
        }

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
            val res = api.agregarFavorito(body)
            Log.d("SQLiteCloud", "Agregar favorito a nube -> Éxito: ${res.isSuccessful}")
        } catch (e: Exception) {
            Log.e("SQLiteCloud", "Excepción al agregar a nube: ${e.localizedMessage}")
        }
    }

    suspend fun eliminarFavorito(artwork: ArtworkModel) = withContext(Dispatchers.IO) {
        favoritosMemoria.removeAll { it.id == artwork.id }

        try {
            val body = SqlBody(id = artwork.id)
            val res = api.eliminarFavorito(body)
            Log.d("SQLiteCloud", "Eliminar favorito de nube -> Éxito: ${res.isSuccessful}")
        } catch (e: Exception) {
            Log.e("SQLiteCloud", "Excepción al eliminar en nube: ${e.localizedMessage}")
        }
    }
}
