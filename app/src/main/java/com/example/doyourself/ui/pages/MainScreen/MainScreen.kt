package com.example.doyourself.ui.pages.MainScreen

import android.R
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavHostController
import androidx.compose.material.icons.filled.Add
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.doyourself.data.local.db.ProcedureDao
import com.example.doyourself.ui.pages.MainScreen.components.PublishedProcedureCard
import com.example.doyourself.ui.pages.MainScreen.viewmodel.MainScreenViewModel
import com.example.doyourself.ui.pages.MainScreen.viewmodel.MainScreenViewModelFactory
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, procedureDao: ProcedureDao) {
    val viewModel: MainScreenViewModel = viewModel(
        factory = MainScreenViewModelFactory(procedureDao)
    )

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val procedures by viewModel.procedures.collectAsState()
    val likedIds by viewModel.likedIds.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search procedures or users...") },
                        modifier = Modifier.fillMaxWidth().absolutePadding(left = 0.dp, top = 2.dp, right = 10.dp, bottom = 2.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar (
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Create, contentDescription = "Drafts", tint = MaterialTheme.colorScheme.onPrimary) },
                    selected = false,
                    onClick = { navController.navigate("drafts") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Add, contentDescription = "Create", tint = MaterialTheme.colorScheme.onPrimary) },
                    selected = false,
                    onClick = { navController.navigate("create") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Account", tint = MaterialTheme.colorScheme.onPrimary) },
                    selected = false,
                    onClick = { navController.navigate("account") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Email, contentDescription = "Messages", tint = MaterialTheme.colorScheme.onPrimary) },
                    selected = false,
                    onClick = { navController.navigate("messages") }
                )
            }
        }
    ) { innerPadding ->
        val filtered = procedures.filter { it.title.contains(searchQuery.text, ignoreCase = true) }
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filtered) { procedure ->
                PublishedProcedureCard(
                    procedure = procedure,
                    liked = likedIds.contains(procedure.id),
                    onLike = { viewModel.likeProcedure(procedure) }
                )
            }
        }
    }
}
