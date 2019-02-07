package utils

import org.junit.Assert
import org.junit.Test

class IterToolsKtTest {

    @Test
    fun cartesianProduct() {
        assert(listOf(1, 2).cartesianProduct(listOf(1, 2)) == setOf(Pair(1, 1), Pair(1, 2), Pair(2, 2), Pair(2, 1)))
    }

    @Test
    fun choose() {
        val list = listOf(1, 2, 3)
        for (i in 0..list.size)
            Assert.assertEquals(i, list.choose(i).size)
    }
}