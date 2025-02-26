package com.example.game_box.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.game_box.databinding.ActivityDevNotesBinding

class DevNotesActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityDevNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDevNotesBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // TODO: Ajouter les fonctionnalités de Dev Notes
    }
}
