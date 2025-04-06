package com.example.doyourself.ui.pages.createProcedure.components.bottomBar

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.doyourself.ui.pages.createProcedure.components.TextButtonTemplate
import com.example.doyourself.ui.theme.DoYourSelfTheme

@Preview(showBackground = true)
@Composable
fun AddStepButtonPreview() {
    DoYourSelfTheme {
        AddStepButton { }
    }
}

@Composable
fun AddStepButton(onClick: () -> Unit = {}) {
    TextButtonTemplate(text = "Add Step", onClick = onClick)
}
