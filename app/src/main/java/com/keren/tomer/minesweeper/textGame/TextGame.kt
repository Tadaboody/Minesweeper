package com.keren.tomer.minesweeper.textGame

import com.keren.tomer.minesweeper.Game



class TextGame(height:Int,width:Int,amountOfMines:Int) : Game(height,width,amountOfMines)
{
    private fun render()
    {
        this.board.forEach{row ->
            row.forEach {
                tile ->
                if (!tile.value.isRevealed)
                {
                    print("X")
                }else
                {
                    if(tile.value.isMine)
                    {
                        print("*")
                    }
                    print(tile.value.numberOfMinedNeighbors)
                }
                print(",")
            }
            println()
        }
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