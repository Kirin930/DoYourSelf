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
import com.example.doyourself.ui.pages.createProcedure.components.topBar.TopEditorBar
import com.example.doyourself.ui.pages.createProcedure.viewmodel.ProcedureEditorViewModel
import com.example.doyourself.ui.pages.createProcedure.viewmodel.ProcedureEditorViewModelFactory
import kotlinx.coroutines.launch
import androidx.activity.compose.BackHandler
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.doyourself.ui.pages.createProcedure.components.stepList.StepNavigationRow
import com.example.doyourself.ui.pages.createProcedure.components.stepList.StepsScreen
import com.example.doyourself.ui.pages.createProcedure.shared.ContentBlock
import java.util.UUID


@Composable
fun CreateProcedureScreen(
    navController: NavController,
    procedureDao: ProcedureDao,
    draftId: String? = null
) {
    // Disable system back button on this screen
    BackHandler {
        navController.navigate("main") {
            popUpTo(0) { inclusive = true }
        }
    }
    // 1) Obtain our VM instance
    val viewModel: ProcedureEditorViewModel = viewModel(
        factory = ProcedureEditorViewModelFactory(procedureDao)
    )

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Snackbar state for error messages
    val snackbarHostState = remember { SnackbarHostState() }
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

    // State controlling whether the "Add Block" menu is visible.
    var showAddMenu by remember { mutableStateOf(false) }
    var selectedStepIndexForMenu by remember { mutableStateOf<Int?>(null) }
    var showMenuForStep by remember { mutableStateOf<Int?>(null) } // step index or null

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopEditorBar(
                title = viewModel.title,
                onTitleChange = viewModel::onTitleChange,
                selectedColor = viewModel.procedureColor,
                onColorChange = viewModel::onColorChange,
                onSave = {
                    viewModel.saveProcedure(
                        navController = navController,
                        onSuccess = {
                            // Navigate and remove this screen from back stack
                            navController.navigate("drafts") {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onError = { error ->
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Error saving: ${error.localizedMessage}")
                            }
                        }
                    )
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            ExpandableFabMenu(
                onAddTitle = {
                    viewModel.addBlock(
                        step = viewModel.steps[viewModel.currentStepIndex],
                        block = ContentBlock.Title(
                            text = "New Title",
                            id = UUID.randomUUID().toString()
                        )
                    )
                },
                onAddText = {
                    viewModel.addBlock(
                        step = viewModel.steps[viewModel.currentStepIndex],
                        block = ContentBlock.Text(
                            text = "New Text",
                            id = UUID.randomUUID().toString()
                        )
                    )
                },
                onAddImage = {/*
                    // Optional: open image picker, or use a placeholder
                    viewModel.addBlock(
                        step = viewModel.steps[viewModel.currentStepIndex],
                        block = ContentBlock.Image(
                            uri = "", // placeholder or selected image URI
                            id = UUID.randomUUID().toString()
                        )
                    )
                */},
                onAddVideo = {/*
                    viewModel.addBlock(
                        step = viewModel.steps[viewModel.currentStepIndex],
                        block = ContentBlock.Video(
                            uri = "", // placeholder or selected video URI
                            id = UUID.randomUUID().toString()
                        )
                    )
                */},
                onAddStep = {
                    viewModel.addStep()
                    viewModel.currentStepIndex = viewModel.steps.lastIndex
                }
            )
        },
        bottomBar = {
           /*BottomEditorBar(onAddStep = {
                viewModel.addStep()
                coroutineScope.launch {
                    pagerState.scrollToPage(viewModel.steps.lastIndex)
                }
            })
        */}
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .imePadding() // keeps FAB visible above keyboard
        ) {
            // Horizontal pager for steps.
            StepsScreen(
                steps = steps,
                onAddBlock = { step, block -> viewModel.addBlock(step, block) },
                onRemoveBlock = { step, index -> viewModel.deleteBlock(step, index) },
                onDuplicateBlock = { step, index -> viewModel.duplicateBlock(step, index) },
                onMoveBlock = { step, from, to -> viewModel.moveBlock(step, from, to) },
                onRemoveStep = { step -> viewModel.deleteStep(step) },
                modifier = Modifier.fillMaxSize(),
                currentStepIndex = viewModel.currentStepIndex,
                onStepSelected = { viewModel.currentStepIndex = it },
                onDuplicateStep = { currentStepIndex -> viewModel.duplicateStep(currentStepIndex)}
            )
        }
    }

}
