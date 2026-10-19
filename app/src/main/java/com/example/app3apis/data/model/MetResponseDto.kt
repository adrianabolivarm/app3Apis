package com.example.app3apis.data.model

import com.google.gson.annotations.SerializedName

data class MetSearchResponseDto(
    @SerializedName("total") val total: Int,
    @SerializedName("objectIDs") val objectIDs: List<Int>?
)

data class MetObjectDto(
    @SerializedName("objectID") val objectID: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("artistDisplayName") val artistDisplayName: String?,
    @SerializedName("primaryImageSmall") val primaryImageSmall: String?,
    @SerializedName("period") val period: String?,
    @SerializedName("culture") val culture: String?
)
