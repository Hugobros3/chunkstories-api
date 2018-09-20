//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.gui

import org.joml.Vector4f
import org.joml.Vector4fc

val white = Vector4f(1.0F)

interface GuiDrawer {
    val gui: Gui
    val fonts: Fonts

    /** Draws a string */
    fun drawString(font: Font = fonts.defaultFont(), xPosition: Int, yPosition: Int, text: String, cutoffLength: Int = -1, color: Vector4fc = white)

    /** Draws a string with a dropdown shadow, 2 pixels to the lower-right, with the color.rgb *= 0.5 */
    fun drawStringWithShadow(font: Font = fonts.defaultFont(), xPosition: Int, yPosition: Int, text: String, cutoffLength: Int = -1, color: Vector4fc = white)

    fun drawBoxWindowsSpaceWithSize(startX: Int, startY: Int, width: Int, height: Int, textureStartX: Float, textureStartY: Float, textureEndX: Float, textureEndY: Float, texture: String?, color: Vector4fc?)

    fun drawCorneredBoxTiled(posx: Int, posy: Int, width: Int, height: Int, cornerSize: Int, texture: String, textureSize: Int)
}

interface Fonts {
    /** Will try to newEntity the requested font, scaled to said size, if not found or fails will return defaultFont  */
    fun getFont(fontName: String, sizeInPX: Float): Font

    /** Returns a reasonnable default font, used in most of the interface */
    fun defaultFont(): Font
}

interface Font {
    val lineHeight: Int
    fun size(): Float

    fun getWidth(whatchars: String): Int
    fun getLinesHeight(whatchars: String, clipWidth: Float): Int
}