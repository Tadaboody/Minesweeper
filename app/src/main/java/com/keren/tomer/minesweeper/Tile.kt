package com.keren.tomer.minesweeper


open class Tile {
    enum class State {
        HIDDEN,
        FLAGGED,
        MINE,
        NUMBERED
    }

    var isMine = false
    fun plantMine() {
        isMine = true
    }

    fun reveal() {
        if (isRevealed) return
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
    val state: State
        get() = when (isRevealed) {
            true -> when (isMine) {
                true -> State.MINE
                false -> State.NUMBERED
            }
            false -> when (isFlagged) {
                true -> State.FLAGGED
                false -> State.HIDDEN
            }
        }

    override fun toString(): String {
        return when (state) {
            State.MINE -> "MINE"
            State.HIDDEN -> "HIDDEN"
            State.FLAGGED -> "FLAGGED"
            State.NUMBERED -> numberOfMinedNeighbors.toString()
        }
    }
}