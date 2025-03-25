package com.example.doyourself.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "steps",
    foreignKeys = [ForeignKey(
        entity = ProcedureDraftEntity::class,
        parentColumns = ["id"],
        childColumns = ["procedureId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class StepEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val procedureId: String,
    val index: Int
)
