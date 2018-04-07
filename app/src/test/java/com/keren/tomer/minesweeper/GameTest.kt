package com.keren.tomer.minesweeper

import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class MockGame(boardPath:String):Game()
{

    init {
        isFirstMove = false
    }
}

class GameTest {

    private var game : Game? = null
    @Before
    fun setUp() {
        game = MockGame("res/testGame")
    }

    @Test
    fun swapMode() {
    }

    @Test
    fun holdTile() {
    }

    @Test
    fun clickMine() {

    }
    @Test
    fun clickEmpty()
    {

    }

    @Test
    fun clickNumber()
    {

    }

    @Test
    fun revealTileNeighbors() {
    }
}