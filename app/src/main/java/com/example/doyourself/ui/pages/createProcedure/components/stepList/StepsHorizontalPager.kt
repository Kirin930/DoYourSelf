package com.example.doyourself.ui.pages.createProcedure.components.stepList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.doyourself.ui.pages.createProcedure.components.stepList.steps.StepEditor
import com.example.doyourself.ui.pages.createProcedure.shared.ContentBlock
import com.example.doyourself.ui.pages.createProcedure.shared.ProcedureStep

@Composable
fun StepsHorizontalPager(
    steps: List<ProcedureStep>,
    onAddBlock: (step: ProcedureStep, block: ContentBlock) -> Unit,
    onRemoveBlock: (step: ProcedureStep, index: Int) -> Unit,
    onDuplicateBlock: (step: ProcedureStep, index: Int) -> Unit,
    onMoveBlock: (step: ProcedureStep, from: Int, to: Int) -> Unit,
    onRemoveStep: (ProcedureStep) -> Unit,
    onMoveStepLeft: (currentIndex: Int) -> Unit,
    onMoveStepRight: (currentIndex: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = steps::size,
    )

    HorizontalPager(
        state = pagerState,
        key = { page -> steps[page].id },
        modifier = modifier.fillMaxSize()
    ) { page ->
        // Each page shows one StepEditor.
        StepEditor(
            step = steps[page],
            stepNumber = page + 1,
            onAddBlock = { block -> onAddBlock(steps[page], block) },
            onRemoveBlock = { index -> onRemoveBlock(steps[page], index) },
            onDuplicateBlock = { index -> onDuplicateBlock(steps[page], index) },
            onMoveBlock = { from, to -> onMoveBlock(steps[page], from, to) },
            onRemoveStep = { onRemoveStep(steps[page]) },
            // Note: since we now reorder steps horizontally via the holder icon,
            // we don’t need vertical move-step controls here.
            onMoveStepUp = {},
            onMoveStepDown = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
