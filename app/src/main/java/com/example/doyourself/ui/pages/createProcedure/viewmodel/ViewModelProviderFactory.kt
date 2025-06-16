package com.example.doyourself.ui.pages.createProcedure.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.doyourself.data.local.db.ProcedureDao

class ProcedureEditorViewModelFactory(
    private val procedureDao: ProcedureDao,
    private val navController: NavController
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProcedureEditorViewModel::class.java)) {
            return ProcedureEditorViewModel(procedureDao, navController) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}