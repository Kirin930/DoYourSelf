package com.example.doyourself.data.local.db

import androidx.room.*
import com.example.doyourself.data.local.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProcedureDao {

    // Insert Procedure, Steps, and Blocks in one go
    @Transaction
    suspend fun insertFullProcedure(
        procedure: ProcedureDraftEntity,
        steps: List<StepEntity>,
        blocks: List<BlockEntity>
    ) {
        insertProcedure(procedure)
        insertSteps(steps)
        insertBlocks(blocks)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProcedure(procedure: ProcedureDraftEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSteps(steps: List<StepEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlocks(blocks: List<BlockEntity>)

    @Query("DELETE FROM steps WHERE id = :stepId")
    suspend fun deleteStep(stepId: String)

    @Query("DELETE FROM blocks WHERE stepId = :stepId AND `index` = :blockIndex")
    suspend fun deleteBlockByStepAndIndex(stepId: String, blockIndex: Int)

    @Transaction
    @Query("SELECT * FROM procedures WHERE id = :procedureId")
    suspend fun getFullProcedure(procedureId: String): ProcedureWithStepsAndBlocks?

    @Transaction
    suspend fun getFullProcedureAll(procedureId: String): ProcedureWithStepsAndBlocks? {
        val draft = getFullProcedure(procedureId)
        if (draft != null) return draft

        val liked = getLikedProcedure(procedureId) ?: return null

        val steps = liked.steps.map { likedStep ->
            StepWithBlocks(
                step = StepEntity(
                    id = likedStep.step.id,
                    procedureId = likedStep.step.procedureId,
                    index = likedStep.step.index
                ),
                blocks = likedStep.blocks.map { likedBlock ->
                    BlockEntity(
                        id = likedBlock.id,
                        stepId = likedBlock.stepId,
                        index = likedBlock.index,
                        type = likedBlock.type,
                        content = likedBlock.content
                    )
                }
            )
        }

        val proc = ProcedureDraftEntity(
            id = liked.procedure.id,
            title = liked.procedure.title,
            createdAt = liked.procedure.createdAt,
            isPublished = true,
            backGroundColor = "#FFFFFF"
        )

        return ProcedureWithStepsAndBlocks(
            procedure = proc,
            steps = steps
        )
    }

    @Transaction
    @Query("SELECT * FROM procedures ORDER BY createdAt DESC")
    fun getAllDrafts(): Flow<List<ProcedureWithStepsAndBlocks>>

    @Query("DELETE FROM procedures WHERE id = :procedureId")
    suspend fun deleteProcedure(procedureId: String)

    @Query("DELETE FROM steps WHERE procedureId = :procedureId")
    suspend fun deleteStepsByProcedureId(procedureId: String)

    @Query("DELETE FROM blocks WHERE stepId IN (SELECT id FROM steps WHERE procedureId = :procedureId)")
    suspend fun deleteBlocksByProcedureId(procedureId: String)

    @Query("UPDATE procedures SET isPublished = :isPublished WHERE id = :procedureId")
    suspend fun updateProcedurePublishedStatus(procedureId: String, isPublished: Boolean)

    @Query("SELECT * FROM procedures WHERE isPublished = 1")
    suspend fun getPublishedProcedures(): List<ProcedureWithStepsAndBlocks>

    // Liked procedures operations
    @Transaction
    suspend fun insertFullLikedProcedure(
        procedure: LikedProcedureEntity,
        steps: List<LikedStepEntity>,
        blocks: List<LikedBlockEntity>
    ) {
        insertLikedProcedure(procedure)
        insertLikedSteps(steps)
        insertLikedBlocks(blocks)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLikedProcedure(procedure: LikedProcedureEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLikedSteps(steps: List<LikedStepEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLikedBlocks(blocks: List<LikedBlockEntity>)

    @Transaction
    @Query("SELECT * FROM liked_procedures WHERE id = :procedureId")
    suspend fun getLikedProcedure(procedureId: String): LikedProcedureWithStepsAndBlocks?

    @Transaction
    @Query("SELECT * FROM liked_procedures ORDER BY createdAt DESC")
    fun getLikedProcedures(): Flow<List<LikedProcedureWithStepsAndBlocks>>

    @Query("DELETE FROM liked_procedures WHERE id = :procedureId")
    suspend fun deleteLikedProcedure(procedureId: String)

    @Query("UPDATE procedures SET backGroundColor = :backgroundColor WHERE id = :procedureId")
    suspend fun updateProcedureBackgroundColor(procedureId: String, backgroundColor: String)

    // Progress operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(progress: ProcedureProgressEntity)

    @Query("SELECT * FROM procedure_progress WHERE procedureId = :procedureId")
    suspend fun getProgress(procedureId: String): ProcedureProgressEntity?
}
