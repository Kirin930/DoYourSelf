package com.example.doyourself.ui.pages.createProcedure.components.topBar

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SaveProcedureButton(
    onSaveClick: () -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }

    Button(
        onClick = { showConfirmDialog = true },
        modifier = Modifier.width(120.dp).padding(8.dp)
    ) {
        Text("Save")
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirm Save") },
            text = { Text("Do you want to save this procedure locally?") },
            confirmButton = {
                TextButton(onClick = {
                    onSaveClick()
                    showConfirmDialog = false
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
