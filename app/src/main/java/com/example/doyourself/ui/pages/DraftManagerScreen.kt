package com.example.doyourself.ui.pages

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.doyourself.data.local.db.ProcedureDao
import com.example.doyourself.data.local.entities.ProcedureWithStepsAndBlocks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun DraftManagerScreen(
    navController: NavController,
    procedureDao: ProcedureDao
) {
    val coroutineScope = rememberCoroutineScope()
    var drafts by remember { mutableStateOf(emptyList<ProcedureWithStepsAndBlocks>()) }
    var draftToDelete by remember { mutableStateOf<String?>(null) }

    // Action handlers
    val onOpenDraft = { id: String -> openDraft(navController, id) }
    val onDeleteDraft = { id: String -> draftToDelete = id }
    val onPublishDraft = { id: String -> publishDraft(navController, id) }

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

@Composable
fun DraftCard(
    draft: ProcedureWithStepsAndBlocks,
    onOpen: () -> Unit,
    onDelete: () -> Unit,
    onPublish: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxSize().clickable { onOpen() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = draft.procedure.title.ifEmpty { "Untitled Procedure" },
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text("Steps: ${draft.steps.size}", style = MaterialTheme.typography.bodySmall)
                    Text("Created: ${draft.procedure.createdAt}", style = MaterialTheme.typography.bodySmall)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(onClick = onDelete) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Draft")
                    }
                    IconButton(onClick = onPublish) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Publish Draft")
                    }
                }
            }
        }
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
