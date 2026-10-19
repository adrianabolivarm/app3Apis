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
        ArtworkModel(1, "A Sunday on La Grande Jatte", "Georges Seurat", "https://www.artic.edu/iiif/2/27df7cbd-7f8e-d1ea-6cbb-335133615e47/full/843,/0/default.jpg", "1884–1886", "Art Institute of Chicago"),
        ArtworkModel(2, "Nighthawks", "Edward Hopper", "https://www.artic.edu/iiif/2/5b357630-3982-2ea9-446f-f236ae0335e2/full/843,/0/default.jpg", "1942", "Art Institute of Chicago"),
        ArtworkModel(3, "The Bedroom", "Vincent van Gogh", "https://www.artic.edu/iiif/2/1d82ea1c-d7d8-1127-1f49-514c35e3940c/full/843,/0/default.jpg", "1889", "Art Institute of Chicago")
    ),
    val metArtworks: List<ArtworkModel> = listOf(
        ArtworkModel(436532, "Wheat Fields with Cypresses", "Vincent van Gogh", "https://images.metmuseum.org/CRDImages/ep/web-large/DP145328.jpg", "1889", "The Metropolitan Museum of Art"),
        ArtworkModel(459080, "The Dance Class", "Edgar Degas", "https://images.metmuseum.org/CRDImages/ph/web-large/DP-14660-001.jpg", "1874", "The Metropolitan Museum of Art")
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
            // Do not set isLoading = true so the app never shows a loading spinner
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
                // Keep existing data on failure, no crash or loading block
            }
        }
    }
}
