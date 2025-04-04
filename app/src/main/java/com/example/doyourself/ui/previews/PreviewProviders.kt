package com.example.doyourself.ui.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class ButtonTextProvider : PreviewParameterProvider<String> {
    override val values: Sequence<String> = sequenceOf("Add Step", "Submit", "Continue")
}
