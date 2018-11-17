package com.keren.tomer.minesweeper

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.GridLayout.spec
import android.widget.ImageView
import android.widget.Toast
import utils.DoublyIndexedItem

fun GridLayout.addGame(game: Game) {
    rowCount = game.width
    columnCount = game.height
    game.board.flatten().forEach { item ->
        val params = GridLayout.LayoutParams(spec(item.i), spec(item.j))
        addView(createView(game, item), params)
    }
}

fun GridLayout.createView(game: Game, item: DoublyIndexedItem<Tile>): View {
    return ImageView(context).apply {
        setImageResource(R.drawable.hidden_tile)
        setOnLongClickListener {
            game.holdTile(item.i, item.j)
            updateBoard(game)
            true
        }
        setOnClickListener {
            game.clickTile(item.i, item.j)
            updateBoard(game)
            Toast.makeText(context, item.value.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}

fun ViewGroup.children(): List<View> = (0 until childCount).map { getChildAt(it) }

fun GridLayout.updateBoard(game: Game) {
    game.board.flatten().zip(children()).forEach { (item, view) ->
        val image = when (item.value.state) {
            Tile.State.HIDDEN -> R.drawable.hidden_tile
            Tile.State.FLAGGED -> R.drawable.flag_tile
            Tile.State.MINE -> R.drawable.mine_tile
            Tile.State.NUMBERED -> R.mipmap.ic_launcher
        }
        view as ImageView
        view.setImageResource(image)
    }
}

