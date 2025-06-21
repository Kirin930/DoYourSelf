package com.example.doyourself.ui.pages.createProcedure.components.stepList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.doyourself.ui.pages.createProcedure.components.stepList.steps.StepEditor
import com.example.doyourself.ui.pages.createProcedure.shared.ContentBlock
import com.example.doyourself.ui.pages.createProcedure.shared.ProcedureStep
import com.example.doyourself.ui.pages.createProcedure.shared.ProcedureStepList

@Composable
fun StepsScreen(
    steps: List<ProcedureStep>,
    currentStepIndex: Int,
    onStepSelected: (Int) -> Unit,
    onAddBlock: (step: ProcedureStep, block: ContentBlock) -> Unit,
    onRemoveBlock: (step: ProcedureStep, index: Int) -> Unit,
    onDuplicateBlock: (step: ProcedureStep, index: Int) -> Unit,
    onMoveBlock: (step: ProcedureStep, from: Int, to: Int) -> Unit,
    onRemoveStep: (step: ProcedureStep) -> Unit,
    onDuplicateStep: (index: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {

        StepNavigationRow(
            steps = steps,
            currentStepIndex = currentStepIndex,
            onRemoveStep = { onRemoveStep(steps[currentStepIndex]) },
            onDuplicateStep = { onDuplicateStep(currentStepIndex) },
            onStepSelected = onStepSelected
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (steps.isNotEmpty()) {
            StepEditor(
                step = steps[currentStepIndex],
                stepNumber = currentStepIndex + 1,
                onAddBlock = { block -> onAddBlock(steps[currentStepIndex], block) },
                onRemoveBlock = { index -> onRemoveBlock(steps[currentStepIndex], index) },
                onDuplicateBlock = { index -> onDuplicateBlock(steps[currentStepIndex], index) },
                onMoveBlock = { from, to -> onMoveBlock(steps[currentStepIndex], from, to) },
                //onRemoveStep = { onRemoveStep(steps[currentStepIndex]) },
                onMoveStepUp = {},  // No-op, steps are not vertically reordered
                onMoveStepDown = {},
                modifier = Modifier.weight(1f)
            )
        }
    }
}