package com.example.app3apis.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app3apis.data.repository.ArticRepository
import com.example.app3apis.data.repository.MetRepository
import com.example.app3apis.data.repository.WeatherRepository
import com.example.app3apis.domain.model.ArtworkModel
import com.example.app3apis.domain.model.WeatherModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DashboardUiState(
    val isLoading: Boolean = false,
    val weather: WeatherModel = WeatherModel(18.5, 12.0, "Sunny / Clear", "La Paz"),
    val articArtworks: List<ArtworkModel> = listOf(
        ArtworkModel(1, "A Sunday on La Grande Jatte", "Georges Seurat", "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?w=800", "1884–1886", "Art Institute of Chicago"),
        ArtworkModel(2, "Nighthawks", "Edward Hopper", "https://images.unsplash.com/photo-1577083552431-6e5fd01aa342?w=800", "1942", "Art Institute of Chicago"),
        ArtworkModel(3, "The Bedroom", "Vincent van Gogh", "https://images.unsplash.com/photo-1582561254395-859a72120015?w=800", "1889", "Art Institute of Chicago")
    ),
    val metArtworks: List<ArtworkModel> = listOf(
        ArtworkModel(436532, "Wheat Fields with Cypresses", "Vincent van Gogh", "https://images.unsplash.com/photo-1565008447742-97f6f38c985c?w=800", "1889", "The Metropolitan Museum of Art"),
        ArtworkModel(459080, "The Dance Class", "Edgar Degas", "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=800", "1874", "The Metropolitan Museum of Art")
    ),
    val errorMessage: String? = null
)

class DashboardViewModel(
    private val weatherRepository: WeatherRepository = WeatherRepository(),
    private val articRepository: ArticRepository = ArticRepository(),
    private val metRepository: MetRepository = MetRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    var selectedArtwork = mutableStateOf<ArtworkModel?>(null)
        private set

    init {
        fetchAllData()
    }

    fun selectArtwork(artwork: ArtworkModel?) {
        selectedArtwork.value = artwork
    }

    fun fetchAllData() {
        viewModelScope.launch {
            try {
                val weatherResult = runCatching { weatherRepository.getWeather() }.getOrNull()
                val articResult = runCatching { articRepository.getArtworks() }.getOrNull() ?: emptyList()
                val metResult = runCatching { metRepository.getMetArtworks() }.getOrNull() ?: emptyList()

                _uiState.update { current ->
                    current.copy(
                        weather = weatherResult ?: current.weather,
                        articArtworks = if (articResult.isNotEmpty()) articResult else current.articArtworks,
                        metArtworks = if (metResult.isNotEmpty()) metResult else current.metArtworks
                    )
                }
            } catch (e: Exception) {
                // Keep existing data on failure
            }
        }
    }
}
