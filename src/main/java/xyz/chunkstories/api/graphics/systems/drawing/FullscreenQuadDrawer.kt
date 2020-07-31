//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.graphics.systems.drawing

/** For many effects in computer graphics, you want to have a pass that just touches every pixel in the framebuffer to
 * apply some fragment shader to it. This system does exactly and nothing but that, added to a pass it means the pass
 * will just draw a quad without having to write explicit code. */
interface FullscreenQuadDrawer : DrawingSystem {
    var shader: String

    val defines: MutableMap<String, String>
}