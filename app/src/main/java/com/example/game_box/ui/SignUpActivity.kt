package com.example.game_box.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.game_box.R
import com.example.game_box.databinding.ActivitySignUpBinding
import com.example.game_box.utils.VibrateView
import com.example.game_box.viewmodel.AuthViewModel

class SignUpActivity : AppCompatActivity(), View.OnFocusChangeListener {
    private lateinit var mBinding: ActivitySignUpBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // Gestion du focus pour la validation en direct
        mBinding.emailEt.onFocusChangeListener = this
        mBinding.passET.onFocusChangeListener = this
        mBinding.confirmPassEt.onFocusChangeListener = this

        // Bouton "Sign Up"
        mBinding.button.setOnClickListener {
            val email = mBinding.emailEt.text.toString().trim()
            val password = mBinding.passET.text.toString().trim()
            val confirmPassword = mBinding.confirmPassEt.text.toString().trim()

            val isEmailValid = validateEmail()
            val isPasswordValid = validatePassword()
            val isConfirmPasswordValid = validateConfirmPassword()
            val isPasswordsMatch = validatePasswordAndConfirmPassword()

            if (isEmailValid && isPasswordValid && isConfirmPasswordValid && isPasswordsMatch) {
                authViewModel.signUp(email, password)
            }
        }

        // Redirection vers SignInActivity
        mBinding.textView.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        // Observer l'état d'authentification
        authViewModel.isAuthenticated.observe(this) { isAuthenticated ->
            if (isAuthenticated) {
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }
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

    private fun validateConfirmPassword(): Boolean {
        val confirmPassword = mBinding.confirmPassEt.text.toString()
        return when {
            confirmPassword.isEmpty() -> {
                showError(mBinding.confirmPasswordLayout, "Confirm Password is required")
                false
            }
            confirmPassword.length < 8 -> {
                showError(mBinding.confirmPasswordLayout, "Password must be at least 8 characters long")
                false
            }
            else -> {
                hideError(mBinding.confirmPasswordLayout)
                true
            }
        }
    }

    private fun validatePasswordAndConfirmPassword(): Boolean {
        val password = mBinding.passET.text.toString()
        val confirmPassword = mBinding.confirmPassEt.text.toString()
        return if (password != confirmPassword) {
            showError(mBinding.confirmPasswordLayout, "Passwords do not match")
            false
        } else {
            hideError(mBinding.confirmPasswordLayout)
            true
        }
    }

    private fun showError(layout: com.google.android.material.textfield.TextInputLayout, message: String) {
        layout.apply {
            isErrorEnabled = true
            error = message
            VibrateView.vibrate(this@SignUpActivity, this)
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
                R.id.confirmPassEt -> validatePasswordAndConfirmPassword()
            }
        }
    }
}
