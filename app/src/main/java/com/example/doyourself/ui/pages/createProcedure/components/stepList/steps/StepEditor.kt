package com.example.doyourself.ui.pages.createProcedure.components.stepList.steps

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.doyourself.ui.pages.createProcedure.components.stepList.steps.blocks.ImageBlock
import com.example.doyourself.ui.pages.createProcedure.components.stepList.steps.blocks.TextBlock
import com.example.doyourself.ui.pages.createProcedure.components.stepList.steps.blocks.TitleBlock
import com.example.doyourself.ui.pages.createProcedure.components.stepList.steps.blocks.VideoBlock
import com.example.doyourself.ui.pages.createProcedure.shared.ContentBlock
import com.example.doyourself.ui.pages.createProcedure.shared.ProcedureStep
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import com.example.doyourself.ui.pages.createProcedure.components.stepList.steps.DraggableBlockItem
import org.burnoutcrew.reorderable.ReorderableItem

@Composable
fun StepEditor(
    step: ProcedureStep,
    stepNumber: Int,
    onAddBlock: (ContentBlock) -> Unit,
    onRemoveBlock: (index: Int) -> Unit,
    onDuplicateBlock: (index: Int) -> Unit,
    onMoveBlock: (from: Int, to: Int) -> Unit,
    //onRemoveStep: () -> Unit,
    onMoveStepUp: () -> Unit,    // Not used now; steps are reordered horizontally.
    onMoveStepDown: () -> Unit,  // Not used now.
    modifier: Modifier = Modifier
) {
    // Reorderable state for the blocks inside this step.
    val data = remember { mutableStateOf(step.blocks)}
    val reorderState = rememberReorderableLazyListState(
        onMove = { from, to -> onMoveBlock(from.index, to.index) }
    )

    Column(modifier = modifier.fillMaxSize()) {
        // Step header with a drag handle for future expansion if needed.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Step $stepNumber", style = MaterialTheme.typography.titleMedium)
            // Optionally, you can show small buttons for "Move" if needed.
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Blocks editor – a vertically scrolling list.
        LazyColumn(
            state = reorderState.listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .reorderable(reorderState),
                //.detectReorderAfterLongPress(reorderState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(
                items = step.blocks,
                key = { _, block ->
                    when (block) {
                        is ContentBlock.Title -> block.id
                        is ContentBlock.Text -> block.id
                        is ContentBlock.Image -> block.id
                        is ContentBlock.Video -> block.id
                    }
                }
            ) { index, block ->
                ReorderableItem(state = reorderState, key = block.id) { isDragging ->
                    val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp)

                    DraggableBlockItem(
                        key = block.id,
                        elevation = elevation,
                        reorderState = reorderState,
                        onDelete = { onRemoveBlock(index) },
                        onDuplicate = { onDuplicateBlock(index) }
                    ) {
                        when (block) {
                            is ContentBlock.Title -> TitleBlock(
                                block = block,
                                onTitleChanged = { newText ->
                                    val idx = step.blocks.indexOf(block)
                                    if (idx != -1) {
                                        step.blocks[idx] = block.copy(text = newText)
                                    }
                                }
                            )

                            is ContentBlock.Text -> TextBlock(
                                block = block,
                                onTextChanged = { newText ->
                                    // Replace the current block with an updated copy.
                                    val idx = step.blocks.indexOf(block)
                                    if (idx != -1) {
                                        step.blocks[idx] = block.copy(text = newText)
                                    }
                                }
                            )

                            is ContentBlock.Image -> ImageBlock(
                                block = block,
                                onImagePicked = { newUri ->
                                    block.uri = newUri
                                }
                            )

                            is ContentBlock.Video -> VideoBlock(
                                block = block,
                                onVideoPicked = { newUri ->
                                    block.uri = newUri
                                }
                            )
                        }
                    }
                }
            }
        }
        /*Spacer(modifier = Modifier.height(12.dp))
        // (For debugging, you might want a small inline add block button here, but the FAB handles adding blocks.)
        Button(onClick = { onAddBlock(ContentBlock.Text("", java.util.UUID.randomUUID().toString())) }, modifier = Modifier.fillMaxWidth()) {
            Text("Add Block")
        }*/
    }
}