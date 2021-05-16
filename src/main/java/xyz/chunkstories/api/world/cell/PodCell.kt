//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.cell

open class PodCell(override val x: Int, override val y: Int, override val z: Int, override val data: CellData) : Cell {
    override fun toString(): String {
        return "[Cell $x, $y, $z data=$data]"
    }
}