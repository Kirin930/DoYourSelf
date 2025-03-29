package com.example.doyourself.ui.pages.createProcedure.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.doyourself.ui.pages.createProcedure.shared.ContentBlock
import com.example.doyourself.ui.pages.createProcedure.shared.ProcedureStep
import com.example.doyourself.ui.pages.createProcedure.logic.*

@Composable
fun StepEditor(
    step: ProcedureStep,
    stepNumber: Int,
    onAddBlock: (ContentBlock) -> Unit,
    onRemoveBlock: (index: Int) -> Unit,
    onRemoveStep: () -> Unit,
    onMoveStepUp: () -> Unit,
    onMoveStepDown: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text("Step $stepNumber", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        step.blocks.forEachIndexed { i, block ->
            when (block) {
                is ContentBlock.Text -> TextBlock(block) {
                    step.blocks[i] = ContentBlock.Text(it)
                }
                is ContentBlock.Image -> ImageBlock(block) {
                    step.blocks[i] = ContentBlock.Image(it)
                }
                is ContentBlock.Video -> VideoBlock(block) {
                    step.blocks[i] = ContentBlock.Video(it)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (i > 0) Button(onClick = { moveBlock(step, i, i - 1) }) { Text("↑") }
                if (i < step.blocks.lastIndex) Button(onClick = { moveBlock(step, i, i + 1) }) { Text("↓") }
                Button(onClick = { onRemoveBlock(i) }) { Text("Remove") }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onAddBlock(ContentBlock.Text("")) }) { Text("Add Text") }
            Button(onClick = { onAddBlock(ContentBlock.Image(null)) }) { Text("Add Image") }
            Button(onClick = { onAddBlock(ContentBlock.Video(null)) }) { Text("Add Video") }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onRemoveStep) { Text("Remove Step") }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onMoveStepUp) { Text("↑ Step") }
                Button(onClick = onMoveStepDown) { Text("↓ Step") }
            }
        }
    }
}
