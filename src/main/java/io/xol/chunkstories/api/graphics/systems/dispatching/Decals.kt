//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.graphics.systems.dispatching

import org.joml.Vector3dc

interface DecalsManager {
    fun add(position: Vector3dc, orientation: Vector3dc, size: Vector3dc, decalName: String)
}

interface DecalsRenderer : DecalsManager, DispatchingSystem {
}