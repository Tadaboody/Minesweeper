package com.keren.tomer.minesweeper

import com.keren.tomer.minesweeper.textGame.TextGame
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

val gameString = """
    |0000000
    |0000100
    |0001000
    |1000000
    |0000000
    |0000000
    |0000000
    |0000000""".trimMargin()

class GameTest {
    class MockGame(private val gameString: String, height: Int, width: Int, amountOfMines: Int) : TextGame(height, width, amountOfMines) {
        companion object Factory {
            fun createMock(gameString: String): MockGame {
                val lines = gameString.lines()
                val height = lines.size
                val width = lines.first().length
                val amountOfMines = gameString.count { it == '1' }
                return MockGame(gameString, height, width, amountOfMines)
            }
        }

        init {
            isFirstMove = false
            initGame(board[0][0])
        }

        override fun plantMines(startingTile: IndexedTile) {
            for ((lineNum, line) in gameString.lines().withIndex()) {
                line.filter { it == '1' }.withIndex().forEachIndexed { charNum, _ ->
                    board[lineNum][charNum].value.plantMine()
                }
            }
        }
    }

    private lateinit var game: TextGame

    @Before
    fun setUp() {
        game = MockGame.createMock(gameString)
    }

    @After
    fun checkResult() = game.render()

    @Test
    fun testMock() {
        assertEquals(true, game.board[3][0].value.isMine)
    }

    @Test
    fun winByFlagging() {
        game.holdTile(1, 4)
        game.holdTile(2, 3)
        game.holdTile(3, 0)
        assertEquals(Game.EndState.WON, game.winState)
    }

    @Test
    fun winByRevealing() {
        game.board.flatten().filter { !it.value.isMine }.forEach { game.run { it.reveal() } }
        assertEquals(Game.EndState.WON, game.winState)
    }

    @Test
    fun holdTile() {
        val holdImportantTile = { game.holdTile(0, 0) }
        val importantTile = game.board[0][0]
        holdImportantTile()
        assertEquals(true, importantTile.value.isFlagged)
        game.clickTile(0, 0)
        assertEquals(false, importantTile.value.isRevealed) // flagged tiles shouldn't be revealed when clicked
        holdImportantTile() // remove flag
        game.swapMode() // holding reveals now
        holdImportantTile()
        assertEquals(true, importantTile.value.isRevealed)
        game.swapMode() // holding flags now
        holdImportantTile() // tile is revealed and shouldn't be flagged
        assertEquals(false, importantTile.value.isFlagged)
    }

    @Test
    fun clickMine() {
        val i = 1
        val j = 4
        game.holdTile(i, j)
        game.clickTile(i, j)//Flag and click
        assertEquals(Game.EndState.UNDECIDED, game.winState)
        game.holdTile(i, j)//Unflag
        game.clickTile(i, j)
        assertEquals(Game.EndState.LOST, game.winState)
    }

    @Test
    fun clickEmpty() {
        val i = 6
        val j = 6
        game.clickTile(i, j)
        assertEquals(true, game.board[i][j].neighbors(game.board).all { it.value.isRevealed })
    }

    @Test
    fun clickNumber() {
        //Tests the functionality of clicking a numbered (non zero) tile
        val importantTile = game.board[1][3]
        val clickImportantTile = { game.clickTile(1, 3) }
        clickImportantTile()
        val importantNeighbors = importantTile.neighbors(game.board) // two mines : [(1,4),(2,3)]
        assertEquals(false, importantNeighbors.any { it.value.isRevealed }) // no neighbors should be revealed
        game.holdTile(1, 4)
        clickImportantTile()
        assertEquals(true, importantNeighbors.filter { !it.value.isFlagged }.all { !it.value.isRevealed }) // only one neighbor mine is flagged, no neighbor should be revealed
        game.holdTile(2, 3)
        clickImportantTile()
        val (flagged, notFlagged) = importantNeighbors.partition { it.value.isFlagged } // enough neighbors are flagged, all neighbors should be revealed
        assertEquals(true, flagged.all { !it.value.isRevealed })
        assertEquals(true, notFlagged.all { it.value.isRevealed })
    }
    @Test
    fun correctAmountOfMines() {
        val expectedAmount = 10
        val game = Game(10, 10, expectedAmount)
        game.clickTile(0, 0)
        val actualAmount = game.board.flatten().count { it.value.isMine }
        assertEquals(expectedAmount, actualAmount)
    }

}
