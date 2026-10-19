package com.example.app3apis.data.remote

import com.example.app3apis.data.model.ArticResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticApiService {
    @GET("api/v1/artworks")
    suspend fun getArtworks(
        @Query("limit") limit: Int = 10
    ): ArticResponseDto
}
