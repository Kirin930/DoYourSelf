package com.example.doyourself.ui.pages.createProcedure.components.topBar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun TopEditorBar(
    title: TextFieldValue,
    onTitleChange: (TextFieldValue) -> Unit,
    selectedColor: Color,
    onColorChange: (Color) -> Unit,
    onSave: () -> Unit
) {
    val swatches = listOf(
        Color(0xFF1E1E1E), Color(0xFF0A84FF), Color(0xFFFFFFFF),
        Color(0xFF2C2C2E), Color(0xFFE91E63), Color(0xFF4CAF50)
    )

    Surface(
        color = selectedColor,
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            //horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Left column: Title (top) and Color Picker (bottom)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TitleEditor(
                    title = title,
                    onTitleChange = onTitleChange
                )

                    /* The circular color picker below the title.
                FloatingColorPicker(
                    anchor = { open ->
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Color Picker",
                            tint = Color(0xFF8C8A8A),
                            modifier = Modifier
                                .size(20.dp)
                                .clickable(onClick = open)
                        )
                        /*

                        Surface(
                            shape = CircleShape,
                            color = selectedColor,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
                            modifier = Modifier
                                .size(20.dp)
                                .clickable(onClick = open)
                        )*/
                    },
                    swatches = swatches,
                    selected = selectedColor,
                    onColorChosen = onColorChange
                )*/
            }

            Box(modifier = Modifier.width(120.dp)) {
                SaveProcedureButton(onSaveClick = onSave)
            }
        }
    }
}
