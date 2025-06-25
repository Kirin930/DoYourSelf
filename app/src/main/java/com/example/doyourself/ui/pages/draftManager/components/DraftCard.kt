package com.example.doyourself.ui.pages.draftManager.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.doyourself.data.local.entities.ProcedureWithStepsAndBlocks


@Composable
fun DraftCard(
    draft: ProcedureWithStepsAndBlocks,
    onOpen: () -> Unit,
    onDelete: () -> Unit,
    onPublish: () -> Unit,
    onExecute: () -> Unit
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
                    IconButton(onClick = onExecute) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Execute")
                    }
                }
            }
        }
    }
}