package com.keren.tomer.minesweeper.textGame

import com.keren.tomer.minesweeper.Game


open class TextGame(height: Int, width: Int, amountOfMines: Int) : Game(height, width, amountOfMines,currentInputMode = InputMode.REVEALING) {
    fun render(hide: Boolean = true) {
        this.board.forEachIndexed { row_i, row ->
            print("$row_i:")
            row.forEach { iTile ->
                val tile = iTile.value
                if (hide && !tile.isRevealed) {
                    if (tile.isFlagged) {
                        print("F")
                    } else {
                        print("?")
                    }
                } else {
                    if (tile.isMine) {
                        print("*")
                    } else {
                        print(tile.numberOfMinedNeighbors)
                    }
                }
                print(",")
            }
            println()
        }
        println()
    }

    fun runGame() {
        render()
        while (winState == EndState.UNDECIDED) {
            print("Enter row:")
            val row = readLine()!!.toInt()
            print("Enter col:")
            val col = readLine()!!.toInt()
            clickTile(row, col)
            render()
        }
        when (winState) {
            EndState.WON -> println("conglaturations!")
            EndState.LOST -> println("OhNo")
            else -> {
            }
        }
    }
}

fun main(args: Array<String>) {
    val a = TextGame(8, 8, 1)
    a.runGame()
}