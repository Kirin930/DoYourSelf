package com.example.doyourself.ui.pages.createProcedure

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.doyourself.data.local.db.ProcedureDao
import com.example.doyourself.ui.pages.createProcedure.components.*
import com.example.doyourself.ui.pages.createProcedure.components.bottomBar.BottomEditorBar
import com.example.doyourself.ui.pages.createProcedure.components.stepList.StepsHolderBar
import com.example.doyourself.ui.pages.createProcedure.components.stepList.StepsHorizontalPager
import com.example.doyourself.ui.pages.createProcedure.components.topBar.TopEditorBar
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
    val procedureColor: Color = viewModel.procedureColor
    val steps = viewModel.steps  // SnapshotStateList<ProcedureStep>
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { viewModel.steps.size }
    )

    Scaffold(
        topBar = {
            TopEditorBar(
                title = viewModel.title,
                onTitleChange = viewModel::onTitleChange,
                selectedColor = viewModel.procedureColor,
                onColorChange = viewModel::onColorChange,
                onSave = {
                    viewModel.saveProcedure(
                        onSuccess = { navController.popBackStack() },
                        onError = { /* handle error */ }
                    )
                }
            )
        },
        floatingActionButton = {
            // FloatingActionButton for adding a new Block to the current step.
            FloatingActionButton(onClick = {
                // Open modal bottom sheet with choices (Text, Image, Video, Title)
                // For now, for simplicity, add a default Text Block to current step.
                val currentStep = steps.getOrNull(pagerState.currentPage)
                if (currentStep != null) {
                    viewModel.addBlock(currentStep, com.example.doyourself.ui.pages.createProcedure.shared.ContentBlock.Text("", java.util.UUID.randomUUID().toString()))
                }
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Block")
            }
        },
        bottomBar = {
            BottomEditorBar(onAddStep = viewModel::addStep)
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            // Place the StepsHolderBar at the top-center for reordering steps.
            Box(modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
            ) {
                StepsHolderBar(
                    onMoveLeft = {
                        // Move current step left if possible.
                        coroutineScope.launch {
                            viewModel.moveStepLeft(pagerState.currentPage)
                        }
                    },
                    onMoveRight = {
                        coroutineScope.launch {
                            viewModel.moveStepRight(pagerState.currentPage)
                        }
                    }
                )
            }
            // Horizontal pager for steps.
            StepsHorizontalPager(
                steps = steps,
                onAddBlock = { step, block -> viewModel.addBlock(step, block) },
                onRemoveBlock = { step, index -> viewModel.deleteBlock(step, index) },
                onDuplicateBlock = { step, index -> viewModel.duplicateBlock(step, index) },
                onMoveBlock = { step, from, to -> viewModel.moveBlock(step, from, to) },
                onRemoveStep = { step -> viewModel.deleteStep(step) },
                onMoveStepLeft = { currentIndex -> viewModel.moveStepLeft(currentIndex) },
                onMoveStepRight = { currentIndex -> viewModel.moveStepRight(currentIndex) },
                modifier = Modifier.fillMaxSize()
            )
        }
    }



    /*
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
    }*/
}
