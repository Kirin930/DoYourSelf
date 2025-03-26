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
    @Query("SELECT * FROM procedures ORDER BY createdAt DESC")
    fun getAllDrafts(): Flow<List<ProcedureWithStepsAndBlocks>>

    @Query("DELETE FROM procedures WHERE id = :procedureId")
    suspend fun deleteProcedure(procedureId: String)

    @Query("DELETE FROM steps WHERE procedureId = :procedureId")
    suspend fun deleteStepsByProcedureId(procedureId: String)

    @Query("DELETE FROM blocks WHERE stepId IN (SELECT id FROM steps WHERE procedureId = :procedureId)")
    suspend fun deleteBlocksByProcedureId(procedureId: String)
}
