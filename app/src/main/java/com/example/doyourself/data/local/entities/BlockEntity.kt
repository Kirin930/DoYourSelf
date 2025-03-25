package com.example.doyourself.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "blocks",
    foreignKeys = [ForeignKey(
        entity = StepEntity::class,
        parentColumns = ["id"],
        childColumns = ["stepId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class BlockEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val stepId: String,
    val index: Int,
    val type: String, // "text", "image", "video"
    val content: String // text or URI
)
