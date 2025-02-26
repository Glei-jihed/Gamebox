package com.example.game_box.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.game_box.R
import com.example.game_box.databinding.ActivitySignInBinding
import com.example.game_box.utils.VibrateView
import com.example.game_box.viewmodel.AuthViewModel

class SignInActivity : AppCompatActivity(), View.OnFocusChangeListener {
    private lateinit var mBinding: ActivitySignInBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // Gestion du focus pour validation en direct
        mBinding.emailEt.onFocusChangeListener = this
        mBinding.passET.onFocusChangeListener = this

        // Bouton "Sign In"
        mBinding.button.setOnClickListener {
            val email = mBinding.emailEt.text.toString().trim()
            val password = mBinding.passET.text.toString().trim()

            val isEmailValid = validateEmail()
            val isPasswordValid = validatePassword()

            if (isEmailValid && isPasswordValid) {
                authViewModel.signIn(email, password)
            }
        }

        // Observer l'état d'authentification
        authViewModel.isAuthenticated.observe(this) { isAuthenticated ->
            if (isAuthenticated) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        // Redirection vers SignUpActivity
        mBinding.textView.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun validateEmail(): Boolean {
        val email = mBinding.emailEt.text.toString()
        return when {
            email.isEmpty() -> {
                showError(mBinding.emailLayout, "Email is required")
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showError(mBinding.emailLayout, "Invalid email address")
                false
            }
            else -> {
                hideError(mBinding.emailLayout)
                true
            }
        }
    }

    private fun validatePassword(): Boolean {
        val password = mBinding.passET.text.toString()
        return when {
            password.isEmpty() -> {
                showError(mBinding.passwordLayout, "Password is required")
                false
            }
            password.length < 8 -> {
                showError(mBinding.passwordLayout, "Password must be at least 8 characters long")
                false
            }
            else -> {
                hideError(mBinding.passwordLayout)
                true
            }
        }
    }

    private fun showError(layout: com.google.android.material.textfield.TextInputLayout, message: String) {
        layout.apply {
            isErrorEnabled = true
            error = message
            VibrateView.vibrate(this@SignInActivity, this)
        }
    }

    private fun hideError(layout: com.google.android.material.textfield.TextInputLayout) {
        layout.apply {
            isErrorEnabled = false
            error = null
        }
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null && !hasFocus) {
            when (view.id) {
                R.id.emailEt -> validateEmail()
                R.id.passET -> validatePassword()
            }
        }
    }
}
