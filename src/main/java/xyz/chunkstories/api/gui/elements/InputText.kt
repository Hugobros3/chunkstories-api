//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.gui.elements

import xyz.chunkstories.api.gui.FocusableGuiElement
import xyz.chunkstories.api.gui.Font
import xyz.chunkstories.api.gui.GuiDrawer
import xyz.chunkstories.api.gui.Layer
import xyz.chunkstories.api.input.Input
import xyz.chunkstories.api.input.Mouse
import org.joml.Vector4f

/**
 * Provides a text field to type text into
 */
class InputText @JvmOverloads constructor(layer: Layer, x: Int, y: Int, width: Int, private val font: Font = layer.gui.fonts.defaultFont()) : FocusableGuiElement(layer, width, 22) {
    var text = ""

    var isTransparent = false
    var isPassword = false

    override var width : Int
        set(value) {super.width = value}
        get() {
            var len = super.width
            val txtlen = font.getWidth(text)
            if (txtlen > len)
                len = txtlen

            return len
        }

    init {
        positionX = x
        positionY = y
        this.width = width
        this.height = 22
    }

    override fun handleInput(input: Input): Boolean {
        if (input.name == "backspace") {
            if (text.length > 0)
                text = text.substring(0, text.length - 1)
            return true
        }
        return false
    }

    fun handleTextInput(c: Char): Boolean {
        if (c.toInt() != 0)
            text += c

        return true
    }

    override fun isMouseOver(mouse: Mouse): Boolean {
        return (mouse.cursorX >= positionX && mouse.cursorX < positionX + width && mouse.cursorY >= positionY
                && mouse.cursorY <= positionY + height)
    }

    override fun render(drawer: GuiDrawer) {

        var text = this.text
        if (isPassword) {
            var passworded = ""
            for (c in text.toCharArray())
                passworded += "*"
            text = passworded
        }

        var backgroundTexture = if (isFocused) "textures/gui/textbox.png" else "textures/gui/textboxnofocus.png"
        if (this.isTransparent)
            backgroundTexture = if (isFocused) "textures/gui/textboxnofocustransp.png" else "textures/gui/textboxtransp.png"

        drawer.drawBoxWithCorners(positionX, positionY, width, height, 8, backgroundTexture)
        drawer.drawStringWithShadow(font, positionX + 4, positionY + 1,
                text + if (isFocused && System.currentTimeMillis() % 1000 > 500) "|" else "", -1, Vector4f(1.0f))

    }
}
