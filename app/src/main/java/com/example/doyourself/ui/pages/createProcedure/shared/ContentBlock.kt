package com.example.doyourself.ui.pages.createProcedure.shared

import android.net.Uri

sealed class ContentBlock {
    data class Text(val text: String) : ContentBlock()
    data class Image(val uri: Uri?) : ContentBlock()
    data class Video(val uri: Uri?) : ContentBlock()
}