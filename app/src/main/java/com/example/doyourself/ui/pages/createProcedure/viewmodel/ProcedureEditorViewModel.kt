package com.example.doyourself.ui.pages.createProcedure.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
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

class ProcedureEditorViewModel(
    private val procedureDao: ProcedureDao
) : ViewModel() {

    // Title state
    var title by mutableStateOf(TextFieldValue(""))
        private set

    // In Compose, a SnapshotStateList is a convenient mutable list type.
    // The UI can observe changes automatically.
    val steps = mutableStateListOf<ProcedureStep>()

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

    var procedureColor by mutableStateOf(Color(0xFFFFFFFF))
        private set
    fun onColorChange(c: Color) { procedureColor = c }


    fun onTitleChange(newValue: TextFieldValue) {
        title = newValue
    }

    fun addStep() {
        steps.add(ProcedureStep())
    }

    fun deleteStep(step: ProcedureStep) {
        steps.remove(step)
    }

    fun moveStepUp(index: Int) {
        if (index <= 0) return
        val step = steps.removeAt(index)
        steps.add(index - 1, step)
    }

    fun moveStepDown(index: Int) {
        if (index >= steps.lastIndex) return
        val step = steps.removeAt(index)
        steps.add(index + 1, step)
    }

    // For blocks:
    fun addBlock(step: ProcedureStep, block: ContentBlock) {
        step.blocks.add(block)
    }

    fun deleteBlock(step: ProcedureStep, index: Int) {
        if (index in step.blocks.indices) {
            step.blocks.removeAt(index)
        }
    }

    fun moveBlock(step: ProcedureStep, from: Int, to: Int) {
        if (from in step.blocks.indices && to in step.blocks.indices) {
            val moved = step.blocks.removeAt(from)
            step.blocks.add(to, moved)
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
                    backgroundColor = procedureColor.toString()
                )
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}