package com.example.doyourself.ui.pages.createProcedure.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth

@Composable
fun TitleEditor(title: TextFieldValue, onTitleChange: (TextFieldValue) -> Unit) {
    OutlinedTextField(
        value = title,
        onValueChange = onTitleChange,
        placeholder = { Text("e.g. How to change a shower head") },
        modifier = Modifier
            .padding(bottom = 16.dp)
    )
}