/* package com.example.app3apis.ui.favoritos

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
*/
package com.example.app3apis.ui.favoritos

import androidx.lifecycle.ViewModel
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
    val isLoading: Boolean = false
)

class FavoritosViewModel(
    private val repository: SQLiteCloudRepository = SQLiteCloudRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritosUiState())
    val uiState: StateFlow<FavoritosUiState> = _uiState.asStateFlow()

    init {
        cargarFavoritos()
    }

    fun cargarFavoritos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val lista = repository.obtenerFavoritos()
                _uiState.update { it.copy(favoritos = lista, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    // Esta es la función que llama tu FavoritosScreen cuando tocan el botón de quitar
    fun quitar(artwork: ArtworkModel) {
        viewModelScope.launch {
            repository.eliminarFavorito(artwork)
            // Actualizamos la lista localmente en la interfaz
            _uiState.update { state ->
                state.copy(favoritos = state.favoritos.filter { it.id != artwork.id })
            }
        }
    }

    fun agregar(artwork: ArtworkModel) {
        viewModelScope.launch {
            repository.agregarFavorito(artwork)
            cargarFavoritos()
        }
    }
}