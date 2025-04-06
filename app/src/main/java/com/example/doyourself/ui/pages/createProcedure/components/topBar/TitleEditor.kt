package com.example.doyourself.ui.pages.createProcedure.components.topBar

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding

@Composable
fun TitleEditor(title: TextFieldValue, onTitleChange: (TextFieldValue) -> Unit) {
    OutlinedTextField(
        value = title,
        onValueChange = onTitleChange,
        placeholder = { Text("How to ...") },
        modifier = Modifier
            .padding(bottom = 16.dp)
    )
}