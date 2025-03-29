package com.example.doyourself.ui.pages.createProcedure.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpSize
import androidx.navigation.NavController
import com.example.doyourself.data.local.db.ProcedureDao
import com.example.doyourself.ui.pages.createProcedure.logic.saveProcedureToRoom
import com.example.doyourself.ui.pages.createProcedure.shared.ProcedureStep
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SaveProcedureButton(
    onSaveClick: () -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }

    Button(
        onClick = { showConfirmDialog = true }
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
