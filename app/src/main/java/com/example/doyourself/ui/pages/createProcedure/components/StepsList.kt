package com.example.doyourself.ui.pages.createProcedure.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.doyourself.ui.pages.createProcedure.shared.ProcedureStep
import com.example.doyourself.ui.pages.createProcedure.logic.*
import com.example.doyourself.ui.pages.createProcedure.shared.ContentBlock

@Composable
fun StepsList(
    steps: SnapshotStateList<ProcedureStep>,
    onRemoveStep: (ProcedureStep) -> Unit,
    onMoveStepUp: (Int) -> Unit,
    onMoveStepDown: (Int) -> Unit,
) {
    steps.forEachIndexed { index, step ->
        StepEditor(
            step = step,
            stepNumber = index + 1,
            onAddBlock = { addBlock(step, it) },
            onRemoveBlock = { deleteBlock(step, it) },
            onRemoveStep = { onRemoveStep(step) },
            onMoveStepUp = { onMoveStepUp(index) },
            onMoveStepDown = { onMoveStepDown(index) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        )
    }
}
