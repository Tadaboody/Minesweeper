package com.keren.tomer.minesweeper



open class Tile {
    var isMine = false
    fun plantMine() {
        isMine = true
    }

    fun reveal() {
        if(isRevealed) return
       isRevealed = true
    }
    fun toggleFlag() {
        isFlagged = !isFlagged
    }

    fun removeMine() {
        isMine = false
    }
    fun isEmpty() = !isMine && numberOfMinedNeighbors == 0

    var numberOfMinedNeighbors = 0
    var isFlagged = false
    var isRevealed = false
}