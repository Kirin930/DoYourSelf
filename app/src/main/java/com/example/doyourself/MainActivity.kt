package com.example.doyourself

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.room.Room
import com.example.doyourself.data.local.db.AppDatabase
import com.example.doyourself.ui.LoginScreen
import com.example.doyourself.ui.MainScreen
import com.example.doyourself.ui.pages.AccountScreen
import com.example.doyourself.ui.pages.MessagesScreen
import com.example.doyourself.ui.pages.createProcedure.CreateProcedureScreen
import com.example.doyourself.ui.pages.DraftManagerScreen
import com.example.doyourself.ui.theme.DoYourSelfTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DoYourSelfTheme {
                val navController = rememberNavController()
                val user by rememberFirebaseUser()

                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java,
                    "procedure_db"
                ).build()

                val dao = db.procedureDao()

                // Navigation graph always starts at "login"
                NavHost(navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen {
                            // onLoginSuccess can be empty; navigation is handled by observing user state.
                        }
                    }
                    composable("main") {
                        MainScreen(navController)
                    }
                    composable("create") {
                        CreateProcedureScreen(navController, dao)
                    }
                    composable("account") {
                        AccountScreen()
                    }
                    composable("messages") {
                        MessagesScreen()
                    }
                    composable("create/{draftId}") { backStackEntry ->
                        val draftId = backStackEntry.arguments?.getString("draftId")
                        CreateProcedureScreen(navController, dao, draftId)
                    }
                    composable("drafts") {
                        DraftManagerScreen(navController = navController, procedureDao = dao)
                    }
                }

                // Listen for FirebaseAuth user changes and navigate to "main" if user becomes non-null.
                LaunchedEffect(user) {
                    if (user != null) {
                        navController.navigate("main") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun rememberFirebaseUser(): State<FirebaseUser?> {
    val firebaseAuth = FirebaseAuth.getInstance()
    val userState = remember { mutableStateOf(firebaseAuth.currentUser) }

    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            userState.value = auth.currentUser
        }
        firebaseAuth.addAuthStateListener(listener)
        onDispose {
            firebaseAuth.removeAuthStateListener(listener)
        }
    }
    return userState
}
