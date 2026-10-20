package com.example.app3apis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.Coil
import coil.ImageLoader
import okhttp3.OkHttpClient
import com.example.app3apis.domain.model.ArtworkModel
import com.example.app3apis.ui.favoritos.FavoritosScreen
import com.example.app3apis.ui.favoritos.FavoritosViewModel
import com.example.app3apis.ui.screens.DetailScreen
import com.example.app3apis.ui.screens.HomeScreen
import com.example.app3apis.ui.theme.App3ApisTheme
import com.example.app3apis.ui.viewmodel.DashboardViewModel

enum class CurrentScreen {
    HOME, DETAIL, FAVORITOS
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val imageLoader = ImageLoader.Builder(this)
            .okHttpClient {
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .header("User-Agent", "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36")
                            .build()
                        chain.proceed(request)
                    }
                    .build()
            }
            .crossfade(true)
            .build()
        Coil.setImageLoader(imageLoader)

        setContent {
            App3ApisTheme {
                val viewModel: DashboardViewModel = viewModel()
                val favoritosViewModel: FavoritosViewModel = viewModel()

                var currentScreen by remember { mutableStateOf(CurrentScreen.HOME) }
                var selectedArtwork by remember { mutableStateOf<ArtworkModel?>(null) }
                val uiState by viewModel.uiState.collectAsState()

                when (currentScreen) {
                    CurrentScreen.HOME -> {
                        HomeScreen(
                            viewModel = viewModel,
                            onArtworkClick = { artwork ->
                                selectedArtwork = artwork
                                currentScreen = CurrentScreen.DETAIL
                            },
                            onVerFavoritos = {
                                currentScreen = CurrentScreen.FAVORITOS
                            }
                        )
                    }
                    CurrentScreen.DETAIL -> {
                        if (selectedArtwork != null) {
                            val isFav = selectedArtwork!!.id in uiState.favoritosIds
                            DetailScreen(
                                artwork = selectedArtwork!!,
                                isFavorito = isFav,
                                onBackClick = {
                                    currentScreen = CurrentScreen.HOME
                                },
                                onFavoritoClick = {
                                    viewModel.alternarFavorito(selectedArtwork!!)
                                }
                            )
                        }
                    }
                    CurrentScreen.FAVORITOS -> {
                        FavoritosScreen(
                            viewModel = favoritosViewModel,
                            onVolver = {
                                currentScreen = CurrentScreen.HOME
                            },
                            onArtworkClick = { artwork ->
                                selectedArtwork = artwork
                                currentScreen = CurrentScreen.DETAIL
                            }
                        )
                    }
                }
            }
        }
    }
}
