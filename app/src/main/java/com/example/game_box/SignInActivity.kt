package com.example.game_box

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.game_box.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity(), View.OnFocusChangeListener, View.OnKeyListener {
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

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
         fun validateEmail():Boolean{
            var errorMessage:String? = null
            val value: String = mBinding.emailEt.text.toString()
            if(value.isEmpty()){

                errorMessage = "Email is required"

            } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()){

                errorMessage = "Email adress is invalid !"
            }
            if(errorMessage != null){

                mBinding.emailLayout.apply {
                    isErrorEnabled = true
                    error = errorMessage
                }

            }
            return errorMessage == null

        }
        fun validatePassword():Boolean{
            var errorMessage:String? = null
            val value: String = mBinding.passET.text.toString()
            if(value.isEmpty()){

                errorMessage = "Password is required"

            } else if (value.length < 6){

                errorMessage = "Password must be 6 characters Long !"
            }

            if(errorMessage != null){

                mBinding.passwordLayout.apply {
                    isErrorEnabled = true
                    error = errorMessage
                }

            }

            return errorMessage == null
        }
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }
}