//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.gui

import xyz.chunkstories.api.gui.elements.InputText
import xyz.chunkstories.api.input.Input
import xyz.chunkstories.api.input.Mouse.MouseButton
import java.util.LinkedList

abstract class Layer(val gui: Gui, val parentLayer: Layer?) {
    var xPosition: Int = 0
    var yPosition: Int = 0

    var width: Int = 0
    var height: Int = 0

    protected var elements = mutableListOf<GuiElement>()
    var focusedElement: FocusableGuiElement? = null

    // this is the root layer !
    val rootLayer: Layer
        get() = if (parentLayer == null)
            this
        else
            parentLayer!!.rootLayer

    init {
        xPosition = 0
        yPosition = 0

        width = gui.viewportWidth
        height = gui.viewportHeight
    }

    /** Override this to implement your own drawing routines. You may render the
     * parent layer to have an overlay effect, but it's not mandatory.  */
    abstract fun render(drawer: GuiDrawer)

    open fun handleInput(input: Input): Boolean {
        if (focusedElement != null)
            if (focusedElement!!.handleInput(input))
                return true

        if (input is MouseButton) {
            for (guiElement in elements) {
                if (guiElement.isMouseOver) {

                    if (guiElement is FocusableGuiElement)
                        this.focusedElement = guiElement

                    if (guiElement is ClickableGuiElement && (guiElement as ClickableGuiElement).handleClick(input))
                        return true
                }
            }
        }

        return false
    }

    open fun handleTextInput(c: Char): Boolean {
        return if (focusedElement != null && focusedElement is InputText) (focusedElement as InputText).handleTextInput(c) else false
    }

    open fun onResize(newWidth: Int, newHeight: Int) {
        if (parentLayer != null)
            parentLayer!!.onResize(newWidth, newHeight)

        this.width = newWidth
        this.height = newHeight
    }

    /** Frees and closes ressources  */
    fun destroy() {

    }
}
