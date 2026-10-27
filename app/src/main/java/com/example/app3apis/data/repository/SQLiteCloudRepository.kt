package com.example.app3apis.data.repository

import com.example.app3apis.data.remote.SQLiteCloudApiService
import com.example.app3apis.domain.model.ArtworkModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SQLiteCloudRepository(
    private val apiService: SQLiteCloudApiService = SQLiteCloudApiService()
) {

    companion object {
        // Lista en memoria local para garantizar que la UI se actualice al instante en la práctica
        val favoritosMemoria = mutableListOf<ArtworkModel>(
            // Obra de ejemplo inicial opcional para pruebas
        )
    }

    suspend fun obtenerFavoritos(): List<ArtworkModel> = withContext(Dispatchers.IO) {
        // Retorna la lista local actual para que la pantalla de favoritos muestre los items al instante
        return@withContext favoritosMemoria.toList()
    }

    suspend fun agregarFavorito(artwork: ArtworkModel) = withContext(Dispatchers.IO) {
        // 1. Actualizar memoria local al instante para que la UI responda inmediatamente
        if (!favoritosMemoria.any { it.id == artwork.id }) {
            favoritosMemoria.add(artwork)
        }

        // 2. Intentar registrar en SQLite Cloud en segundo plano
        try {
            val query = "INSERT OR REPLACE INTO favoritos (id, title, artist, imageUrl, date, museum) VALUES (${artwork.id}, '${artwork.title.replace("'", "''")}', '${artwork.artist.replace("'", "''")}', '${artwork.imageUrl}', '${artwork.date}', '${artwork.museum}');"
            apiService.ejecutarConsulta(query)
        } catch (e: Exception) {
            // Evitar que un fallo de red detenga la app en la práctica
        }
    }

    suspend fun eliminarFavorito(artwork: ArtworkModel) = withContext(Dispatchers.IO) {
        // 1. Actualizar memoria local al instante
        favoritosMemoria.removeAll { it.id == artwork.id }

        // 2. Intentar eliminar en SQLite Cloud en segundo plano
        try {
            val query = "DELETE FROM favoritos WHERE id = ${artwork.id};"
            apiService.ejecutarConsulta(query)
        } catch (e: Exception) {
            // Evitar fallos de red
        }
    }
}
