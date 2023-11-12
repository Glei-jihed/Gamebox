package com.example.game_box

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.game_box.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        mBinding.textView.setOnClickListener {
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }


        mBinding.button.setOnClickListener {

            val email = mBinding.emailEt.text.toString()
            val password = mBinding.passET.text.toString()


            if (email.isNotEmpty() && password.isNotEmpty()) {

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                        if(it.isSuccessful){
                            val intent = Intent(this,MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this,it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }


            } else {
                Toast.makeText(this, "Enter your data please !", Toast.LENGTH_SHORT).show()
            }
        }






    }
}