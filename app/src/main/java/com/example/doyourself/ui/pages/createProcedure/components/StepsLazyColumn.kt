package com.example.doyourself.ui.pages.createProcedure.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.doyourself.ui.pages.createProcedure.components.stepList.step.StepEditor
import com.example.doyourself.ui.pages.createProcedure.shared.ProcedureStep
import com.example.doyourself.ui.pages.createProcedure.logic.*

@Composable
fun StepsLazyColumn(
    steps: SnapshotStateList<ProcedureStep>,
    onRemoveStep: (ProcedureStep) -> Unit = {},
    onMoveStepUp: (Int) -> Unit = {},
    onMoveStepDown: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 24.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        itemsIndexed(
            items = steps,
            key = { _, step -> step.id }         // keep state when list mutates
        ) { index, step ->

            StepEditor(
                step = step,
                stepNumber = index + 1,
                onAddBlock = { addBlock(step, it) },
                onRemoveBlock = { deleteBlock(step, it) },
                onRemoveStep = { onRemoveStep(step) },
                onMoveStepUp = { onMoveStepUp(index) },
                onMoveStepDown = { onMoveStepDown(index) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
