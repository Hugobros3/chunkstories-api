//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics.systems.drawing

import xyz.chunkstories.api.graphics.systems.GraphicSystem

/** A Drawing system is a system that supplies vertex data and submits draw calls in the taskInstance
 * of whatever passes invoke it. */
interface DrawingSystem : GraphicSystem