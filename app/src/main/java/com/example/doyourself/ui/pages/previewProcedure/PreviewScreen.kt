package com.example.doyourself.ui.pages.previewProcedure

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.doyourself.data.local.db.ProcedureDao
import com.example.doyourself.ui.pages.previewProcedure.components.ReadOnlyStep
import com.example.doyourself.ui.pages.previewProcedure.viewmodel.PreviewViewModel
import com.example.doyourself.ui.pages.previewProcedure.viewmodel.PreviewViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun PreviewScreen(
    navController: NavController,
    procedureDao: ProcedureDao,
    draftId: String
) {
    // Obtain the ViewModel
    val viewModel: PreviewViewModel = viewModel(
        factory = PreviewViewModelFactory(procedureDao)
    )

    // Load the procedure exactly once
    LaunchedEffect(draftId) {
        viewModel.loadProcedure(draftId)
    }

    // Observe the procedure from the VM
    val procedure = viewModel.procedure.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    var isPublishing by remember { mutableStateOf(false) }
    var publishSuccess by remember { mutableStateOf<Boolean?>(null) }
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Row {
                Text(
                    text = "Preview",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.width(100.dp))

                if (procedure != null) {
                    IconButton(onClick = {
                        viewModel.editDraft(
                            navController,
                            procedure.procedure.id
                        )
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(3.dp))
                    IconButton(
                        onClick = {
                            isPublishing = true

                            viewModel.publishProcedure(draftId) { success ->
                                isPublishing = false
                                publishSuccess = success
                            }
                        },

                        ) {
                        Icon(Icons.Default.Share, contentDescription = "Publish")
                    }
                }

            }
            Spacer(modifier = Modifier.height(24.dp))

            if (procedure == null) {
                Text("Loading procedure...")
            } else {
                // Show the procedure’s title
                Text(
                    text = procedure.procedure.title.ifEmpty { "Untitled Procedure" },
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Show the read-only steps
                procedure.steps.sortedBy { it.step.index }.forEachIndexed { index, stepWithBlocks ->
                    ReadOnlyStep(
                        stepNumber = index + 1,
                        blocks = stepWithBlocks.blocks.sortedBy { it.index }
                            .map { it.type to it.content }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (isPublishing) {
                CircularProgressIndicator()
            }

            publishSuccess?.let { wasSuccessful ->
                if (wasSuccessful) {
                    Text("Publish succeeded!")
                    viewModel.onPublishSuccess(navController)
                } else {
                    Text("Publish failed, please try again")
                }
            }
        }
    }
}
