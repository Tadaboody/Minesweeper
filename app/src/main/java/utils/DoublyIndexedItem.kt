package utils

data class DoublyIndexedItem<T>(val i:Int,val j:Int,val value:T)
{
    val offsets = arrayListOf(0, 1, -1).cartesianProduct(arrayListOf(0, 1, -1)).minus(setOf(Pair(0, 0)))
    fun neighbors(board: List<List<DoublyIndexedItem<T>>>): List<DoublyIndexedItem<T>> {

        return offsets.filter { board.inRange(i + it.first, j + it.second) }.map {

            board[i + it.first][j + it.second]
        }
    }

    fun <T> Collection<Collection<T>>.inRange(i: Int, j: Int) = i in 0..(size - 1) && j in 0 until first().size
}