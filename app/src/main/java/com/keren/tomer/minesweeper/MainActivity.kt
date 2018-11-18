package com.keren.tomer.minesweeper

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        easyB.setOnClickListener { createBoardActivity(8, 8, 10) }
        mediumB.setOnClickListener { createBoardActivity(16, 16, 40) }
        hardB.setOnClickListener { createBoardActivity(30, 16, 99) }
    }

    private fun EditText.toInt() = text.toString().toInt()

    private fun createBoardActivity(height: Int, width: Int, amountOfMines: Int) {
        val intent = Intent(this, GameActivity::class.java).apply {
            GameActivity.run {
                putExtra(INTENT_MINES, amountOfMines)
                putExtra(INTENT_WIDTH, width)
                putExtra(INTENT_HEIGHT, height)
            }
        }
        startActivity(intent)
    }
}

