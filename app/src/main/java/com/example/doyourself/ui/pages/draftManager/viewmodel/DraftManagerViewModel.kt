package com.example.doyourself.ui.pages.draftManager.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.doyourself.data.local.db.ProcedureDao
import com.example.doyourself.data.local.entities.ProcedureWithStepsAndBlocks
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DraftManagerViewModel(
    private val procedureDao: ProcedureDao
) : ViewModel() {

    // Holds the list of drafts from the database
    private val _drafts = MutableStateFlow<List<ProcedureWithStepsAndBlocks>>(emptyList())
    val drafts: StateFlow<List<ProcedureWithStepsAndBlocks>> = _drafts

    private val _procedures = MutableStateFlow<List<ProcedureWithStepsAndBlocks>>(emptyList())
    val procedures: StateFlow<List<ProcedureWithStepsAndBlocks>> = _procedures

    // Holds the ID of the draft the user wants to delete
    var draftToDelete by mutableStateOf<String?>(null)
        private set

    // Holds the ID of the draft the user wants to delete
    var procedureToDelete by mutableStateOf<String?>(null)
        private set

    init {
        // Load the drafts as soon as the ViewModel is created
        viewModelScope.launch {
            procedureDao.getAllDrafts().collectLatest { list ->
                _procedures.value = list.filter { it.procedure.isPublished }
                _drafts.value     = list.filter { !it.procedure.isPublished }

                //_drafts.value = list
                /*for (draft in list) {
                    if (draft.procedure.isPublished) {
                        _procedures.value += draft
                    } else {
                        _drafts.value += draft
                    }
                }*/
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

    // If you have more logic for “preview before publishing” you could do it here:
    fun publishDraft(
        navController: NavController,
        draftId: String
    ) {
        navController.navigate("preview/$draftId")
    }

    fun openDraft(
        navController: NavController,
        draftId: String
    ) {
        navController.navigate("create/$draftId")
    }


    // Called when the user taps Delete on a Procedure
    fun onDeleteProcedureRequested(draftId: String) {
        procedureToDelete = draftId
    }

    // Actually deletes the procedure and resets procedureToDelete
    fun confirmDeleteProcedure() {
        procedureToDelete?.let { id ->
            viewModelScope.launch {
                deletePublishedProcedure(
                    procedureId = id,
                    dao = procedureDao
                )

                // Clear it once done
                procedureToDelete = null
            }
        }
    }

    // Called when the user closes the dialog
    fun dismissDeleteProcedure() {
        procedureToDelete = null
    }

    suspend fun deletePublishedProcedure(
        procedureId: String,            // == firestoreId
        dao: ProcedureDao
    ) {
        // 1. Remove the Firestore doc → triggers the Cloud Function
        FirebaseFirestore.getInstance()
            .collection("publishedProcedures")
            .document(procedureId)
            .delete()
            .await()

        // 2. Update local DB
        dao.deleteProcedure(procedureId)           // or mark isPublished = false
    }

}