package com.example.app3apis.data.remote

import com.example.app3apis.data.model.MetObjectDto
import com.example.app3apis.data.model.MetSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MetApiService {
    @GET("public/collection/v1/search")
    suspend fun searchObjects(
        @Query("q") query: String = "painting"
    ): MetSearchResponseDto

    @GET("public/collection/v1/objects/{objectID}")
    suspend fun getObjectDetails(
        @Path("objectID") objectID: Int
    ): MetObjectDto
}
