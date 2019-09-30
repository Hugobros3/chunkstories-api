//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.gui.elements

import xyz.chunkstories.api.gui.ClickableGuiElement
import xyz.chunkstories.api.gui.FocusableGuiElement
import xyz.chunkstories.api.gui.GuiDrawer
import xyz.chunkstories.api.gui.Layer
import xyz.chunkstories.api.input.Mouse
import xyz.chunkstories.api.input.Mouse.MouseButton
import org.joml.Vector4f

abstract class ScrollableContainer protected constructor(layer: Layer) : FocusableGuiElement(layer, 480, 1024), ClickableGuiElement {

    var elements = mutableListOf<ContainerElement>()
    protected var scroll = 0

    open fun isMouseOver(mx: Int, my: Int): Boolean {
        return mx >= positionX && mx <= positionX + width && my >= positionY && my <= positionY + height
    }

    override fun render(drawer: GuiDrawer) {
        var startY = this.positionY + height
        var i = scroll

        while (true) {
            if (i >= elements.size)
                break
            val element = elements[i]
            startY -= element.height
            if (startY < this.positionY)
                break
            i++

            element.setPosition(this.positionX, startY)
            element.render(drawer)
            startY -= 4
        }

        // return r;
    }

    open fun scroll(sign: Boolean) {
        if (sign) {
            // Scroll up
            scroll--
            if (scroll < 0)
                scroll = 0
        } else {
            // Scroll up
            scroll++
            if (scroll >= elements.size)
                scroll = elements.size - 1
        }
    }

    abstract inner class ContainerElement(var name: String?, var descriptionLines: String?) {
        var topRightString: String? = ""
        var iconTextureLocation = "textures/gui/info.png"

        var positionX: Int = 0
        var positionY: Int = 0

        var width = 480
        var height = 72

        fun setPosition(positionX: Int, positionY: Int) {
            this.positionX = positionX
            this.positionY = positionY
        }

        open fun render(drawer: GuiDrawer) {
            // Setup textures
            val bgTexture = if (isMouseOver(drawer.gui.mouse)) "textures/gui/genericOver.png" else "textures/gui/generic.png"

            // Render graphical base
            drawer.drawBox(positionX, positionY, width, height, 0f, 1f, 1f, 0f, bgTexture, Vector4f(1.0f, 1.0f, 1.0f, 1.0f))
            // Render icon
            drawer.drawBox(positionX + 4, positionY + 4, 64, 64, 0f, 1f, 1f, 0f, iconTextureLocation, Vector4f(1.0f, 1.0f, 1.0f, 1.0f))
            // Text !
            if (name != null)
                drawer.drawString(drawer.fonts.getFont("LiberationSans-Regular", 12f), positionX + 70, positionY + 54, name!!, -1,
                        Vector4f(0.0f, 0.0f, 0.0f, 1.0f))

            if (topRightString != null) {
                val dekal = width - drawer.fonts.getFont("LiberationSans-Regular", 12f).getWidth(topRightString!!) - 4
                drawer.drawString(drawer.fonts.getFont("LiberationSans-Regular", 12f), positionX + dekal, positionY + 54, topRightString!!, -1,
                        Vector4f(0.25f, 0.25f, 0.25f, 1.0f))
            }

            if (descriptionLines != null)
                drawer.drawString(drawer.fonts.getFont("LiberationSans-Regular", 12f), positionX + 70, positionY + 38, descriptionLines!!, -1,
                        Vector4f(0.25f, 0.25f, 0.25f, 1.0f))

        }

        abstract fun handleClick(mouseButton: MouseButton): Boolean

        fun isMouseOver(mouse: Mouse): Boolean {
            val s = 1
            val mx = mouse.cursorX
            val my = mouse.cursorY
            return mx >= positionX && mx <= positionX + width * s && my >= positionY && my <= positionY + height * s
        }
    }

    override fun handleClick(mouseButton: MouseButton): Boolean {
        var startY = (this.positionY + height).toFloat()
        var i = scroll

        while (true) {
            if (i >= elements.size)
                break
            val element = elements[i]
            if (startY - element.height < this.positionY)
                break
            startY -= element.height.toFloat()
            startY -= 4f
            i++

            if (element.isMouseOver(mouseButton.mouse)) {
                element.handleClick(mouseButton)
                return true
            }
        }

        return false
    }
}
