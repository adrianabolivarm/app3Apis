package com.example.app3apis.ui.favoritos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.app3apis.data.local.AppDatabase
import com.example.app3apis.data.repository.FavoritosRepository
import com.example.app3apis.domain.model.ArtworkModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FavoritosUiState(
    val favoritos: List<ArtworkModel> = emptyList(),
    val cargando: Boolean = false
)

class FavoritosViewModel(application: Application) : AndroidViewModel(application) {
    private val favoritosRepository = FavoritosRepository(AppDatabase.obtener(application).favoritoDao())

    val uiState: StateFlow<FavoritosUiState> = favoritosRepository.observarFavoritos()
        .map { lista -> FavoritosUiState(favoritos = lista, cargando = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FavoritosUiState()
        )

    fun quitar(artwork: ArtworkModel) {
        viewModelScope.launch {
            favoritosRepository.quitar(artwork.id)
        }
    }
}
