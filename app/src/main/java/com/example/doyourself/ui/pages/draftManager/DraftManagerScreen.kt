package com.example.doyourself.ui.pages.draftManager

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.doyourself.data.local.db.ProcedureDao
import com.example.doyourself.data.local.entities.ProcedureWithStepsAndBlocks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.example.doyourself.ui.pages.draftManager.components.DraftCard
import com.example.doyourself.ui.pages.draftManager.viewmodel.*

@Composable
fun DraftManagerScreen(
    navController: NavController,
    procedureDao: ProcedureDao
) {
    //VM
    val viewModel: DraftManagerViewModel = viewModel(
        factory = DraftManagerViewModelFactory(procedureDao)
    )

    val coroutineScope = rememberCoroutineScope()
    var drafts by remember { mutableStateOf(emptyList<ProcedureWithStepsAndBlocks>()) }
    var draftToDelete by remember { mutableStateOf<String?>(null) }

    // Action handlers
    val onOpenDraft = { id: String -> viewModel.openDraft(navController, id) }
    val onDeleteDraft = { id: String -> viewModel.onDeleteDraftRequested(id) }
    val onPublishDraft = { id: String -> viewModel.publishDraft(id) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            procedureDao.getAllDrafts().collectLatest {
                drafts = it
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("My Drafts", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(drafts) { draft ->
                DraftCard(
                    draft = draft,
                    onOpen = { onOpenDraft(draft.procedure.id) },
                    onDelete = { onDeleteDraft(draft.procedure.id) },
                    onPublish = { onPublishDraft(draft.procedure.id) }
                )
            }
        }
    }

    draftToDelete?.let { draftId ->
        ConfirmDeleteDialog(
            onConfirm = {
                deleteDraft(
                    coroutineScope = coroutineScope,
                    procedureDao = procedureDao,
                    draftId = draftId,
                    onComplete = { draftToDelete = null }
                )
            },
            onDismiss = { draftToDelete = null }
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Draft") },
        text = { Text("Are you sure you want to delete this draft?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

fun openDraft(
    navController: NavController,
    draftId: String
) {
    navController.navigate("create/$draftId")
}

fun deleteDraft(
    coroutineScope: CoroutineScope,
    procedureDao: ProcedureDao,
    draftId: String,
    onComplete: () -> Unit
) {
    coroutineScope.launch {
        procedureDao.deleteProcedure(draftId)
        onComplete()
    }
}

fun publishDraft(
    navController: NavController,
    draftId: String
) {
    // TODO: Implement publish logic
}
