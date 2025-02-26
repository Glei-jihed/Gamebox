package com.example.game_box.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.game_box.repository.AuthRepository

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    private val _isAuthenticated = MutableLiveData<Boolean>()
    val isAuthenticated: LiveData<Boolean> get() = _isAuthenticated

    fun signIn(email: String, password: String) {
        authRepository.signIn(email, password) { success ->
            _isAuthenticated.value = success
        }
    }

    fun signUp(email: String, password: String) {
        authRepository.signUp(email, password) { success ->
            _isAuthenticated.value = success
        }
    }

    fun signOut() {
        authRepository.signOut()
        _isAuthenticated.value = false
    }

    fun signInWithGoogle(idToken: String) {
        authRepository.signInWithGoogle(idToken) { success ->
            _isAuthenticated.value = success
        }
    }
}
