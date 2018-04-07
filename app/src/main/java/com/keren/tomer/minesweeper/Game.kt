package com.keren.tomer.minesweeper

import utils.DoublyIndexedItem
import utils.choose
import utils.next

import java.util.*

typealias IndexedTile = DoublyIndexedItem<Tile>

abstract class Game(val height: Int,val width : Int, val amountOfMines : Int)
{
    protected val board : List<List<IndexedTile>> = List(height,{i->List(width,{j-> IndexedTile(i,j,Tile()) }) })
    enum class EndState {WON,LOST,UNDECIDED}
    var winState = EndState.UNDECIDED
    var isFirstMove = true
    enum class InputMode {REVEALING,FLAGGING}
    var currentInputMode = InputMode.REVEALING

    private fun isWinnable(): Boolean {
        return true //TODO:Implemnt
    }

    private fun initGame(startingTile:IndexedTile) {
        do {
            cleanMines()
            plantMines(startingTile)
            updateNumbers()
        }while(!isWinnable())
    }

    private fun updateNumbers() {
        board.flatten().forEach{tile-> tile.value.numberOfMinedNeighbors = tile.neighbors(board).count { it.value.isMine }}
    }

    fun swapMode()
    {
        currentInputMode = currentInputMode.next()
    }

    fun holdTile(i: Int,j: Int)
    {
        swapMode()
        clickTile(i,j)
        swapMode()
    }

    fun clickTile(i:Int,j:Int)
    {
        val currentTile = board[i][j]
        if(isFirstMove)
        {
            initGame(currentTile)
            revealTile(currentTile)
        }else if(!currentTile.value.isRevealed) {
            when (currentInputMode) {
                InputMode.REVEALING -> revealTile(currentTile)
                InputMode.FLAGGING -> flagTile(currentTile)
            }
        }else
        {
            if(currentTile.value.isEmpty() || currentTile.isSafe())
            {
                revealTileNeighbors(currentTile)
            }
        }
    }

    private fun IndexedTile.isSafe() : Boolean {
        val expectedFlags = value.numberOfMinedNeighbors
        return neighbors(board).count { it.value.isFlagged } == expectedFlags
    }
    var minesLeft = amountOfMines
    private fun flagTile(hiddenTile: IndexedTile) {
        if(!hiddenTile.value.isRevealed) hiddenTile.value.toggleFlag()
        if(hiddenTile.value.isFlagged) minesLeft-- else minesLeft++ //flag added

    }

    private fun revealTile(dangerousTile : IndexedTile)
    {
        if(!dangerousTile.value.isRevealed) {
            if (dangerousTile.value.isFlagged) return
            if (dangerousTile.value.isMine) lose()
            dangerousTile.value.reveal()
        }else if(dangerousTile.value.isEmpty()) {
            revealTileNeighbors(dangerousTile)
        }
        if(winConditionDone()) win()
    }

    private fun win() {
        winState = EndState.WON
    }

    private fun winConditionDone(): Boolean = board.flatten().count { !it.value.isRevealed } == amountOfMines

    fun revealTileNeighbors(startingTile: IndexedTile)
    {
        val tileQueue : Queue<IndexedTile> = LinkedList()
        tileQueue.add(startingTile)
        while(tileQueue.isNotEmpty())
        {
            val currentTile = tileQueue.poll()
            currentTile.value.reveal()
            if(currentTile.value.isEmpty()) // spread
            {
                tileQueue.addAll(currentTile.neighbors(board).toSet().filter { it.value.isRevealed })
            }
        }
    }


    private fun lose() {
        winState = EndState.LOST
    }

    private fun plantMines(startingTile:IndexedTile) {
        val possibleMines = board.flatten().toSet() - (startingTile.neighbors(board) + startingTile)
        possibleMines.choose(amountOfMines).forEach { tile -> tile.value.plantMine() }
    }
    private fun cleanMines() = board.flatten().filter { it.value.isMine }.forEach{tile -> tile.value.removeMine() }
}



