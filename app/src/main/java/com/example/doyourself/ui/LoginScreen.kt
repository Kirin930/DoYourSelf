package com.example.doyourself.ui

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    val context = LocalContext.current
    val activity = context as Activity

    var loading by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.result
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { authResult ->
                    if (authResult.isSuccessful) {
                        // Save user to Firestore
                        val user = FirebaseAuth.getInstance().currentUser!!
                        val db = FirebaseFirestore.getInstance()
                        val userRef = db.collection("users").document(user.uid)

                        userRef.get().addOnSuccessListener { doc ->
                            if (!doc.exists()) {
                                userRef.set(
                                    mapOf(
                                        "uid" to user.uid,
                                        "name" to user.displayName,
                                        "email" to user.email,
                                        "photoUrl" to user.photoUrl.toString(),
                                        "createdAt" to System.currentTimeMillis()
                                    )
                                )
                            }
                            Toast.makeText(context, "Login OK", Toast.LENGTH_SHORT).show()
                            Log.d("Login", "User: ${user.displayName}")
                            onLoginSuccess()
                        }
                    } else {
                        loading = false
                    }
                }
        } catch (e: Exception) {
            loading = false
            e.printStackTrace()
        }
    }

    val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("151207932394-qkq5hm0336inqsetb189pqho8kce31pc.apps.googleusercontent.com") // 🔐 get this from Firebase project settings
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, signInOptions)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        if (loading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = {
                loading = true
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            }) {
                Text("Sign in with Google")
            }
        }
    }
}