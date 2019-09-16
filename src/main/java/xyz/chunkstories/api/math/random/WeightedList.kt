package xyz.chunkstories.api.math.random

import java.util.*

class WeightedSet<T : Any>(initialSet: Collection<Pair<Double, T>>) {
    val all: Set<T> = initialSet.map { it.second }.toSet()

    private val list = initialSet.toList()
    private val totalWeight = initialSet.sumByDouble { it.first }

    fun random(): T = random(Random(System.currentTimeMillis()))

    fun random(rng: Random): T {
        val p = rng.nextDouble() * totalWeight
        var acc = 0.0
        for ((weight, item) in list) {
            acc += weight
            if (acc >= p)
                return item
        }

        return all.random()
    }
}