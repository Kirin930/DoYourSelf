package com.example.doyourself.ui.pages.createProcedure.logic

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.doyourself.ui.pages.createProcedure.shared.ProcedureStep

fun addStep(steps: SnapshotStateList<ProcedureStep>, step: ProcedureStep = ProcedureStep()) {
    steps.add(step)
}

fun deleteStep(steps: SnapshotStateList<ProcedureStep>, step: ProcedureStep) {
    steps.remove(step)
}

fun moveStepUp(steps: SnapshotStateList<ProcedureStep>, index: Int) {
    if (index > 0) {
        val step = steps.removeAt(index)
        steps.add(index - 1, step)
    }
}

fun moveStepDown(steps: SnapshotStateList<ProcedureStep>, index: Int) {
    if (index < steps.lastIndex) {
        val step = steps.removeAt(index)
        steps.add(index + 1, step)
    }
}
