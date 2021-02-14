package xyz.chunkstories.api.world

import xyz.chunkstories.api.graphics.structs.InterfaceBlock

typealias WorldDimensionSize = Int

/** Dimension of zero = infinite size */
class WorldDimensions(val x: WorldDimensionSize, val y: WorldDimensionSize, val z: WorldDimensionSize) : InterfaceBlock

enum class WorldSize
constructor(@JvmField val sizeInChunks: Int, val description: String) {
    TINY(32, "1x1km"),
    SMALL(64, "2x2km"),
    MEDIUM(128, "4x4km"),
    BIG(256, "8x8km"),
    LARGE(512, "16x16km"),
    VERYLARGE(1024, "32x32km"),

    /** Warning : this can be VERY ressource intensive as it will make a 4294km2 map,
    leading to enormous map sizes ( in the order of 10Gbs to 100Gbs ) when fully explored. */
    HUGE(2048, "64x64km");

    @JvmField val maskForChunksCoordinates: Int = sizeInChunks - 1
    @JvmField val bitlengthOfHorizontalChunksCoordinates: Int = (Math.log(sizeInChunks.toDouble()) / Math.log(2.0)).toInt()

    //TODO make those configurable as well ?
    @JvmField val heightInChunks = 32
    @JvmField val bitlengthOfVerticalChunksCoordinates = 5

    @JvmField val heightInBlocks = heightInChunks * 32
    @JvmField val squareSizeInBlocks = sizeInChunks * 32

    companion object {
        val allSizes: String
            get() {
                var sizes = ""
                for (s in WorldSize.values()) {
                    sizes = sizes + s.name + ", " + s.name + " "
                }
                return sizes
            }

        fun getWorldSize(name: String): WorldSize? {
            name.toUpperCase()
            for (s in WorldSize.values()) {
                if (s.name == name)
                    return s
            }
            return null
        }
    }
}