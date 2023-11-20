package com.example.game_box

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.game_box.databinding.ActivityStartTicBinding
import kotlin.random.Random
import kotlin.random.nextInt

class StartTicActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityStartTicBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityStartTicBinding.inflate(layoutInflater)
        setContentView(mBinding.root)



        mBinding.playOff.setOnClickListener{
            createOfflineGame()
        }

        mBinding.playOn.setOnClickListener {
            createOnlineGame()
        }


    }
    fun createOnlineGame(){
        GameData.myID = "X"
        GameData.saveGameModel(
            GameModel(
                gameStatus = GameStatus.CREATED,
                gameId = Random.nextInt(1000..9999).toString()
            )
        )
        startGame()
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






