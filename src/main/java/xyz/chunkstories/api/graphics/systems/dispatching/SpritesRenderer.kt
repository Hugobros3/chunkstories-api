//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics.systems.dispatching

interface SpritesRenderer : DispatchingSystem {
    var materialTag: String

    var shader: String
}