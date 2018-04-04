package utils

fun <T,K,C: Iterable<T>> C.cartesianProduct(other : Iterable<K>) : Set<Pair<T,K>>
{
    return this.flatMap {first  -> other.map { second ->  Pair(first,second)}  }.toSet()
}

fun <E> Collection<E>.choose(amount: Int): Collection<E> {
    return this.shuffled().slice(0..amount)
}

inline fun <reified T: Enum<T>> T.next(): T {
    val values = enumValues<T>()
    val nextOrdinal = (ordinal + 1) % values.size
    return values[nextOrdinal]
}