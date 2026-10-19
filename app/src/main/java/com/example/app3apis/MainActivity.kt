package com.example.app3apis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app3apis.domain.model.ArtworkModel
import com.example.app3apis.ui.screens.DetailScreen
import com.example.app3apis.ui.screens.HomeScreen
import com.example.app3apis.ui.theme.App3ApisTheme
import com.example.app3apis.ui.viewmodel.DashboardViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App3ApisTheme {
                val viewModel: DashboardViewModel = viewModel()
                var selectedArtwork by remember { mutableStateOf<ArtworkModel?>(null) }

                if (selectedArtwork == null) {
                    HomeScreen(
                        viewModel = viewModel,
                        onArtworkClick = { artwork ->
                            selectedArtwork = artwork
                        }
                    )
                } else {
                    DetailScreen(
                        artwork = selectedArtwork!!,
                        onBackClick = {
                            selectedArtwork = null
                        }
                    )
                }
            }
        }
    }
}
