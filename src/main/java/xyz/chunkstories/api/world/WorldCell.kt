//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world

import xyz.chunkstories.api.Location
import xyz.chunkstories.api.block.BlockSide
import xyz.chunkstories.api.world.cell.Cell
import xyz.chunkstories.api.world.cell.MutableCell

interface WorldCell : Cell {
    val world: World

    val location: Location
        get() = Location(world, x.toDouble(), y.toDouble(), z.toDouble())

    fun getNeighbour(side: BlockSide) = world.getCell(x + side.dx, y + side.dy, z + side.dz)
    fun getNeighbourMut(side: BlockSide) = world.getCellMut(x + side.dx, y + side.dy, z + side.dz)
}

interface MutableWorldCell : MutableCell, WorldCell