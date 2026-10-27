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

    suspend fun obtenerFavoritos(): List<ArtworkModel> = withContext(Dispatchers.IO) {
        return@withContext try {
            Log.d("SQLiteCloud", "Consultando favoritos a la nube (Edge Function)...")
            val response = api.obtenerFavoritos()
            if (response.isSuccessful && response.body() != null) {
                val lista = response.body()!!.map { entity ->
                    ArtworkModel(
                        id = entity.id,
                        title = entity.title,
                        artist = entity.artist,
                        imageUrl = entity.imageUrl,
                        date = entity.date,
                        museum = entity.museum
                    )
                }
                Log.d("SQLiteCloud", "Favoritos obtenidos con éxito: ${lista.size} elementos")
                lista
            } else {
                Log.e("SQLiteCloud", "Error al obtener favoritos: ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("SQLiteCloud", "Excepción al obtener favoritos: ${e.localizedMessage}")
            emptyList()
        }
    }

    suspend fun agregarFavorito(artwork: ArtworkModel) = withContext(Dispatchers.IO) {
        try {
            Log.d("SQLiteCloud", "Enviando 'Me gusta' para: ${artwork.title} (ID: ${artwork.id})")
            val body = SqlBody(
                id = artwork.id,
                title = artwork.title,
                artist = artwork.artist,
                imageUrl = artwork.imageUrl,
                date = artwork.date,
                museum = artwork.museum,
                agregadoEn = System.currentTimeMillis()
            )
            val response = api.agregarFavorito(body)
            if (response.isSuccessful) {
                Log.d("SQLiteCloud", "¡Favorito guardado en la nube con éxito! 🎉")
            } else {
                Log.e("SQLiteCloud", "Error en servidor cloud: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("SQLiteCloud", "Excepción al agregar favorito: ${e.localizedMessage}")
        }
    }

    suspend fun eliminarFavorito(artwork: ArtworkModel) = withContext(Dispatchers.IO) {
        try {
            Log.d("SQLiteCloud", "Eliminando favorito ID: ${artwork.id}")
            val body = SqlBody(id = artwork.id)
            val response = api.eliminarFavorito(body)
            if (response.isSuccessful) {
                Log.d("SQLiteCloud", "¡Favorito eliminado de la nube con éxito!")
            } else {
                Log.e("SQLiteCloud", "Error al eliminar en nube: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("SQLiteCloud", "Excepción al eliminar favorito: ${e.localizedMessage}")
        }
    }
}
