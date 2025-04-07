package com.example.doyourself.ui.pages.createProcedure.components.stepList.step.blocks

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.example.doyourself.ui.pages.createProcedure.shared.ContentBlock

@Composable
fun TitleBlock(
    block: ContentBlock.Title,
    onTitleChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = block.text,
        onValueChange = onTitleChanged,
        placeholder = { Text("Step Title") },
        modifier = Modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.headlineSmall // H1-like style; adjust as needed
    )
}
