package com.example.doyourself.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseUser
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(user: FirebaseUser) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(user.photoUrl),
            contentDescription = "User photo",
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Welcome, ${user.displayName}", style = MaterialTheme.typography.headlineSmall)
        Text(user.email ?: "", textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            FirebaseAuth.getInstance().signOut()
            // Activity restart to go back to login
            (user as? android.app.Activity)?.recreate()
        }) {
            Text("Logout")
        }
    }
}
