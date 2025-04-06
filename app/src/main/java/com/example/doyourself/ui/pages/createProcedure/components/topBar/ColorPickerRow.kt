package com.example.doyourself.ui.pages.createProcedure.components.topBar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorPickerRow(
    colors: List<Color>,
    selected: Color,
    onColorChange: (Color) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        colors.forEach { swatchColor ->
            ColorSwatch(
                color = swatchColor,
                selected = swatchColor == selected,
                onSelect = { onColorChange(swatchColor) }
            )
        }
    }
}
