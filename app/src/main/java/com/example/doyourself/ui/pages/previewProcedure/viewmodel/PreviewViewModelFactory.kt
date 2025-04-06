package com.example.doyourself.ui.pages.previewProcedure.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.doyourself.data.local.db.ProcedureDao

class PreviewViewModelFactory(
    private val procedureDao: ProcedureDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PreviewViewModel::class.java)) {
            return PreviewViewModel(procedureDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
