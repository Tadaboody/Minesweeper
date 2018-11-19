package com.keren.tomer.minesweeper

import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.GridLayout.spec
import android.widget.ImageView
import utils.DoublyIndexedItem

/**
 * Adds a Minesweeper game to an existing GridLayout for rendering
 * @param game The minesweeper instance to be added
 */
fun GridLayout.addGame(viewModel: GameViewModel) {
    rowCount = viewModel.height
    columnCount = viewModel.width
    viewModel.board.forEach { item ->
        val params = GridLayout.LayoutParams(spec(item.i), spec(item.j))
        addView(createView(viewModel, item), params)
    }
}

/**
 * Creates a View to represent a viewModel tile
 * @param viewModel The viewModel the tile belongs to
 * @param item The IndexedTile that the View represent
 * @return A View representing item
 */
fun GridLayout.createView(viewModel: GameViewModel, item: DoublyIndexedItem<Tile>): View {
    return ImageView(context).apply {
        setImageResource(R.drawable.hidden_tile)
        setOnLongClickListener {
            viewModel.holdTile(item.i, item.j)
            updateBoard(viewModel.game)
            true
        }
        setOnClickListener {
            viewModel.clickTile(item.i, item.j)
            updateBoard(viewModel.game)
        }
    }
}

/**
 * Returns all of a ViewGroup's children
 */
fun ViewGroup.children(): List<View> = (0 until childCount).map { getChildAt(it) }

/**
 * Update every view of the GridLayout according to the gamestate
 * @receiver The gridlayout being updated
 * @param game The game supplying the state
 */
fun GridLayout.updateBoard(game: Game) {
    game.board.flatten().zip(children()).forEach { (item, view) ->
        val image = when (item.value.state) {
            Tile.State.HIDDEN -> R.drawable.hidden_tile
            Tile.State.FLAGGED -> R.drawable.flag_tile
            Tile.State.MINE -> R.drawable.ic_mine
            Tile.State.NUMBERED -> numberedTile(item.value.numberOfMinedNeighbors)
        }
        view as ImageView
        view.setImageResource(image)
    }
}

/**
 * @param number The number the tile should represent
 * @return The resource for the image representing the tile
 */
fun numberedTile(number: Int): Int = when (number) {
    0 -> R.drawable.ic_minesweeper_0
    1 -> R.drawable.ic_minesweeper_1
    2 -> R.drawable.ic_minesweeper_2
    3 -> R.drawable.ic_minesweeper_3
    4 -> R.drawable.ic_minesweeper_4
    5 -> R.drawable.ic_minesweeper_5
    6 -> R.drawable.ic_minesweeper_6
    7 -> R.drawable.ic_minesweeper_7
    8 -> R.drawable.ic_minesweeper_8
    else -> {
        R.mipmap.ic_launcher
    }
}

