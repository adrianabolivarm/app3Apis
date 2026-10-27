package com.example.app3apis.data.remote

import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class SQLiteCloudApiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .build()

    fun ejecutarConsulta(sqlQuery: String): Boolean {
        return try {
            val url = "https://${SQLiteCloudConfig.HOST}:${SQLiteCloudConfig.PORT}/" +
                    "?apikey=${SQLiteCloudConfig.API_KEY}&database=${SQLiteCloudConfig.DATABASE}"

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val jsonBody = "{\"query\": \"${sqlQuery.replace("\"", "\\\"")}\"}"
            val body = jsonBody.toRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

            val response = client.newCall(request).execute()
            val success = response.isSuccessful
            Log.d("SQLiteCloud", "SQL ejecutado en SQLite Cloud: $sqlQuery -> Éxito: $success")
            response.close()
            success
        } catch (e: Exception) {
            Log.e("SQLiteCloud", "Error en SQLite Cloud: ${e.localizedMessage}")
            false
        }
    }
}
