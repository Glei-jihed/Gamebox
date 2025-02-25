package com.example.game_box.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels  // <-- Import crucial
import androidx.appcompat.app.AppCompatActivity
import com.example.game_box.databinding.ActivitySignInBinding
import com.example.game_box.viewmodel.AuthViewModel

class SignInActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivitySignInBinding

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // Bouton "Sign In"
        mBinding.button.setOnClickListener {
            val email = mBinding.emailEt.text.toString()
            val password = mBinding.passET.text.toString()
            authViewModel.signIn(email, password)
        }

        // Observer l'état d'authentification
        authViewModel.isAuthenticated.observe(this) { isAuthenticated ->
            if (isAuthenticated) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}
