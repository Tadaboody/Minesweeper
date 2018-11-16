package com.keren.tomer.minesweeper

import android.content.Context
import android.util.AttributeSet
import android.view.View
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
        setImageResource(R.mipmap.ic_launcher)
        setOnClickListener {
            Toast.makeText(context, "Clicked $item.i,$item.j", Toast.LENGTH_SHORT).show()
        }
    }
}

