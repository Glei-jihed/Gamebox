package com.example.game_box.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthRepository {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signIn(email: String, password: String, callback: (Boolean) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }

    fun signUp(email: String, password: String, callback: (Boolean) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun signInWithGoogle(idToken: String, callback: (Boolean) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }
}
