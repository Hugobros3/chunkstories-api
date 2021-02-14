//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.util.compatibility

import xyz.chunkstories.api.block.BlockSide

// Vanilla mc sides (stairs)
// 1 = cs_RIGHT / mc_WEST | 3
// 0 = cs_LEFT / mc_EAST | 0 X 1
// 2 = cs_FRONT / mc_SOUTH | 2
// 3 = cs_BACK / mc_NORTH |

/**
 * Returns the Chunk Stories side from the minecraft metadata of the following
 * objects, no top/bottom direction allowed
 */
fun getSideMcStairsChestFurnace(mcSide: Int): BlockSide {
    when (mcSide) {
        2 -> return BlockSide.BACK
        3 -> return BlockSide.FRONT
        4 -> return BlockSide.RIGHT
        5 -> return BlockSide.LEFT
    }

    return BlockSide.FRONT
}

/**
 * Returns the Chunk Stories side from the minecraft metadata of the following
 * objects, no top/bottom direction allowed
 */
fun getSideMcDoor(mcSide: Int): BlockSide {
    when (mcSide) {
        0 -> return BlockSide.LEFT
        1 -> return BlockSide.BACK
        2 -> return BlockSide.RIGHT
        3 -> return BlockSide.FRONT
    }

    return BlockSide.FRONT
}