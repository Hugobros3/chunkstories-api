//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.world.cell

import xyz.chunkstories.api.Location
import xyz.chunkstories.api.physics.Box
import xyz.chunkstories.api.voxel.Voxel
import xyz.chunkstories.api.world.World

/** A CellData is representing the content of a specific getCell in the world.
 *
 * A getCell is the smallest editable bit of world data in chunk stories. A
 * getCell contains a Voxel type, 8 bits of free-form metadata and 2x 4-bits of
 * voxel lightning ( for sun/skylight and voxel-emitted light )  */
interface Cell {
    val world: World

    val x: Int
    val y: Int
    val z: Int

    // Just here so dummy classes are shorter, actual voxel contexts have a field for this
    //TODO remove like 'world' and move to WorldCell
    val location: Location
        get() = Location(world, x.toDouble(), y.toDouble(), z.toDouble())

    /** Return the Voxel type used in this getCell  */
    val voxel: Voxel

    val metaData: Int

    val sunlight: Int

    val blocklight: Int

    /** Returns an array (possibly 0-sized) of collision boxes translated to the
     * actual position of the voxel  */
    val translatedCollisionBoxes: Array<Box>?
        get() {
            val voxel = voxel
            return if (voxel != null) voxel.getTranslatedCollisionBoxes(this) else arrayOf()
        }

    // Neightbor cells access

    fun getNeightbor(side: Int): Cell

    fun getNeightborVoxel(side: Int): Voxel? {
        return getNeightbor(side).voxel // Optimisation hint: do not newEntity the neightbor object if you just want
        // to peek the voxel
    }

    fun getNeightborMetadata(side: Int): Int {
        return getNeightbor(side).metaData // Optimisation hint: do not newEntity the neightbor object if you just
        // want to peek the metadata
    }
}