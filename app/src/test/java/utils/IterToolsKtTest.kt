package utils

import org.junit.Test

import org.junit.Assert.*

class IterToolsKtTest {

    @Test
    fun cartesianProduct() {
        assert(listOf(1,2).cartesianProduct(listOf(1,2)) == setOf(Pair(1,1), Pair(1,2), Pair(2,2), Pair(2,1)))
    }
}