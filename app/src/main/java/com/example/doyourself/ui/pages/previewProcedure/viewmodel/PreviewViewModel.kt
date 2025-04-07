package com.example.doyourself.ui.pages.previewProcedure.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.doyourself.data.local.db.ProcedureDao
import com.example.doyourself.data.local.entities.ProcedureWithStepsAndBlocks
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class PreviewViewModel(
    private val procedureDao: ProcedureDao
) : ViewModel() {

    // Holds the procedure with its steps/blocks
    private val _procedure = MutableStateFlow<ProcedureWithStepsAndBlocks?>(null)
    val procedure: StateFlow<ProcedureWithStepsAndBlocks?> = _procedure

    // Called by the screen after receiving the draftId
    fun loadProcedure(draftId: String) {
        viewModelScope.launch {
            val full = procedureDao.getFullProcedure(draftId)
            _procedure.update { full }
        }
    }

    fun editDraft(
        navController: NavController,
        draftId: String
    ) {
        navController.navigate("create/$draftId") {
            popUpTo("preview/$draftId") {
                inclusive = true
            }
        }
    }

    fun publishProcedure(draftId: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                // 1) Assicurati che l’utente sia loggato
                val currentUser = FirebaseAuth.getInstance().currentUser
                    ?: throw Exception("Utente non autenticato!")
                val userId = currentUser.uid

                // 2) Load the procedure from your local DB
                val procedureFull = procedureDao.getFullProcedure(draftId)
                    ?: throw IllegalStateException("No local procedure found")

                val procedureEntity = procedureFull.procedure
                val stepsFull = procedureFull.steps

                // 3) For each block, if it's an image or video, upload to Storage
                //    We'll build a list of "step data" in memory that references uploaded URLs
                val stepDataList = mutableListOf<Map<String, Any?>>()

                for ((stepIndex, stepWithBlocks) in stepsFull.sortedBy { it.step.index }.withIndex()) {
                    val blockDataList = mutableListOf<Map<String, Any?>>()

                    // Sort blocks by index, too
                    val sortedBlocks = stepWithBlocks.blocks.sortedBy { it.index }

                    for (block in sortedBlocks) {
                        val blockType = block.type
                        val blockContent = block.content
                        val blockMap = when (blockType) {
                            "text" -> {
                                // text block => store text directly
                                mapOf(
                                    "type" to "text",
                                    "content" to blockContent
                                )
                            }
                            "title" -> {
                                // title block => store text directly
                                mapOf(
                                    "type" to "title",
                                    "content" to blockContent
                                )
                            }
                            "image" -> {
                                // Upload the local image to Storage if not empty
                                val localUri = blockContent.takeIf { it.isNotEmpty() }
                                val downloadUrl = localUri?.let { uploadFileToFirebaseStorage(it, procedureEntity.id, "images") }
                                mapOf(
                                    "type" to "image",
                                    "content" to (downloadUrl ?: "")
                                )
                            }
                            "video" -> {
                                // Upload the local video to Storage
                                val localUri = blockContent.takeIf { it.isNotEmpty() }
                                val downloadUrl = localUri?.let { uploadFileToFirebaseStorage(it, procedureEntity.id, "videos") }
                                mapOf(
                                    "type" to "video",
                                    "content" to (downloadUrl ?: "")
                                )
                            }
                            else -> {
                                // fallback or unknown block
                                mapOf("type" to blockType, "content" to blockContent)
                            }
                        }
                        blockDataList.add(blockMap)
                    }

                    val stepMap = mapOf(
                        "stepIndex" to stepIndex,
                        "blocks" to blockDataList
                    )
                    stepDataList.add(stepMap)
                }

                // 4) Build the Firestore doc
                val procedureDoc = mapOf(
                    "procedureId" to procedureEntity.id,
                    "title" to procedureEntity.title,
                    "createdAt" to procedureEntity.createdAt, // or System.currentTimeMillis()
                    "steps" to stepDataList,
                    "userId" to userId // Add the user ID to the procedure document in Firestore"
                )

                // 5) Save to Firestore
                val db = FirebaseFirestore.getInstance()
                db.collection("publishedProcedures")
                    .document(procedureEntity.id) // use the same ID as local
                    .set(procedureDoc)
                    .await()

                // Completed successfully
                procedureDao.updateProcedurePublishedStatus(procedureEntity.id, true)

                onComplete(true)
            } catch (e: Exception) {
                // Error uploading or saving
                onComplete(false)
            }
        }
    }

    // Simple helper function for uploading images/videos to Storage
    private suspend fun uploadFileToFirebaseStorage(localUri: String, procedureId: String, folder: String): String {
        val storage = FirebaseStorage.getInstance()
        val ref = storage.reference.child("procedures/$procedureId/$folder/${System.currentTimeMillis()}")
        // localUri is e.g. "content://..."
        // Convert to Uri:
        val fileUri = Uri.parse(localUri)
        val uploadTask = ref.putFile(fileUri).await()
        // Once uploaded, get the download URL
        return ref.downloadUrl.await().toString()
    }

    fun onPublishSuccess(navController: NavController) {
        navController.navigate("main") {
            popUpTo("preview/{draftId}") {
                inclusive = true
            }
        }
    }

}
