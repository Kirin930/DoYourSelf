package com.example.doyourself.ui.pages.createProcedure.logic

import androidx.navigation.NavController
import com.example.doyourself.data.local.db.ProcedureDao
import com.example.doyourself.data.local.entities.BlockEntity
import com.example.doyourself.data.local.entities.StepEntity
import com.example.doyourself.data.local.entities.ProcedureDraftEntity
import com.example.doyourself.ui.pages.createProcedure.shared.ContentBlock
import com.example.doyourself.ui.pages.createProcedure.shared.ProcedureStep
import java.sql.Date
import java.sql.Time

suspend fun saveProcedureToRoom(
    procedureId: String,
    title: String,
    steps: List<ProcedureStep>,
    backgroundColor: String,
    procedureDao: ProcedureDao,
    navController: NavController
) {
    // Delete and replace (cascade handles cleanup)
    procedureDao.deleteProcedure(procedureId)

    procedureDao.insertProcedure(
        ProcedureDraftEntity(
            id = procedureId,
            title = title,
            createdAt = Date(System.currentTimeMillis()).toString().toLong(),
            backGroundColor = backgroundColor
        )
    )

    procedureDao.insertSteps(
        steps.mapIndexed { i, s ->
            StepEntity(id = s.id, procedureId = procedureId, index = i)
        }
    )

    steps.forEach { step ->
        procedureDao.insertBlocks(
            step.blocks.mapIndexed { i, b ->
                BlockEntity(
                    stepId = step.id,
                    index = i,
                    type = when (b) {
                        is ContentBlock.Text -> "text"
                        is ContentBlock.Title -> "title"
                        is ContentBlock.Image -> "image"
                        is ContentBlock.Video -> "video"
                    },
                    content = when (b) {
                        is ContentBlock.Text -> b.text
                        is ContentBlock.Title -> b.text
                        is ContentBlock.Image -> b.uri?.toString() ?: ""
                        is ContentBlock.Video -> b.uri?.toString() ?: ""
                    }
                )
            }
        )
    }


}