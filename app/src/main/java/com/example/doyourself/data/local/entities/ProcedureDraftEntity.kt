package com.example.doyourself.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "procedures")
data class ProcedureDraftEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isPublished: Boolean = false,
    //val firestoreId: String? = null,        // doc id in “procedures” collection
    //val storagePaths: List<String> = emptyList()  // gs://… or relative paths
)