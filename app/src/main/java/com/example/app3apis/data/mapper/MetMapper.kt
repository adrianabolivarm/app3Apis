package com.example.app3apis.data.mapper

import com.example.app3apis.data.model.MetObjectDto
import com.example.app3apis.domain.model.ArtworkModel

class MetMapper {
    fun mapToDomain(dto: MetObjectDto): ArtworkModel {
        return ArtworkModel(
            id = dto.objectID,
            title = dto.title ?: "Untitled",
            artist = dto.artistDisplayName.takeIf { !it.isNullOrBlank() } ?: "The Met Collection",
            imageUrl = dto.primaryImageSmall ?: "",
            date = dto.period ?: dto.culture ?: "Classical",
            museum = "The Metropolitan Museum of Art"
        )
    }
}
