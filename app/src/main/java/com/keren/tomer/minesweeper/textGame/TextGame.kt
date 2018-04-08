package com.keren.tomer.minesweeper.textGame

import com.keren.tomer.minesweeper.Game


open class TextGame(height: Int, width: Int, amountOfMines: Int) : Game(height, width, amountOfMines) {
    fun render(hide: Boolean = true)
    {
        this.board.forEach{row ->
            row.forEach { iTile ->
                val tile = iTile.value
                if (hide && !tile.isRevealed)
                {
                    if (tile.isFlagged) {
                        print("F")
                    } else {
                        print("X")
                    }
                }else
                {
                    if (tile.isMine)
                    {
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
    fun runGame()
    {
        while(winState == EndState.UNDECIDED)
        {
            clickTile(readLine()!!.toInt(), readLine()!!.toInt())
            render()
        }
        when(winState)
        {
            EndState.WON -> println("conglaturations!")
            EndState.LOST -> println("OhNo")
            else -> {
            }
        }
    }
}

fun main(args: Array<String>) {
    val a = TextGame(8,8,1)
    a.runGame()
}