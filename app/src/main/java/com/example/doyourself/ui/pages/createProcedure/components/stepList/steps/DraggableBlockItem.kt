package com.example.doyourself.ui.pages.createProcedure.components.stepList.steps

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.reorderable
import java.nio.file.WatchEvent


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableBlockItem(
    key: Any,
    reorderState: ReorderableLazyListState,
    elevation: State<Dp>,
    onDelete: () -> Unit,
    onDuplicate: () -> Unit,
    content: @Composable () -> Unit
) {
    // State to control the visibility of the options menu.
    var showMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .shadow(elevation.value)
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Display the actual block content.
        Box(modifier = Modifier.weight(1f)) {
            content()
        }

        // Separate icon for showing the options menu.
        IconButton(onClick = { showMenu = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Options",
                modifier = Modifier.detectReorderAfterLongPress(reorderState)
            )
        }

        if(showMenu) {
            BlockOptionsMenu(
                onDelete = onDelete,
                onDuplicate = onDuplicate,
                onDismiss = { showMenu = false }
            )
        }
    }
}