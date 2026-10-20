package com.example.app3apis.data.mapper

import com.example.app3apis.data.local.FavoritoEntity
import com.example.app3apis.domain.model.ArtworkModel

fun FavoritoEntity.aArtworkModel(): ArtworkModel =
    ArtworkModel(
        id = id,
        title = title,
        artist = artist,
        imageUrl = imageUrl,
        date = date,
        museum = museum
    )

fun ArtworkModel.aFavoritoEntity(): FavoritoEntity =
    FavoritoEntity(
        id = id,
        title = title,
        artist = artist,
        imageUrl = imageUrl,
        date = date,
        museum = museum
    )
