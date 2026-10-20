package com.example.app3apis.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.app3apis.domain.model.ArtworkModel
import com.example.app3apis.ui.viewmodel.DashboardUiState
import com.example.app3apis.ui.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: DashboardViewModel,
    onArtworkClick: (ArtworkModel) -> Unit,
    onVerFavoritos: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Museums & Weather",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    // Favorites button (Room DB)
                    IconButton(onClick = onVerFavoritos) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favoritos",
                            tint = Color(0xFFE53935)
                        )
                    }
                    IconButton(onClick = { viewModel.fetchAllData() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF121212))
            )
        },
        containerColor = Color(0xFF121212)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                WeatherCard(uiState)
            }

            // ART INSTITUTE OF CHICAGO SECTION
            item {
                MuseumSection(
                    museumTitle = "Art Institute of Chicago",
                    apiName = "API 2: Artic",
                    artworks = uiState.articArtworks,
                    favoritosIds = uiState.favoritosIds,
                    onArtworkClick = onArtworkClick,
                    onFavoritoClick = { viewModel.alternarFavorito(it) }
                )
            }

            // THE MET MUSEUM SECTION
            item {
                MuseumSection(
                    museumTitle = "The Metropolitan Museum of Art",
                    apiName = "API 3: The Met",
                    artworks = uiState.metArtworks,
                    favoritosIds = uiState.favoritosIds,
                    onArtworkClick = onArtworkClick,
                    onFavoritoClick = { viewModel.alternarFavorito(it) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun WeatherCard(uiState: DashboardUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "API 1: WEATHER (OPEN-METEO)",
                color = Color(0xFFBB86FC),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    val temp = uiState.weather.temperature
                    Text(
                        text = "$temp°C",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = uiState.weather.condition,
                        color = Color.LightGray,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "📍 ${uiState.weather.location}",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Wind speed",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "${uiState.weather.windspeed} km/h",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun MuseumSection(
    museumTitle: String,
    apiName: String,
    artworks: List<ArtworkModel>,
    favoritosIds: Set<Int>,
    onArtworkClick: (ArtworkModel) -> Unit,
    onFavoritoClick: (ArtworkModel) -> Unit
) {
    if (artworks.isEmpty()) return

    val featured = artworks.first()
    val others = artworks.drop(1).take(5)
    val isFeaturedFav = featured.id in favoritosIds

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = apiName.uppercase(),
                    color = Color(0xFFBB86FC),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = museumTitle,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Featured Artwork Card with Favorite Heart Button
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onArtworkClick(featured) },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
        ) {
            Box {
                Column {
                    if (featured.imageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = featured.imageUrl,
                            contentDescription = featured.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "★ OBRA DESTACADA",
                            color = Color(0xFFFFD700),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = featured.title,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Artist: ${featured.artist}",
                            color = Color.LightGray,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Date: ${featured.date}",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                }

                // Favorite Heart IconButton
                IconButton(
                    onClick = { onFavoritoClick(featured) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Color(0x99000000), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = if (isFeaturedFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (isFeaturedFav) Color(0xFFE53935) else Color.White
                    )
                }
            }
        }

        // Horizontal Carousel for more items
        if (others.isNotEmpty()) {
            Text(
                text = "Otras obras destacadas",
                color = Color.LightGray,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                others.forEach { item ->
                    SmallArtworkCard(
                        artwork = item,
                        isFav = item.id in favoritosIds,
                        onClick = { onArtworkClick(item) },
                        onFavClick = { onFavoritoClick(item) }
                    )
                }
            }
        }
    }
}

@Composable
fun SmallArtworkCard(
    artwork: ArtworkModel,
    isFav: Boolean,
    onClick: () -> Unit,
    onFavClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Box {
            Column {
                if (artwork.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = artwork.imageUrl,
                        contentDescription = artwork.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = artwork.title,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = artwork.artist,
                        color = Color.Gray,
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }
            }

            IconButton(
                onClick = onFavClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(32.dp)
                    .padding(4.dp)
                    .background(Color(0x99000000), shape = CircleShape)
            ) {
                Icon(
                    imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (isFav) Color(0xFFE53935) else Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
