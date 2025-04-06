package com.example.doyourself.ui.pages.createProcedure.components.topBar

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ColorSwatch(
    color: Color,
    selected: Boolean,
    onSelect: () -> Unit
) {
    val border = if (selected) BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface)
    else null

    Surface(
        shape = CircleShape,
        color = color,
        border = border,
        modifier = Modifier
            .size(36.dp)
            .clickable(onClick = onSelect)
    ) { /* empty – just a coloured circle */ }
}
