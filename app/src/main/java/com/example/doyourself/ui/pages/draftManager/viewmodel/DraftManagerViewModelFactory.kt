package com.example.doyourself.ui.pages.draftManager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.doyourself.data.local.db.ProcedureDao

class DraftManagerViewModelFactory(
    private val procedureDao: ProcedureDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DraftManagerViewModel::class.java)) {
            return DraftManagerViewModel(procedureDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
