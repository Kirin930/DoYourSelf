package com.example.doyourself.ui.pages.createProcedure.components.stepList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StepsHolderBar(
    stepIndex: Int,
    onDeleteStep: (Int) -> Unit,
    onDuplicateStep: (Int) -> Unit,
    onMoveLeft: (Int) -> Unit,
    onMoveRight: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box (
        modifier = Modifier
            .size(48.dp)
            .padding(4.dp)
            .background(Color.LightGray, CircleShape) // DEBUG
            .clickable { expanded = true },
        contentAlignment = Alignment.Center
    ){
        Icon(
            imageVector = Icons.Rounded.Menu,
            contentDescription = "Step Options",
            modifier = Modifier
                .size(32.dp)

        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Delete Step") },
                onClick = {
                    onDeleteStep(stepIndex)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Duplicate Step") },
                onClick = {
                    onDuplicateStep(stepIndex)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Move Left") },
                onClick = {
                    onMoveLeft(stepIndex)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Move Right") },
                onClick = {
                    onMoveRight(stepIndex)
                    expanded = false
                }
            )
        }
    }
}
