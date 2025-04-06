package com.example.doyourself.ui.pages.createProcedure.components.topEditorBar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.doyourself.ui.pages.createProcedure.components.SaveProcedureButton

@Composable
fun TopEditorBar(
    title: TextFieldValue,
    onTitleChange: (TextFieldValue) -> Unit,
    selectedColor: Color,
    onColorChange: (Color) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    // palette – tweak / extend as you like
    val swatches = listOf(
        Color(0xFF1E1E1E), // black‑ish
        Color(0xFF0A84FF), // blue
        Color(0xFFFFFFFF), // white
        Color(0xFF2C2C2E)  // dark grey
    )

    Surface(
        color = selectedColor,
        shadowElevation = 4.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {

            // Save button row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                SaveProcedureButton(onSaveClick = onSave)
            }

            Spacer(Modifier.height(12.dp))

            // Title
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                placeholder = { Text("Procedure title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = selectedColor,
                    focusedContainerColor = selectedColor
                )
            )

            Spacer(Modifier.height(12.dp))

            // Color picker
            ColorPickerRow(
                colors = swatches,
                selected = selectedColor,
                onColorChange = onColorChange
            )
        }
    }
}
