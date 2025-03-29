package com.example.doyourself.ui.pages.createProcedure.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.doyourself.ui.pages.createProcedure.shared.ContentBlock

@Composable
fun TextBlock(block: ContentBlock.Text, onTextChanged: (String) -> Unit) {
    OutlinedTextField(
        value = block.text,
        onValueChange = onTextChanged,
        placeholder = { Text("Write something...") },
        modifier = Modifier.fillMaxWidth()
    )
}