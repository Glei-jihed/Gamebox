package com.example.game_box.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels // <-- Import crucial
import androidx.appcompat.app.AppCompatActivity
import com.example.game_box.databinding.ActivityMainBinding
import com.example.game_box.viewmodel.AuthViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding

    // On utilise `viewModels()` pour récupérer le ViewModel
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // Observer l'état d'authentification
        authViewModel.isAuthenticated.observe(this) { isAuthenticated ->
            if (!isAuthenticated) {
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }
        }

        // Bouton "Log Out"
        mBinding.SObutton.setOnClickListener {
            authViewModel.signOut()
        }
    }
}
