package com.example.app3apis.data.model

import com.google.gson.annotations.SerializedName

data class ArticResponseDto(
    @SerializedName("data") val data: List<ArticArtworkDto>?
)

data class ArticArtworkDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("artist_display") val artistDisplay: String?,
    @SerializedName("image_id") val imageId: String?,
    @SerializedName("date_display") val dateDisplay: String?
)
