package com.example.game_box.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.game_box.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _isAuthenticated = MutableLiveData<Boolean>()
    val isAuthenticated: LiveData<Boolean> get() = _isAuthenticated

    private val _currentUser = MutableLiveData<FirebaseUser?>()
    val currentUser: LiveData<FirebaseUser?> get() = _currentUser

    fun signIn(email: String, password: String) {
        authRepository.signIn(email, password) { success ->
            _isAuthenticated.value = success
            _currentUser.value = firebaseAuth.currentUser // 🔹 Stocke l'utilisateur Firebase actuel
        }
    }

    fun signUp(email: String, password: String) {
        authRepository.signUp(email, password) { success ->
            _isAuthenticated.value = success
            _currentUser.value = firebaseAuth.currentUser // 🔹 Stocke l'utilisateur Firebase actuel
        }
    }

    fun signOut() {
        authRepository.signOut()
        _isAuthenticated.value = false
        _currentUser.value = null
    }

    fun signInWithGoogle(idToken: String) {
        authRepository.signInWithGoogle(idToken) { success ->
            _isAuthenticated.value = success
            _currentUser.value = firebaseAuth.currentUser // 🔹 Stocke l'utilisateur Firebase actuel
        }
    }
}
