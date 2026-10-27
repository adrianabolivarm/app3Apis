/*package com.example.app3apis.data.repository

import com.example.app3apis.data.local.FavoritoDao
import com.example.app3apis.data.mapper.aArtworkModel
import com.example.app3apis.data.mapper.aFavoritoEntity
import com.example.app3apis.domain.model.ArtworkModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritosRepository(private val dao: FavoritoDao) {

    fun observarFavoritos(): Flow<List<ArtworkModel>> =
        dao.observarFavoritos().map { entidades -> entidades.map { it.aArtworkModel() } }

    fun observarIds(): Flow<Set<Int>> =
        dao.observarIdsFavoritos().map { ids -> ids.toSet() }

    suspend fun agregar(artwork: ArtworkModel) = dao.insertar(artwork.aFavoritoEntity())

    suspend fun quitar(id: Int) = dao.eliminarPorId(id)
}
*/
package com.example.app3apis.data.repository

import com.example.app3apis.data.remote.SQLiteCloudApiService
import com.example.app3apis.domain.model.ArtworkModel

class FavoritosRepository(
    private val apiService: SQLiteCloudApiService = SQLiteCloudApiService()
) {

    // Simula o realiza la consulta para traer los favoritos desde SQLite Cloud
    suspend fun obtenerFavoritos(): List<ArtworkModel> {
        // Aquí se conectará a la nube para hacer un "SELECT * FROM favoritos"
        return emptyList()
    }

    // Inserta un favorito en la nube mediante SQL
    suspend fun agregarFavorito(artwork: ArtworkModel) {
        val query = "INSERT INTO favoritos (id, title, artist, museum, imageUrl) VALUES ('${artwork.id}', '${artwork.title}', '${artwork.artist}', '${artwork.museum}', '${artwork.imageUrl}');"
        apiService.ejecutarConsulta(query)
    }

    // Elimina un favorito de la nube mediante SQL
    suspend fun eliminarFavorito(artwork: ArtworkModel) {
        val query = "DELETE FROM favoritos WHERE id = '${artwork.id}';"
        apiService.ejecutarConsulta(query)
    }
}