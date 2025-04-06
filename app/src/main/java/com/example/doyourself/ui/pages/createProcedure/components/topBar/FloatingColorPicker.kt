package com.example.doyourself.ui.pages.createProcedure.components.topBar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FloatingColorPicker(
    anchor: @Composable (open: () -> Unit) -> Unit,
    swatches: List<Color>,
    selected: Color,
    onColorChosen: (Color) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    // ── 1.  the anchor (our little circle) ──────────────
    anchor { expanded = true }

    // ── 2.  the floating menu ───────────────────────────
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        swatches.forEach { color ->
            DropdownMenuItem(
                text = {},
                onClick = {
                    onColorChosen(color)
                    expanded = false
                },
                leadingIcon = {
                    Surface(
                        shape = CircleShape,
                        color = color,
                        border = if (color == selected)
                            BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface)
                        else null,
                        modifier = Modifier.size(32.dp)
                    ) {}
                }
            )
        }
    }
}
