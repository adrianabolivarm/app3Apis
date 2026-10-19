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
    val weather: WeatherModel? = null,
    val articArtworks: List<ArtworkModel> = emptyList(),
    val metArtworks: List<ArtworkModel> = emptyList(),
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
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                // Call 3 independent repositories
                val weatherResult = runCatching { weatherRepository.getWeather() }.getOrNull()
                val articResult = runCatching { articRepository.getArtworks() }.getOrNull() ?: emptyList()
                val metResult = runCatching { metRepository.getMetArtworks() }.getOrNull() ?: emptyList()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        weather = weatherResult,
                        articArtworks = articResult,
                        metArtworks = metResult
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.localizedMessage ?: "Unknown error occurred"
                    )
                }
            }
        }
    }
}
