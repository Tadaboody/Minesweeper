package com.keren.tomer.minesweeper

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.keren.tomer.minesweeper.R.id.game_board
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
//        toasty_button.setOnClickListener { Toast.makeText(this, "Hello!", Toast.LENGTH_LONG).show() }
        //        gameGrid.adapter = GameAdapter(this)
        val width = intent.getIntExtra("WIDTH", 15)
        val height = intent.getIntExtra("HEIGHT", 15)
        val mines = intent.getIntExtra("MINES", 15)
        val game = Game(height,width, mines)
        game_board.addGame(game)
//        game = Game(width = width,height=height,amountOfMines = mines)

    }
}

