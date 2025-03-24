package com.example.doyourself.ui.pages

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import java.util.*

@Composable
fun CreateProcedureScreen(navController: NavController) {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    val steps = remember { mutableStateListOf(ProcedureStep()) }
    val scrollState = rememberScrollState()

    LaunchedEffect(steps.size) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text("Procedure Title", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            placeholder = { Text("e.g. How to change a shower head") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        steps.forEachIndexed { index, step ->
            StepEditor(
                step = step,
                stepNumber = index + 1,
                onAddBlock = { block -> step.blocks.add(block) },
                onMoveBlock = { from, to ->
                    if (from in step.blocks.indices && to in step.blocks.indices) {
                        val movedBlock = step.blocks.removeAt(from)
                        step.blocks.add(to, movedBlock)
                    }
                },
                onRemoveStep = { steps.remove(step) },
                onMoveStepUp = {
                    if (index > 0) {
                        val temp = steps[index - 1]
                        steps[index - 1] = steps[index]
                        steps[index] = temp
                    }
                },
                onMoveStepDown = {
                    if (index < steps.lastIndex) {
                        val temp = steps[index + 1]
                        steps[index + 1] = steps[index]
                        steps[index] = temp
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            )
        }

        Button(onClick = { steps.add(ProcedureStep()) }) {
            Text("Add Another Step")
        }
    }
}

@Composable
fun StepEditor(
    step: ProcedureStep,
    stepNumber: Int,
    onAddBlock: (ContentBlock) -> Unit,
    onMoveBlock: (from: Int, to: Int) -> Unit,
    onRemoveStep: () -> Unit,
    onMoveStepUp: () -> Unit,
    onMoveStepDown: () -> Unit,
    modifier: Modifier = Modifier
) {
    var collapsed by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text("Step $stepNumber", style = MaterialTheme.typography.titleMedium)
            Row {
                IconButton(onClick = onMoveStepUp) {
                    Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Move Step Up")
                }
                IconButton(onClick = onMoveStepDown) {
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Move Step Down")
                }
                IconButton(onClick = { collapsed = !collapsed }) {
                    Icon(
                        imageVector = if (collapsed) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                        contentDescription = if (collapsed) "Expand" else "Collapse"
                    )
                }
                IconButton(onClick = onRemoveStep) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Step")
                }
            }
        }

        if (!collapsed) {
            step.blocks.forEachIndexed { i, block ->
                when (block) {
                    is ContentBlock.Text -> {
                        var text by remember { mutableStateOf(block.text) }
                        OutlinedTextField(
                            value = text,
                            onValueChange = {
                                text = it
                                step.blocks[i] = ContentBlock.Text(it)
                            },
                            placeholder = { Text("Enter text...") },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        )
                    }
                    is ContentBlock.Image -> {
                        block.uri?.let { uri ->
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxWidth().height(200.dp).padding(vertical = 4.dp)
                            )
                        } ?: Text("[Empty Image Block]", color = Color.Gray, modifier = Modifier.padding(8.dp))
                    }
                    is ContentBlock.Video -> {
                        Text("[Video Placeholder]", color = Color.Gray, modifier = Modifier.padding(8.dp))
                    }
                }

                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    if (i > 0) {
                        TextButton(onClick = { onMoveBlock(i, i - 1) }) {
                            Text("Move Up")
                        }
                    }
                    if (i < step.blocks.lastIndex) {
                        TextButton(onClick = { onMoveBlock(i, i + 1) }) {
                            Text("Move Down")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onAddBlock(ContentBlock.Text("")) }) { Text("Add Text") }
                Button(onClick = { onAddBlock(ContentBlock.Image(null)) }) { Text("Add Image") }
                Button(onClick = { onAddBlock(ContentBlock.Video(null)) }) { Text("Add Video") }
            }
        }
    }
}

// Models

data class ProcedureStep(
    val id: String = UUID.randomUUID().toString(),
    val blocks: MutableList<ContentBlock> = mutableStateListOf()
)

sealed class ContentBlock {
    data class Text(var text: String) : ContentBlock()
    data class Image(var uri: Uri?) : ContentBlock()
    data class Video(var uri: Uri?) : ContentBlock()
}
