//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.cell

import xyz.chunkstories.api.voxel.Voxel

/** A getCell we have the right to edit.  */
interface EditableCell : Cell {
    override var voxel: Voxel

    override var metaData: Int

    override var sunlight: Int

    override var blocklight: Int
}
