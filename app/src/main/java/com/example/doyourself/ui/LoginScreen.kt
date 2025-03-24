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

@Composable
fun LoginScreen() {
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
                            activity.recreate() // Restart MainActivity to show HomeScreen
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
        .requestIdToken("1:151207932394:android:05116fae874becd6cf8293") // 🔐 get this from Firebase project settings
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
