package com.keren.tomer.minesweeper

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class GameViewModel(height: Int, width: Int, amountOfMines: Int) : ViewModel() {
    val game: Game = UnMutatingGame(height, width, amountOfMines).apply { endCallback = { winnState.value = it } }
    val height = game.height
    val width = game.width
    val board = game.board.flatten()
    val winnState: MutableLiveData<Game.EndState> = MutableLiveData()
    val flagsLeft: MutableLiveData<Int> = MutableLiveData()
    val tiles: List<MutableLiveData<Tile.State>> = board.map { MutableLiveData<Tile.State>().apply { value = it.value.state } }
    val gameState = MutableLiveData<Game.EndState>()
    private fun flagsLeft_() = game.amountOfMines - board.count { it.value.isFlagged }
    fun holdTile(i: Int, j: Int) {
        game.holdTile(i, j)
        updateData()
    }

    private fun updateData() {
        flagsLeft.value = flagsLeft_()
        gameState.value = game.winState
    }

    fun clickTile(i: Int, j: Int) {
        game.clickTile(i, j)
        updateData()
    }

}

class GameViewModelFactory(private val height: Int, private val width: Int, private val amountOfMines: Int) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return GameViewModel(height, width, amountOfMines) as T
    }
}


