package com.example.doyourself.ui.pages.createProcedure.shared

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.mutableStateListOf
import java.util.UUID

class ProcedureStep(
    val id: String = UUID.randomUUID().toString(),
    var blocks: SnapshotStateList<ContentBlock> = mutableStateListOf()
)