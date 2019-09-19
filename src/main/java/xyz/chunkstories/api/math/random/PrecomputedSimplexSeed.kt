package xyz.chunkstories.api.math.random

import xyz.chunkstories.api.graphics.structs.InterfaceBlock
import java.util.ArrayList

/** Used to feed the GLSL side with the same simplex noise generation as the JVM side ! */
class PrecomputedSimplexSeed(seed: String) : InterfaceBlock {
    val perm = IntArray(512)

    constructor() : this("${System.currentTimeMillis()}")

    init {
        val p = IntArray(256)
        val temp = ArrayList<Int>()
        for (i in 0..255) {
            temp.add(i)
        }

        val seedBytes = seed.toByteArray()
        for (i in 0..255) {
            val select = seedBytes[i % seedBytes.size].toInt()
            if (select < temp.size) {
                p[i] = temp[select]
                temp.removeAt(select)
            } else {
                p[i] = temp[0]
                temp.removeAt(0)
            }
        }
        // Init perm arrays
        for (i in 0..511) {
            perm[i] = p[i and 255]
        }
    }
}