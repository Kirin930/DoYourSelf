package com.example.doyourself.ui.pages.createProcedure.components.stepList.step

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableBlockItem(
    key: Any,
    reorderState: ReorderableLazyListState,
    onDelete: () -> Unit,
    onDuplicate: () -> Unit,
    content: @Composable () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            // First, enable reordering on the item.
            .reorderable(reorderState)
            // Then, add a combinedClickable that listens for long presses.
            .combinedClickable(
                onClick = { /* Could select the block, etc. */ },
                onLongClick = { showMenu = true }
            )
            .fillMaxWidth()
    ) {
        // The block content.
        content()
        // When long pressed, show our floating menu.
        if (showMenu) {
            BlockOptionsMenu(
                onDelete = onDelete,
                onDuplicate = onDuplicate,
                onDismiss = { showMenu = false }
            )
        }
    }
}
