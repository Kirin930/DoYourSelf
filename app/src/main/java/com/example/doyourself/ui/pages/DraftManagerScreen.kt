package com.example.doyourself.ui.pages

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.doyourself.data.local.db.ProcedureDao
import com.example.doyourself.data.local.entities.ProcedureWithStepsAndBlocks
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun DraftManagerScreen(
    navController: NavController,
    procedureDao: ProcedureDao
) {
    val coroutineScope = rememberCoroutineScope()
    var drafts by remember { mutableStateOf(emptyList<ProcedureWithStepsAndBlocks>()) }
    var showDeleteDialog by remember { mutableStateOf<Pair<String, Boolean>?>(null) }

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
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = draft.procedure.title.ifEmpty { "Untitled Procedure" },
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.clickable {
                                navController.navigate("create/${draft.procedure.id}")
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Steps: ${draft.steps.size}", style = MaterialTheme.typography.bodySmall)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                IconButton(onClick = {
                                    showDeleteDialog = Pair(draft.procedure.id, true)
                                }) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Draft")
                                }
                                IconButton(onClick = {
                                    // Preview & publish (future)
                                }) {
                                    Icon(imageVector = Icons.Default.Share, contentDescription = "Publish Draft")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Confirmation dialog
    showDeleteDialog?.let { (draftId, open) ->
        if (open) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = null },
                title = { Text("Delete Draft") },
                text = { Text("Are you sure you want to delete this draft?") },
                confirmButton = {
                    TextButton(onClick = {
                        coroutineScope.launch {
                            procedureDao.deleteProcedure(draftId)
                            showDeleteDialog = null
                        }
                    }) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
