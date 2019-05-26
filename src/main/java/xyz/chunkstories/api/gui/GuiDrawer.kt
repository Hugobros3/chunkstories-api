//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.gui

import xyz.chunkstories.api.graphics.systems.drawing.DrawingSystem
import org.joml.Vector4f
import org.joml.Vector4fc

val white = Vector4f(1.0F)

interface GuiDrawer : DrawingSystem {
    val gui: Gui
    val fonts: Fonts

    /** Draws a string */
    fun drawString(font: Font = fonts.defaultFont(), xPosition: Int, yPosition: Int, text: String, cutoffLength: Int, color: Vector4fc = white)

    fun drawString(font: Font = fonts.defaultFont(), xPosition: Int, yPosition: Int, text: String, color: Vector4fc = white)

    fun drawString(xPosition: Int, yPosition: Int, text: String, color: Vector4fc = white)

    fun drawString(xPosition: Int, yPosition: Int, text: String)

    /** Draws a string with a dropdown shadow, 2 pixels to the lower-right, with the color.rgb *= 0.5 */
    fun drawStringWithShadow(font: Font = fonts.defaultFont(), xPosition: Int, yPosition: Int, text: String, cutoffLength: Int = -1, color: Vector4fc = white)

    fun drawBox(startX: Int, startY: Int, width: Int, height: Int, textureStartX: Float, textureStartY: Float, textureEndX: Float, textureEndY: Float, texture: String?, color: Vector4fc?)

    fun drawBox(startX: Int, startY: Int, width: Int, height: Int, texture: String?, color: Vector4fc?)

    fun drawBox(startX: Int, startY: Int, width: Int, height: Int, texture: String?)

    fun drawBox(startX: Int, startY: Int, width: Int, height: Int, color: Vector4fc?)

    /** Draws a box of arbitrary size and tiles the texture and borders like a proper windowing system.
     * @param cornerSizeDivider The ratio of the total texture size to the size of the corner section. Typically it's 1/8th so you'd input '8'
     */
    fun drawBoxWithCorners(posx: Int, posy: Int, width: Int, height: Int, cornerSizeDivider: Int = 8, texture: String)

    fun withScissor(startX: Int, startY: Int, width: Int, height: Int, code: () -> Unit)
}

interface Fonts {
    /** Will try to newEntity the requested font, scaled to said size, if not found or fails will return defaultFont  */
    fun getFont(fontName: String, sizeInPX: Float): Font

    /** Returns a reasonnable default font, used in most of the interface */
    fun defaultFont(): Font

    /** Returns the default font, but with it's size scaled by N */
    fun defaultFont(sizeMultiplier: Int): Font
}

interface Font {
    val lineHeight: Int
    fun size(): Float

    fun getWidth(whatchars: String): Int
    fun getLinesHeight(whatchars: String, clipWidth: Float): Int
}