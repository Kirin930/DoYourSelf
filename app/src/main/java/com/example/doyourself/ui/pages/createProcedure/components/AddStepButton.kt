package com.example.doyourself.ui.pages.createProcedure.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AddStepButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("Add Step")
    }
}