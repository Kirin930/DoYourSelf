package com.example.doyourself.ui.pages

import android.net.Uri
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.doyourself.data.local.db.ProcedureDao
import com.example.doyourself.data.local.entities.BlockEntity
import com.example.doyourself.data.local.entities.ProcedureDraftEntity
import com.example.doyourself.data.local.entities.StepEntity
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProcedureScreen(
    navController: NavController,
    procedureDao: ProcedureDao,
    draftId: String? = null
) {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    val steps = remember { mutableStateListOf<ProcedureStep>() }
    val scrollState = rememberScrollState()

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val currentDraftId = remember { mutableStateOf(draftId ?: UUID.randomUUID().toString()) }

    LaunchedEffect(Unit) {
        if (draftId == null) {
            procedureDao.insertProcedure(
                ProcedureDraftEntity(
                    id = currentDraftId.value,
                    title = "",
                    createdAt = System.currentTimeMillis()
                )
            )
            steps.add(ProcedureStep())
        } else {
            val full = procedureDao.getFullProcedure(draftId)
            title = TextFieldValue(full?.procedure?.title ?: "")
            full?.steps?.forEach { stepWithBlocks ->
                val blocks = stepWithBlocks.blocks.map {
                    when (it.type) {
                        "text" -> ContentBlock.Text(it.content)
                        "image" -> ContentBlock.Image(Uri.parse(it.content))
                        "video" -> ContentBlock.Video(Uri.parse(it.content))
                        else -> ContentBlock.Text("")
                    }
                }.toMutableStateList()
                steps.add(ProcedureStep(stepWithBlocks.step.id, blocks))
            }
        }
    }

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
            onValueChange = {
                title = it
                coroutineScope.launch {
                    procedureDao.insertProcedure(
                        ProcedureDraftEntity(id = currentDraftId.value, title = it.text)
                    )
                }
            },
            placeholder = { Text("e.g. How to change a shower head") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        steps.forEachIndexed { index, step ->
            StepEditor(
                step = step,
                stepNumber = index + 1,
                onAddBlock = { block ->
                    step.blocks.add(block)
                    coroutineScope.launch {
                        val stepId = step.id
                        procedureDao.insertSteps(listOf(StepEntity(id = stepId, procedureId = currentDraftId.value, index = index)))
                        procedureDao.insertBlocks(step.blocks.mapIndexed { i, b ->
                            BlockEntity(
                                stepId = stepId,
                                index = i,
                                type = when (b) {
                                    is ContentBlock.Text -> "text"
                                    is ContentBlock.Image -> "image"
                                    is ContentBlock.Video -> "video"
                                },
                                content = when (b) {
                                    is ContentBlock.Text -> b.text
                                    is ContentBlock.Image -> b.uri?.toString() ?: ""
                                    is ContentBlock.Video -> b.uri?.toString() ?: ""
                                }
                            )
                        })
                    }
                },
                onMoveBlock = { from, to ->
                    if (from in step.blocks.indices && to in step.blocks.indices) {
                        val movedBlock = step.blocks.removeAt(from)
                        step.blocks.add(to, movedBlock)
                    }
                },
                onRemoveBlock = { blockIndex ->
                    if (blockIndex in step.blocks.indices) step.blocks.removeAt(blockIndex)
                },
                onRemoveStep = {
                    steps.remove(step)
                    coroutineScope.launch {
                        procedureDao.insertSteps(steps.mapIndexed { i, s ->
                            StepEntity(id = s.id, procedureId = currentDraftId.value, index = i)
                        })
                    }
                },
                onMoveStepUp = {
                    if (index > 0) {
                        steps.removeAt(index).also { steps.add(index - 1, it) }
                    }
                },
                onMoveStepDown = {
                    if (index < steps.lastIndex) {
                        steps.removeAt(index).also { steps.add(index + 1, it) }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { steps.add(ProcedureStep()) }) {
                Text("Add Another Step")
            }
            Button(onClick = {
                Toast.makeText(context, "Procedure saved locally ✅", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }) {
                Text("Save Procedure")
            }
        }
    }
}

@Composable
fun StepEditor(
    step: ProcedureStep,
    stepNumber: Int,
    onAddBlock: (ContentBlock) -> Unit,
    onMoveBlock: (from: Int, to: Int) -> Unit,
    onRemoveBlock: (index: Int) -> Unit,
    onRemoveStep: () -> Unit,
    onMoveStepUp: () -> Unit,
    onMoveStepDown: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Already included in your previous version, left unchanged.
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
