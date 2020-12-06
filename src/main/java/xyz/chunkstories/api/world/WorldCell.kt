//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world

import xyz.chunkstories.api.Location
import xyz.chunkstories.api.world.cell.MutableCell

interface WorldCell : MutableCell {
    val world: World

    val location: Location
        get() = Location(world, x.toDouble(), y.toDouble(), z.toDouble())
}