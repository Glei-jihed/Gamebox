package com.example.game_box.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.game_box.databinding.ActivityOpenApiDocBinding

class OpenApiDocActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityOpenApiDocBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityOpenApiDocBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // Configuration de la Toolbar avec flèche "up"
        val toolbar = mBinding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Open API Doc"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> { finish(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
