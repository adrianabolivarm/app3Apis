package com.example.app3apis.data.repository

import com.example.app3apis.data.remote.SQLiteCloudApiService
import com.example.app3apis.domain.model.ArtworkModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SQLiteCloudRepository(
    private val apiService: SQLiteCloudApiService = SQLiteCloudApiService()
) {

    companion object {
        val favoritosMemoria = mutableListOf<ArtworkModel>()
    }

    suspend fun obtenerFavoritos(): List<ArtworkModel> = withContext(Dispatchers.IO) {
        // En un caso real con API REST se consultaría la tabla favoritos.
        // Por ahora devolvemos la lista en memoria sincronizada.
        return@withContext favoritosMemoria.toList()
    }

    suspend fun agregarFavorito(artwork: ArtworkModel) = withContext(Dispatchers.IO) {
        val query = "INSERT OR REPLACE INTO favoritos (id, title, artist, imageUrl, date, museum) VALUES (${artwork.id}, '${artwork.title.replace("'", "''")}', '${artwork.artist.replace("'", "''")}', '${artwork.imageUrl}', '${artwork.date}', '${artwork.museum}');"
        apiService.ejecutarConsulta(query)

        if (!favoritosMemoria.any { it.id == artwork.id }) {
            favoritosMemoria.add(artwork)
        }
    }

    suspend fun eliminarFavorito(artwork: ArtworkModel) = withContext(Dispatchers.IO) {
        val query = "DELETE FROM favoritos WHERE id = ${artwork.id};"
        apiService.ejecutarConsulta(query)

        favoritosMemoria.removeAll { it.id == artwork.id }
    }
}
