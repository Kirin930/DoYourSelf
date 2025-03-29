package com.example.doyourself.ui.pages.createProcedure.logic

import com.example.doyourself.ui.pages.createProcedure.shared.ProcedureStep
import com.example.doyourself.ui.pages.createProcedure.shared.ContentBlock

fun addBlock(step: ProcedureStep, block: ContentBlock) {
    step.blocks.add(block)
}

fun deleteBlock(step: ProcedureStep, index: Int) {
    if (index in step.blocks.indices) {
        step.blocks.removeAt(index)
    }
}

fun moveBlock(step: ProcedureStep, from: Int, to: Int) {
    if (from in step.blocks.indices && to in step.blocks.indices) {
        val moved = step.blocks.removeAt(from)
        step.blocks.add(to, moved)
    }
}
