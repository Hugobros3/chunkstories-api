//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//
package xyz.chunkstories.api.math.random

import java.util.*

class SeededRandomNumberTranslator(seed: String) {
    var premadeRandom = IntArray(10000)
    fun getRandomChanceForChunk(cx: Int, cz: Int): Int {
        var pointer = cx * 100 + cz + cx * (53 - cz) + 7 * (cx % 47) + cz * cz * 5 + cx / 14 % 5 * (cz + 9)
        pointer %= 10000
        if (pointer < 0) pointer += 10000
        return premadeRandom[pointer]
    }

    fun getRandomChanceForChunkPlusModifier(cx: Int, cz: Int, i: Int): Int {
        var pointer = (cx * 100 + cz + cx * (53 - cz) + 7 * (cx % 47) + cz * cz * 5 + cx / 14 % 5 * (cz + 9) + i * 7 + i + cx * i + cz * i * 3
                - 47 * i * i * (cx / 4))
        pointer %= 10000
        if (pointer < 0) pointer += 10000
        return premadeRandom[pointer]
    }

    init {
        // Init p array based on see
        var lseed: Long = 0
        var z: Long = 1
        for (b in seed.toByteArray()) {
            lseed += b * z
            z *= 10
            z %= 1874818188L
        }
        val rnd = Random(lseed)
        for (i in 0..9999) {
            premadeRandom[i] = rnd.nextInt(1000)
        }
    }
}