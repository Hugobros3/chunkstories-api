package xyz.chunkstories.api.math

import org.junit.Test
import xyz.chunkstories.api.math.MathUtils.abs_
import xyz.chunkstories.api.math.MathUtils.is_double
import xyz.chunkstories.api.math.MathUtils.is_float
import xyz.chunkstories.api.math.MathUtils.is_int
import xyz.chunkstories.api.math.MathUtils.rnd_uniformf

class TestMathFns {
    @Test
    fun testAbs() {
        assert(abs_(-5) == 5)
    }

    @Test
    /** Tests the horrible hack for type queries */
    fun testReifiedCrazyness() {
        assert(is_float<Float>())
        assert(is_double<Double>())
        assert(is_int<Int>())

        assert(!is_float<Int>())
        assert(!is_float<Double>())

        assert(!is_double<Int>())
        assert(!is_double<Float>())

        assert(!is_int<Float>())
        assert(!is_int<Double>())
    }

    @Test
    fun benchAbs() {
        val size = 1024 * 1024 * 32
        val bigassArray = FloatArray(size)
        val out = FloatArray(size)
        for (i in 0 until size)
            bigassArray[i] = rnd_uniformf() * 2048f - 1024f

        val runs = 8
        for(run in 0 until runs) {
            run {
                val start = System.currentTimeMillis()
                for (i in 0 until size) {
                    out[i] = Math.abs(bigassArray[i])
                }
                val end = System.currentTimeMillis()
                println("Math.abs abs took ${end - start}ms")
            }

            run {
                val start = System.currentTimeMillis()
                for (i in 0 until size) {
                    out[i] = abs_(bigassArray[i])
                }
                val end = System.currentTimeMillis()
                println("Reified abs took ${end - start}ms")
            }
        }
    }
}