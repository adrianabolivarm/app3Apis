package com.example.app3apis.data.repository

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
