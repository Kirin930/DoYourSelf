package com.example.doyourself.ui.pages.createProcedure

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.doyourself.data.local.db.ProcedureDao
import com.example.doyourself.ui.pages.createProcedure.components.*
import com.example.doyourself.ui.pages.createProcedure.viewmodel.ProcedureEditorViewModel
import com.example.doyourself.ui.pages.createProcedure.viewmodel.ProcedureEditorViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun CreateProcedureScreen(
    navController: NavController,
    procedureDao: ProcedureDao,
    draftId: String? = null
) {
    // 1) Obtain our VM instance
    val viewModel: ProcedureEditorViewModel = viewModel(
        factory = ProcedureEditorViewModelFactory(procedureDao)
    )

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // 2) Load data if needed. (Load once)
    LaunchedEffect(draftId) {
        viewModel.loadProcedure(draftId)
    }

    // 3) Observe VM state
    val title: TextFieldValue = viewModel.title
    val steps = viewModel.steps  // SnapshotStateList<ProcedureStep>

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Title + Save
        Row(
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Text("Procedure Title", style = MaterialTheme.typography.titleMedium)

            SaveProcedureButton(
                onSaveClick = {
                    // Let the VM do the save
                    viewModel.saveProcedure(
                        onSuccess = {
                            // Pop back on success
                            navController.popBackStack()
                        },
                        onError = {
                            // Show an error message or toast
                            // your choice
                        }
                    )
                }
            )
        }

        TitleEditor(
            title = title,
            onTitleChange = { viewModel.onTitleChange(it) }
        )

        // Steps List
        StepsList(
            steps = steps,
            onRemoveStep = { step ->
                viewModel.deleteStep(step)
            },
            onMoveStepUp = { index ->
                viewModel.moveStepUp(index)
            },
            onMoveStepDown = { index ->
                viewModel.moveStepDown(index)
            }/*,
            onAddBlock = { step, block ->
                viewModel.addBlock(step, block)
            },
            onDeleteBlock = { step, blockIndex ->
                viewModel.deleteBlock(step, blockIndex)
            },
            onMoveBlock = { step, from, to ->
                viewModel.moveBlock(step, from, to)
            }*/
        )

        // Add Step
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AddStepButton(onClick = { viewModel.addStep() })
        }
    }
}
