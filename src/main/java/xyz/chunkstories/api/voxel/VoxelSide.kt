//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.voxel

/**
 * Conventions for space in Chunk Stories
 */
enum class VoxelSide private constructor(val dx: Int, val dy: Int, val dz: Int) {

    LEFT(-1, 0, 0),
    FRONT(0, 0, 1),
    RIGHT(1, 0, 0),
    BACK(0, 0, -1),
    TOP(0, 1, 0),
    BOTTOM(0, -1, 0);

    val oppositeSide: VoxelSide
        get() = oppsiteSide[this.ordinal]

    enum class Corners {
        TOP_FRONT_RIGHT, TOP_FRONT_LEFT, TOP_BACK_RIGHT, TOP_BACK_LEFT,
        BOTTOM_FRONT_RIGHT, BOTTOM_FRONT_LEFT, BOTTOM_BACK_RIGHT, BOTTOM_BACK_LEFT
    }

    companion object {

        private val oppsiteSide = arrayOf(RIGHT, BACK, LEFT, FRONT, BOTTOM, TOP)
    }
}
