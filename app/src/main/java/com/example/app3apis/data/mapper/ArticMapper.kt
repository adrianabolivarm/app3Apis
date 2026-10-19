package com.example.app3apis.data.mapper

import com.example.app3apis.data.model.ArticArtworkDto
import com.example.app3apis.domain.model.ArtworkModel

class ArticMapper {
    fun mapToDomain(dto: ArticArtworkDto): ArtworkModel {
        val imgId = dto.imageId ?: ""
        val imageUrl = if (imgId.isNotEmpty()) {
            "https://www.artic.edu/iiif/2/$imgId/full/843,/0/default.jpg"
        } else ""
        return ArtworkModel(
            id = dto.id,
            title = dto.title ?: "Untitled",
            artist = dto.artistDisplay ?: "Unknown Artist",
            imageUrl = imageUrl,
            date = dto.dateDisplay ?: "N/A",
            museum = "Art Institute of Chicago"
        )
    }
}
