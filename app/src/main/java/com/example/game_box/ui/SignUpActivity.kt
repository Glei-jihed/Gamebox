package com.example.game_box.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels  // <-- Import crucial
import androidx.appcompat.app.AppCompatActivity
import com.example.game_box.databinding.ActivitySignUpBinding
import com.example.game_box.viewmodel.AuthViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivitySignUpBinding

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // Bouton "Sign Up"
        mBinding.button.setOnClickListener {
            val email = mBinding.emailEt.text.toString()
            val password = mBinding.passET.text.toString()
            val confirmPassword = mBinding.confirmPassEt.text.toString()

            if (password == confirmPassword) {
                authViewModel.signUp(email, password)
            } else {
                mBinding.confirmPasswordLayout.error = "Passwords do not match"
            }
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
