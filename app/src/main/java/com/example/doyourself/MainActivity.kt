package com.example.doyourself

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.doyourself.ui.LoginScreen
import com.example.doyourself.ui.pages.MainScreen.MainScreen
import com.example.doyourself.ui.theme.DoYourSelfTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import androidx.navigation.compose.composable
import androidx.room.Room
import com.example.doyourself.data.local.db.AppDatabase
import com.example.doyourself.ui.pages.AccountScreen
import com.example.doyourself.ui.pages.MessagesScreen
import com.example.doyourself.ui.pages.createProcedure.CreateProcedureScreen
import com.example.doyourself.ui.pages.draftManager.DraftManagerScreen
import com.example.doyourself.ui.pages.previewProcedure.PreviewScreen
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.initialize



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        setContent {
            /*DoYourSelfTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }*/
            val navController = rememberNavController()
            val user by rememberFirebaseUser()

            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "procedure_db"
            ).build()

            val dao = db.procedureDao()

            Firebase.initialize(context = this)
            Firebase.appCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance(),
            )

            FirebaseApp.initializeApp(this)
            val providerFactory = DebugAppCheckProviderFactory.getInstance()
            FirebaseAppCheck.getInstance().installAppCheckProviderFactory(providerFactory)


            MaterialTheme {
                NavHost(
                    navController,
                    startDestination = if (user != null) "main" else "login"
                ) {
                    composable("login") {
                        LoginScreen {
                            // onLoginSuccess: do nothing, recomposition will re-trigger
                        }
                    }
                    composable("main") {
                        MainScreen(navController, dao)
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
                        DraftManagerScreen(
                            navController = navController,
                            procedureDao = dao
                        )
                    }
                    composable("preview/{draftId}") { backStackEntry ->
                        val draftId = backStackEntry.arguments?.getString("draftId") ?: return@composable
                        PreviewScreen(
                            navController = navController,
                            procedureDao = dao,
                            draftId = draftId
                        )
                    }

                    // Add other screens here (account, messages, etc.)
                }
            }

        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DoYourSelfTheme {
        Greeting("Android")
    }
}

@Composable
fun rememberFirebaseUser(): State<FirebaseUser?> {
    val firebaseAuth = remember { FirebaseAuth.getInstance() }
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