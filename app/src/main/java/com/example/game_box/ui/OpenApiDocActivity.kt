package com.example.game_box.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.game_box.databinding.ActivityOpenApiDocBinding

class OpenApiDocActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityOpenApiDocBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityOpenApiDocBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }
}
