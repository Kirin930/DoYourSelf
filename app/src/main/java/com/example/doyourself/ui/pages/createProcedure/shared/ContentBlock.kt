package com.example.doyourself.ui.pages.createProcedure.shared

import android.net.Uri
import java.util.UUID

sealed class ContentBlock {
    abstract val id: String // Unique identifier for each block

    data class Text(var text: String, override val id: String = UUID.randomUUID().toString()) : ContentBlock()
    data class Title(var text: String, override val id: String = UUID.randomUUID().toString()) : ContentBlock()
    data class Image(var uri: Uri? = null, override val id: String = UUID.randomUUID().toString()) : ContentBlock()
    data class Video(var uri: Uri? = null, override val id: String = UUID.randomUUID().toString()) : ContentBlock()
}