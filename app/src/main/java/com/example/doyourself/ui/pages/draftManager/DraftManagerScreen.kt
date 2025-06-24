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
import com.example.doyourself.ui.pages.draftManager.components.*
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

    val drafts by viewModel.drafts.collectAsState()
    val draftToDelete = viewModel.draftToDelete

    val procedures by viewModel.procedures.collectAsState()
    val procedureToDelete = viewModel.procedureToDelete

    val likedProcedures by viewModel.likedProcedures.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Published", "Drafts", "Liked")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("My Procedures", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        //Tab Row (too choose with tab we have to show
        TabRow(selectedTabIndex = selectedTab) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = (selectedTab == index),
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        // 2) Show content based on selectedTab
        when (selectedTab) {
            0 -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(procedures) { procedure ->
                    ProcedureCard(
                        procedure = procedure,
                        onDelete = { viewModel.onDeleteProcedureRequested(procedure.procedure.id) }
                    )
                }
            }
            1 -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(drafts) { draft ->
                    DraftCard(
                        draft = draft,
                        onOpen = { viewModel.openDraft(navController, draft.procedure.id) },
                        onDelete = { viewModel.onDeleteDraftRequested(draft.procedure.id) },
                        onPublish = { viewModel.publishDraft(navController, draft.procedure.id) }
                    )
                }
            }
            2 -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(likedProcedures) { procedure ->
                    LikedProcedureCard(
                        procedure = procedure,
                        onDelete = {}
                    )
                }
            }
        }


        /*
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(drafts) { draft ->
                DraftCard(
                    draft = draft,
                    onOpen = { viewModel.openDraft(navController, draft.procedure.id) },
                    onDelete = { viewModel.onDeleteDraftRequested(draft.procedure.id) },
                    onPublish = { viewModel.publishDraft(navController, draft.procedure.id) }
                )
            }
        }*/
    }

    draftToDelete?.let { draftId ->
        ConfirmDraftDeleteDialog(
            onConfirm = { viewModel.confirmDeleteDraft() },
            onDismiss = { viewModel.dismissDeleteDraft() }
        )
    }

    procedureToDelete?.let { procedureId ->
        ConfirmProcedureDeleteDialog(
            onConfirm = { viewModel.confirmDeleteProcedure() },
            onDismiss = { viewModel.dismissDeleteProcedure() }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmDraftDeleteDialog(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmProcedureDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Procedure") },
        text = { Text("Are you sure you want to delete this procedure? This will delete it also from the cloud!") },
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