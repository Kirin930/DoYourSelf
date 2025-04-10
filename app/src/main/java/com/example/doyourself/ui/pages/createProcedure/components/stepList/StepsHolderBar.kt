package com.example.doyourself.ui.pages.createProcedure.components.stepList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StepsHolderBar(
    onMoveLeft: () -> Unit,
    onMoveRight: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        // This icon is our holder.
        Icon(
            imageVector = Icons.Rounded.Menu,
            contentDescription = "Reorder Steps",
            modifier = Modifier
                .size(32.dp)
                .clickable { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Move Left") },
                onClick = {
                    onMoveLeft()
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Move Right") },
                onClick = {
                    onMoveRight()
                    expanded = false
                }
            )
        }
    }
}
