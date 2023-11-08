package com.example.game_box

import android.os.Bundle
import android.util.Patterns

import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.game_box.databinding.ActivitySignUpBinding


class SignUpActivity: AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {

    private lateinit var mBinding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignUpBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)
        mBinding.emailEt.onFocusChangeListener = this
        mBinding.passET.onFocusChangeListener = this
        mBinding.confirmPassEt.onFocusChangeListener = this
        setContentView(R.layout.activity_sign_up)
    }
    private fun validateEmail():Boolean{
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
    private fun validatePassword():Boolean{
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

    private fun validateConfirmpassword():Boolean {
        var errorMessage:String? = null
        val value: String = mBinding.confirmPassEt.text.toString()
        if(value.isEmpty()){

            errorMessage = " Confirm Password is required"

        } else if (value.length < 6){

            errorMessage = "Confirm Password must be 6 characters Long !"
        }
        if(errorMessage != null){

            mBinding.confirmPasswordLayout.apply {
                isErrorEnabled = true
                error = errorMessage
            }

        }

        return errorMessage == null
    }

    private fun validatePasswordAndConfirmPassword():Boolean{
        var errorMessage: String? = null
        val password: String? = mBinding.passET.text.toString()
        val confirmPassword: String? = mBinding.confirmPassEt.text.toString()

        if(password!=confirmPassword){
            errorMessage = "Confirm password doesn't match with password"
        }
        if(errorMessage != null){

            mBinding.confirmPasswordLayout.apply {
                isErrorEnabled = true
                error = errorMessage
            }

        }
        return errorMessage ==null
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }

}