package com.keren.tomer.minesweeper

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.game_action_bar.*

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
        val model = ViewModelProviders.of(this, GameViewModelFactory(width, height, mines)).get(GameViewModel::class.java)
        model.winnState.observe(this, Observer {
            game_zoom.engine.zoomTo(1.0F, true)
            GameEndDialog.newInstance(it!!).show(supportFragmentManager, "game end dialog")
        })
        game_board.addGame(model)
        supportActionBar?.setup(model)
//        game = Game(width = width,height=height,amountOfMines = mines)

    }

    private fun ActionBar.setup(model: GameViewModel) {
        displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        setDisplayShowCustomEnabled(true)
        setCustomView(R.layout.game_action_bar)
        model.inputMode.observe(this@GameActivity, NonNullObserver {
            inputModeButton.setImageResource(when (it) {
                Game.InputMode.REVEALING -> R.mipmap.ic_launcher
                Game.InputMode.FLAGGING -> R.drawable.flag_tile
                else -> {
                    R.mipmap.ic_launcher
                }
            })
        })
        inputModeButton.setOnClickListener { model.toggleInputMode() }
        model.flagsLeft.observe(this@GameActivity, NonNullObserver {
            flagsRemainingText.text = it.toString()
        })
    }
}

fun Activity.restart() {
    startActivity(intent)
    finish()
}

class NonNullObserver<T : Any>(val onChangedNotNull: (T) -> Unit) : Observer<T> {
    override fun onChanged(t: T?) {
        if (t != null) onChangedNotNull(t)
    }
}
