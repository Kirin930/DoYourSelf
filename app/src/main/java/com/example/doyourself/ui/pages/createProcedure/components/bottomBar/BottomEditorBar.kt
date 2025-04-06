package com.example.doyourself.ui.pages.createProcedure.components.bottomBar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomEditorBar(
    onAddStep: () -> Unit
) {
    // Retrieve the IME insets as padding values.
    val imePadding = WindowInsets.ime.asPaddingValues()
    // If the bottom padding is greater than zero, we assume the keyboard is visible.
    val isKeyboardVisible = imePadding.calculateBottomPadding() > 0.dp

    // AnimatedVisibility fades the bottom bar in and out.
    AnimatedVisibility(
        visible = !isKeyboardVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) // Adjust as needed for your design.
        ) {
            AddStepButton(onClick = onAddStep)
        }
    }
}
