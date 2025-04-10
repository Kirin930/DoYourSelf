package com.example.doyourself.ui.pages.createProcedure.components.stepList.steps

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BlockOptionsMenu(
    onDelete: () -> Unit,
    onDuplicate: () -> Unit,
    onDismiss: () -> Unit
) {
    DropdownMenu(
        expanded = true,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(
            text = { Text("Delete") },
            onClick = { onDelete(); onDismiss() },
            leadingIcon = { Icon(Icons.Rounded.Delete, contentDescription = "Delete") }
        )
        DropdownMenuItem(
            text = { Text("Duplicate") },
            onClick = { onDuplicate(); onDismiss() },
            leadingIcon = { Icon(Icons.Rounded.Add, contentDescription = "Duplicate") }
        )
    }
}
