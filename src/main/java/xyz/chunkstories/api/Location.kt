//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api

import org.joml.Vector3d
import org.joml.Vector3dc

import xyz.chunkstories.api.world.World

/** Cartesian product of a world and a position within that world  */
class Location(val world: World, x: Double, y: Double, z: Double) : Vector3d(x, y, z) {

    constructor(world: World, position: Vector3dc) : this(world, position.x(), position.y(), position.z())

    constructor(location: Location) : this(location.world, location.x, location.y, location.z)

    fun set(loc: Location) {
        if (loc.world !== this.world)
            throw RuntimeException("You can't change the world of a location.")

        super.set(loc)
    }
}
