package com.example.game_box.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.game_box.databinding.ActivityMainBinding
import com.example.game_box.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // Vérifier l'état d'authentification
        authViewModel.isAuthenticated.observe(this) { isAuthenticated: Boolean ->
            if (!isAuthenticated) {
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }
        }

        authViewModel.currentUser.observe(this) { user: FirebaseUser? ->
            if (user != null) {
                val userId: String = user.uid
                Log.d("MainActivity", "Utilisateur connecté : $userId")
                mBinding.cardDevNotes.setOnClickListener {
                    val intent = Intent(this, DevNotesActivity::class.java)
                    intent.putExtra("USER_ID", userId)
                    startActivity(intent)
                }
            } else {
                Log.e("MainActivity", "Erreur : Aucun utilisateur Firebase connecté.")
            }
        }

        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val userId: String = user.uid
            Log.d("MainActivity", "Utilisateur connecté: $userId")
            mBinding.cardDevNotes.setOnClickListener {
                val intent = Intent(this, DevNotesActivity::class.java)
                intent.putExtra("USER_ID", user.uid)
                startActivity(intent)
            }

        } else {
            Log.e("MainActivity", "Utilisateur non connecté !")
        }

        mBinding.cardOpenApi.setOnClickListener {
            startActivity(Intent(this, OpenApiDocActivity::class.java))
        }

        mBinding.btnLogout.setOnClickListener {
            authViewModel.signOut()
        }
    }
}
