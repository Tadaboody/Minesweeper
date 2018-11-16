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
        setImageResource(R.drawable.hidden_tile)
        setOnClickListener {
            game.clickTile(item.i, item.j)
            val image = item.value.run {
                when (isRevealed) {
                    true -> when (isMine) {
                        true -> R.drawable.mine_tile
                        false -> R.mipmap.ic_launcher //TODO: dynamically change the image according to number
                    }
                    false -> when (isFlagged) {
                        true -> R.drawable.flag_tile
                        false -> R.drawable.hidden_tile
                    }
                }
            }
            setImageResource(image)
            Toast.makeText(context, "Clicked $item.i,$item.j", Toast.LENGTH_SHORT).show()
        }
    }
}

