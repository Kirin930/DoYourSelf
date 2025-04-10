package com.example.doyourself.ui.pages.createProcedure.components.stepList.steps

import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun StepEditor(
    step: ProcedureStep,
    stepNumber: Int,
    onAddBlock: (ContentBlock) -> Unit,
    onRemoveBlock: (index: Int) -> Unit,
    onDuplicateBlock: (index: Int) -> Unit,
    onMoveBlock: (from: Int, to: Int) -> Unit,
    onRemoveStep: () -> Unit,
    onMoveStepUp: () -> Unit,    // Not used now; steps are reordered horizontally.
    onMoveStepDown: () -> Unit,  // Not used now.
    modifier: Modifier = Modifier
) {
    // Reorderable state for the blocks inside this step.
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
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .reorderable(reorderState)
                .detectReorderAfterLongPress(reorderState),
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
                DraggableBlockItem(
                    key = block.id,
                    reorderState = reorderState,
                    onDelete = { onRemoveBlock(index) },
                    onDuplicate = { onDuplicateBlock(index) }
                ) {
                    when (block) {
                        is ContentBlock.Title -> TitleBlock(block = block, onTitleChanged = { newText ->
                            block.text = newText
                        })
                        is ContentBlock.Text -> TextBlock(block = block, onTextChanged = { newText ->
                            block.text = newText
                        })
                        is ContentBlock.Image -> ImageBlock(block = block, onImagePicked = { newUri ->
                            block.uri = newUri
                        })
                        is ContentBlock.Video -> VideoBlock(block = block, onVideoPicked = { newUri ->
                            block.uri = newUri
                        })
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        // (For debugging, you might want a small inline add block button here, but the FAB handles adding blocks.)
        Button(onClick = { onAddBlock(ContentBlock.Text("", java.util.UUID.randomUUID().toString())) }, modifier = Modifier.fillMaxWidth()) {
            Text("Add Block")
        }
    }
}


/*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.doyourself.ui.pages.createProcedure.components.stepList.steps.blocks.ImageBlock
import com.example.doyourself.ui.pages.createProcedure.components.stepList.steps.blocks.TextBlock
import com.example.doyourself.ui.pages.createProcedure.components.stepList.steps.blocks.TitleBlock
import com.example.doyourself.ui.pages.createProcedure.components.stepList.steps.blocks.VideoBlock
import com.example.doyourself.ui.pages.createProcedure.shared.ContentBlock
import com.example.doyourself.ui.pages.createProcedure.shared.ProcedureStep
import com.example.doyourself.ui.pages.createProcedure.logic.*
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun StepEditor(
    step: ProcedureStep,
    stepNumber: Int,
    onAddBlock: (ContentBlock) -> Unit,
    onRemoveBlock: (index: Int) -> Unit,
    onRemoveStep: () -> Unit,
    onMoveStepUp: () -> Unit,
    onMoveStepDown: () -> Unit,
    onDuplicateBlock: (index: Int) -> Unit,
    onMoveBlock: (fromIndex: Int, toIndex: Int) -> Unit,
    modifier: Modifier = Modifier
) {

    // Create the reorderable state using burnoutcrew's API.
    val reorderState = rememberReorderableLazyListState(
        onMove = { from, to ->
            onMoveBlock(from.index, to.index)
        }
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Text("Step $stepNumber", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Step $stepNumber", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onMoveStepUp) { Text("↑ Step") }
                Button(onClick = onMoveStepDown) { Text("↓ Step") }
                Button(onClick = onRemoveStep) { Text("Remove Step") }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
          modifier = Modifier
              .fillMaxWidth()
              .reorderable(reorderState)
              .detectReorderAfterLongPress(reorderState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(
                items = step.blocks,
                key = { _, block ->
                    when (block){
                        is ContentBlock.Title -> block.id
                        is ContentBlock.Text -> block.id
                        is ContentBlock.Image -> block.id
                        is ContentBlock.Video -> block.id
                    }
                }
            ) { index, block ->
                DraggableBlockItem(
                    key = block.id,
                    reorderState = reorderState,
                    onDelete = { onRemoveBlock(index) },
                    onDuplicate = { onDuplicateBlock(index) }
                ) {
                    when (block) {
                        is ContentBlock.Title -> {
                            TitleBlock(
                                block = block,
                                onTitleChanged = { newText ->
                                    block.text = newText
                                }
                            )
                        }
                        is ContentBlock.Text -> {
                            TextBlock(
                                block = block,
                                onTextChanged = { newText ->
                                    block.text = newText
                                }
                            )
                        }
                        is ContentBlock.Image -> {
                            ImageBlock(
                                block = block,
                                onImageChanged = { newImage ->
                                    block.image = newImage
                                }
                            )
                        }
                        is ContentBlock.Video -> {
                            VideoBlock(
                                block = block,
                                onVideoChanged = { newVideo ->
                                    block.video = newVideo
                                }
                            )
                        }
                    }
                }
            }
        }
//        step.blocks.forEachIndexed { i, block ->
//            when (block) {
//                is ContentBlock.Text -> TextBlock(block) {
//                    step.blocks[i] = ContentBlock.Text(it)
//                }
//                is ContentBlock.Title -> TitleBlock(block) {
//                    step.blocks[i] = ContentBlock.Title(it)
//                }
//                is ContentBlock.Image -> ImageBlock(block) {
//                    step.blocks[i] = ContentBlock.Image(it)
//                }
//                is ContentBlock.Video -> VideoBlock(block) {
//                    step.blocks[i] = ContentBlock.Video(it)
//                }
//            }
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                if (i > 0) Button(onClick = { moveBlock(step, i, i - 1) }) { Text("↑") }
//                if (i < step.blocks.lastIndex) Button(onClick = { moveBlock(step, i, i + 1) }) { Text("↓") }
//                Button(onClick = { onRemoveBlock(i) }) { Text("Remove") }
//            }
//            Spacer(modifier = Modifier.height(12.dp))
//        }
//
//        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//            Button(onClick = { onAddBlock(ContentBlock.Text("")) }) { Text("Add Text") }
//            Button(onClick = { onAddBlock(ContentBlock.Image(null)) }) { Text("Add Image") }
//            Button(onClick = { onAddBlock(ContentBlock.Video(null)) }) { Text("Add Video") }
//        }

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
*/