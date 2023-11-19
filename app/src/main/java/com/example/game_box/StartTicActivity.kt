package com.example.game_box

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.game_box.databinding.ActivityStartTicBinding

class StartTicActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityStartTicBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityStartTicBinding.inflate(layoutInflater)
        setContentView(mBinding.root)



        mBinding.playOff.setOnClickListener{
            createOfflineGame()
        }





    }

    fun createOfflineGame(){
        GameData.saveGameModel(
            GameModel(
                gameStatus = GameStatus.JOINED
            )
        )
        startGame()
    }

    fun startGame(){
        startActivity(Intent(this,GameActivity::class.java))
    }

}






