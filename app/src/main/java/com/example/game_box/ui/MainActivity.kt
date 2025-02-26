package com.example.game_box.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.game_box.databinding.ActivityMainBinding
import com.example.game_box.viewmodel.AuthViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflater le layout + binding
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // Observer l'état d'authentification
        authViewModel.isAuthenticated.observe(this) { isAuthenticated ->
            // Si on n’est plus authentifié, retour à SignIn
            if (!isAuthenticated) {
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }
        }

        // Déconnexion
        mBinding.SObutton.setOnClickListener {
            authViewModel.signOut()
        }
    }
}
