//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.gui

import io.xol.chunkstories.api.input.Mouse

abstract class GuiElement protected constructor(protected val layer: Layer, open var width: Int, open var height: Int) {
    var positionX = 0
    var positionY = 0


    val isMouseOver: Boolean
        get() = isMouseOver(layer.getGui().mouse)

    fun setPosition(x: Int, y: Int) {
        positionX = x
        positionY = y
    }

    /** Is the mouse over this object  */
    open fun isMouseOver(mouse: Mouse): Boolean {
        return (mouse.cursorX >= this.positionX && mouse.cursorY >= this.positionY
                && mouse.cursorX <= this.positionX + this.width && mouse.cursorY <= this.positionY + this.height)
    }

    abstract fun render(renderer: GuiDrawer)
}
