//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.exceptions.world.voxel

import xyz.chunkstories.api.exceptions.world.VoxelException
import xyz.chunkstories.api.world.cell.CellData

/** Used to restrict block operations, is thrown when a forbidden action is
 * being attempted  */
class IllegalBlockModificationException(context: CellData, private val message2: String) : VoxelException(context) {

    override val message: String?
        get() {
            return message2
        }

    companion object {

        private val serialVersionUID = -1717494086092644106L
    }
}
