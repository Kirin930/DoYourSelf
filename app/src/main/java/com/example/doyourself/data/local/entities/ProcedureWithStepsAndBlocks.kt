package com.example.doyourself.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ProcedureWithStepsAndBlocks(
    @Embedded val procedure: ProcedureDraftEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "procedureId",
        entity = StepEntity::class
    )
    val steps: List<StepWithBlocks>
)

data class StepWithBlocks(
    @Embedded val step: StepEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "stepId",
        entity = BlockEntity::class
    )
    val blocks: List<BlockEntity>
)
