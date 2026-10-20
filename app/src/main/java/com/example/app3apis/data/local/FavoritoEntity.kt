package com.example.app3apis.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritos")
data class FavoritoEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val artist: String,
    val imageUrl: String,
    val date: String,
    val museum: String,
    val agregadoEn: Long = System.currentTimeMillis()
)
