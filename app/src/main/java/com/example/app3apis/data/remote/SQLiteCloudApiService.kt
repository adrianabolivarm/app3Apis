package com.example.app3apis.data.remote

import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class SQLiteCloudApiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    fun ejecutarConsulta(sqlQuery: String): Boolean {
        return try {
            val url = "https://${SQLiteCloudConfig.HOST}:${SQLiteCloudConfig.PORT}/" +
                    "?apikey=${SQLiteCloudConfig.API_KEY}&database=${SQLiteCloudConfig.DATABASE}"

            val mediaType = "text/plain; charset=utf-8".toMediaType()
            val body = sqlQuery.toRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .header("Authorization", "Bearer ${SQLiteCloudConfig.API_KEY}")
                .header("apikey", SQLiteCloudConfig.API_KEY)
                .post(body)
                .build()

            val response = client.newCall(request).execute()
            val success = response.isSuccessful
            val responseString = response.body?.string() ?: ""
            Log.d("SQLiteCloud", "SQL ejecutado -> Éxito: $success | Respuesta: $responseString")
            response.close()
            success
        } catch (e: Exception) {
            Log.e("SQLiteCloud", "Error HTTP: ${e.localizedMessage}")
            false
        }
    }
}
