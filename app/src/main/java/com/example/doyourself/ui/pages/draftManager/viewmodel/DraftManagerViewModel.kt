package com.example.doyourself.ui.pages.draftManager.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.doyourself.data.local.db.ProcedureDao
import com.example.doyourself.data.local.entities.ProcedureWithStepsAndBlocks
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DraftManagerViewModel(
    private val procedureDao: ProcedureDao
) : ViewModel() {

    // Holds the list of drafts from the database
    private val _drafts = MutableStateFlow<List<ProcedureWithStepsAndBlocks>>(emptyList())
    val drafts: StateFlow<List<ProcedureWithStepsAndBlocks>> = _drafts

    // Holds the ID of the draft the user wants to delete
    var draftToDelete by mutableStateOf<String?>(null)
        private set

    init {
        // Load the drafts as soon as the ViewModel is created
        viewModelScope.launch {
            procedureDao.getAllDrafts().collectLatest { list ->
                _drafts.value = list
            }
        }
    }

    // Called when the user taps Delete on a draft
    fun onDeleteDraftRequested(draftId: String) {
        draftToDelete = draftId
    }

    // Actually deletes the draft and resets draftToDelete
    fun confirmDeleteDraft() {
        draftToDelete?.let { id ->
            viewModelScope.launch {
                procedureDao.deleteProcedure(id)
                // Clear it once done
                draftToDelete = null
            }
        }
    }

    // Called when the user closes the dialog
    fun dismissDeleteDraft() {
        draftToDelete = null
    }

    // If you have more logic for “publishing,” you could do it here:
    fun publishDraft(draftId: String/*, onPublishComplete: () -> Unit*/) {
        viewModelScope.launch {
            // TODO: Do some publish logic, e.g., network call
            //onPublishComplete()
        }
    }

    fun openDraft(
        navController: NavController,
        draftId: String
    ) {
        navController.navigate("create/$draftId")
    }
}