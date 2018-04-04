package utils

data class DoublyIndexedItem<T>(val i:Int,val j:Int,val value:T)
{
    fun neighbors(board: List<List<DoublyIndexedItem<T>>>): List<DoublyIndexedItem<T>> {
        val offsets = arrayListOf(1,-1)
        return offsets.cartesianProduct(offsets).map {board[i+it.first][j+it.second]}
    }
}