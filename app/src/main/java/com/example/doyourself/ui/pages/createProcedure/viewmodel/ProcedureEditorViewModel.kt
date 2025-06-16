package com.example.doyourself.ui.pages.createProcedure.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doyourself.data.local.db.ProcedureDao
import com.example.doyourself.data.local.entities.BlockEntity
import com.example.doyourself.data.local.entities.ProcedureDraftEntity
import com.example.doyourself.data.local.entities.StepEntity
import com.example.doyourself.ui.pages.createProcedure.shared.ContentBlock
import com.example.doyourself.ui.pages.createProcedure.shared.ProcedureStep
import com.example.doyourself.ui.pages.createProcedure.logic.saveProcedureToRoom
import kotlinx.coroutines.launch
import java.util.UUID
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.net.toUri
import androidx.navigation.NavController

class ProcedureEditorViewModel(
    private val procedureDao: ProcedureDao,
    private val navController: NavController
) : ViewModel() {

    // Title state
    var title by mutableStateOf(TextFieldValue(""))
        private set

    var procedureColor by mutableStateOf(Color(0xFFFFFFFF))
        private set

    // Steps list: each step is a ProcedureStep.
    val steps: SnapshotStateList<ProcedureStep> = mutableStateListOf()

    // Current draft ID (or newly generated if none is provided)
    var currentDraftId: String? by mutableStateOf(null)
        private set

    /**
     * Public method to initialize the ViewModel
     * with either a brand-new procedure (if draftId == null)
     * or by loading an existing procedure from the database.
     */
    fun loadProcedure(draftId: String?) {
        currentDraftId = draftId ?: UUID.randomUUID().toString()
        viewModelScope.launch {
            if (draftId == null) {
                // For a new procedure, just ensure we have at least one step
                if (steps.isEmpty()) {
                    steps.add(ProcedureStep())
                }
            } else {
                val full = procedureDao.getFullProcedure(draftId)
                if (full != null) {
                    // Update title
                    title = TextFieldValue(full.procedure.title)

                    // Clear any existing steps
                    steps.clear()

                    // Sort steps and blocks by index before building them
                    full.steps
                        .sortedBy { it.step.index }
                        .forEach { stepWithBlocks ->
                            val blocks = stepWithBlocks.blocks
                                .sortedBy { it.index }
                                .map {
                                    when (it.type) {
                                        "text" -> ContentBlock.Text(it.content)
                                        "title" -> ContentBlock.Title(it.content)
                                        "image" -> ContentBlock.Image(it.content.toUri())
                                        "video" -> ContentBlock.Video(it.content.toUri())
                                        else -> ContentBlock.Text("")
                                    }
                                }
                                .toMutableStateList()
                            steps.add(ProcedureStep(stepWithBlocks.step.id, blocks))
                        }
                }
            }
        }
    }

    // ----------------------
    // State update methods
    // ----------------------


    fun onColorChange(newColor: Color) {
        procedureColor = newColor
    }

    fun onTitleChange(newValue: TextFieldValue) {
        title = newValue
    }

    // --- Step management ---

    fun addStep() {
        // When a new step is added, automatically insert a TitleBlock and a TextBlock.
        val newStep = ProcedureStep(
            blocks = mutableStateListOf(
                ContentBlock.Title(text = "Step Title"),
                ContentBlock.Text(text = "Step description")
            )
        )
        steps.add(newStep)
    }

    fun deleteStep(step: ProcedureStep) {
        steps.remove(step)
    }

    fun moveStepLeft(currentIndex: Int) {
        if (currentIndex > 0 && currentIndex < steps.size) {
            steps.add(currentIndex - 1, steps.removeAt(currentIndex))
        }
    }
    fun moveStepRight(currentIndex: Int) {
        if (currentIndex < steps.size - 1) {
            steps.add(currentIndex + 1, steps.removeAt(currentIndex))
        }
    }



    // --- Block management for a given step ---

    fun addBlock(step: ProcedureStep, block: ContentBlock) {
        step.blocks.add(block)
    }

    fun deleteBlock(step: ProcedureStep, index: Int) {
        if (index in step.blocks.indices) {
            step.blocks.removeAt(index)
        }
    }

    fun duplicateBlock(step: ProcedureStep, blockIndex: Int) {
        if (blockIndex in step.blocks.indices) {
            val original = step.blocks[blockIndex]
            val duplicate = when (original) {
                is ContentBlock.Title -> original.copy(id = UUID.randomUUID().toString())
                is ContentBlock.Text -> original.copy(id = UUID.randomUUID().toString())
                is ContentBlock.Image -> original.copy(id = UUID.randomUUID().toString())
                is ContentBlock.Video -> original.copy(id = UUID.randomUUID().toString())
            }
            // Insert the duplicate right after the original.
            step.blocks.add(blockIndex + 1, duplicate)
        }
    }

    fun moveBlock(step: ProcedureStep, fromIndex: Int, toIndex: Int) {
        if (fromIndex in step.blocks.indices && toIndex in 0..step.blocks.size) {
            step.blocks.add(toIndex, step.blocks.removeAt(fromIndex))
        }
    }

    // ----------------------
    // Save logic
    // ----------------------

    fun saveProcedure(
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val pid = currentDraftId ?: UUID.randomUUID().toString()
                // Use your existing save logic
                saveProcedureToRoom(
                    procedureId = pid,
                    title = title.text,
                    steps = steps,
                    procedureDao = procedureDao,
                    backgroundColor = procedureColor.toString(),
                    navController = navController
                )
                onSuccess()
                navController.navigate("draftManager")
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}