package com.keren.tomer.minesweeper

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {

    companion object {
        const val INTENT_WIDTH = "WIDTH"
        const val INTENT_HEIGHT = "HEIGHT"
        const val INTENT_MINES = "MINES"
        const val TAG = "GameActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
//        toasty_button.setOnClickListener { Toast.makeText(this, "Hello!", Toast.LENGTH_LONG).show() }
        //        gameGrid.adapter = GameAdapter(this)
        val width = intent.getIntExtra(INTENT_WIDTH, 15)
        val height = intent.getIntExtra(INTENT_HEIGHT, 15)
        val mines = intent.getIntExtra(INTENT_MINES, 15)
        val game = Game(height, width, mines)
        game.endCallback = {
            game_zoom.engine.zoomTo(1.0F, true)
            when (it) {//TODO
                Game.EndState.WON -> game.revealBoard()
                Game.EndState.LOST -> ""
                Game.EndState.UNDECIDED -> Log.e(TAG, "INVALID_STATE")
            }
        }
        game_board.addGame(game)
//        game = Game(width = width,height=height,amountOfMines = mines)

    }
}

