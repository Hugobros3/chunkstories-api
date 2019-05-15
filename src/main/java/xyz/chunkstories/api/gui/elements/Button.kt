//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.gui.elements

import xyz.chunkstories.api.gui.*
import org.joml.Vector4f

import xyz.chunkstories.api.input.Mouse
import xyz.chunkstories.api.input.Mouse.MouseButton

/** Provides a scalable button  */
open class Button(layer: Layer, val font: Font, x: Int, y: Int, var text: String) : FocusableGuiElement(layer, 0, 22), ClickableGuiElement {

    var action: Runnable? = null

    override var width: Int
        get() {
            val localizedText = layer.gui.localization().localize(text)
            val labelWidth = font.getWidth(localizedText) + 8

            return if (super.width > labelWidth) super.width else labelWidth

        }
        set(value) { super.width = value }

    constructor(layer: Layer, x: Int, y: Int, width: Int, text: String) : this(layer, x, y, text, null) {
        this.width = width
    }

    @JvmOverloads
    constructor(layer: Layer, x: Int, y: Int, text: String, action: Runnable? = null) : this(layer, layer.gui.fonts.getFont("LiberationSans-Regular", 12f), x, y, text) {
        this.action = action
    }

    init {

        this.positionX = x
        this.positionY = y
    }

    override fun isMouseOver(mouse: Mouse): Boolean {
        return (mouse.cursorX >= positionX && mouse.cursorX < positionX + width && mouse.cursorY >= positionY
                && mouse.cursorY <= positionY + height)
    }

    override fun render(renderer: GuiDrawer) {
        val localizedText = layer.gui.localization().localize(text)

        var buttonTexture = "textures/gui/scalableButton.png"
        if (isFocused || isMouseOver)
            buttonTexture = "textures/gui/scalableButtonOver.png"

        renderer.drawBoxWithCorners(positionX, positionY, width, height, 8, buttonTexture)
        renderer.drawString(font, positionX + 4, positionY + (height / 2) - (font.lineHeight / 2) - 2 , localizedText, -1, Vector4f(0f, 0f, 0f, 1f))
    }

    override fun handleClick(mouseButton: MouseButton): Boolean {
        if (mouseButton.name != "mouse.left")
            return false

        this.layer.gui.client.soundManager.playSoundEffect("sounds/gui/gui_click2.ogg")

        if (this.action != null)
            this.action!!.run()

        return true
    }
}
