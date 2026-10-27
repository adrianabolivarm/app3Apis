package com.example.app3apis.data.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class SqlBody(
    val id: Int? = null,
    val title: String? = null,
    val artist: String? = null,
    val imageUrl: String? = null,
    val date: String? = null,
    val museum: String? = null,
    val agregadoEn: Long? = null
)

interface SQLiteCloudApi {

    @Headers(
        "Authorization: Bearer M98koUFH29tcyeJ0cbgmunMESruaV3GGmLrt3t7hHwE",
        "Content-Type: application/json"
    )
    @POST("v2/functions/agregar_favoritos")
    suspend fun agregarFavorito(@Body body: SqlBody): Response<ResponseBody>

    @Headers(
        "Authorization: Bearer M98koUFH29tcyeJ0cbgmunMESruaV3GGmLrt3t7hHwE",
        "Content-Type: application/json"
    )
    @POST("v2/functions/eliminar_favoritos")
    suspend fun eliminarFavorito(@Body body: SqlBody): Response<ResponseBody>

    @Headers(
        "Authorization: Bearer M98koUFH29tcyeJ0cbgmunMESruaV3GGmLrt3t7hHwE",
        "Content-Type: application/json"
    )
    @POST("v2/functions/obtener_favoritos")
    suspend fun obtenerFavoritos(@Body body: Map<String, String> = emptyMap()): Response<ResponseBody>
}
