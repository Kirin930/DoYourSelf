package com.example.doyourself.ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("create/${draft.procedure.id}")
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = draft.procedure.title.ifEmpty { "Untitled Procedure" }, style = MaterialTheme.typography.titleMedium)
                        Text(text = "Steps: ${draft.steps.size}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
