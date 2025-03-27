package com.example.doyourself

import android.os.Bundle
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
import com.example.doyourself.ui.MainScreen
import com.example.doyourself.ui.theme.DoYourSelfTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import androidx.navigation.compose.composable
import com.example.doyourself.ui.pages.AccountScreen
import com.example.doyourself.ui.pages.MessagesScreen
import com.example.doyourself.ui.pages.CreateProcedureScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

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
                        MainScreen(navController)
                    }
                    composable("create") {
                        CreateProcedureScreen(navController)
                    }
                    composable("account") {
                        AccountScreen()
                    }
                    composable("messages") {
                        MessagesScreen()
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