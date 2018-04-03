package utils

fun <T,K,C: Iterable<T>> C.cartesianProduct(other : Iterable<K>) : Set<Pair<T,K>>
{
    return this.flatMap {first  -> other.map { second ->  Pair(first,second)}  }.toSet()
}