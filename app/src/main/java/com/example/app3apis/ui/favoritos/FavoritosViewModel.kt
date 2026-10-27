package com.example.app3apis.ui.favoritos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app3apis.data.repository.SQLiteCloudRepository
import com.example.app3apis.domain.model.ArtworkModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FavoritosUiState(
    val favoritos: List<ArtworkModel> = emptyList(),
    val cargando: Boolean = false
)

class FavoritosViewModel(application: Application) : AndroidViewModel(application) {
    private val sqliteCloudRepository = SQLiteCloudRepository()

    private val _uiState = MutableStateFlow(FavoritosUiState())
    val uiState: StateFlow<FavoritosUiState> = _uiState.asStateFlow()

    init {
        cargarFavoritos()
    }

    fun cargarFavoritos() {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true) }
            val lista = try {
                sqliteCloudRepository.obtenerFavoritos()
            } catch (e: Exception) {
                emptyList()
            }
            _uiState.update { it.copy(favoritos = lista, cargando = false) }
        }
    }

    fun quitar(artwork: ArtworkModel) {
        viewModelScope.launch {
            sqliteCloudRepository.eliminarFavorito(artwork)
            cargarFavoritos()
        }
    }
}
