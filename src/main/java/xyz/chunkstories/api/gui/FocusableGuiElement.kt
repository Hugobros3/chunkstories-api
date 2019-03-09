//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.gui

import xyz.chunkstories.api.input.Input

/** Elements that can be focused, either by tab-ing on them, or by clicking  */
abstract class FocusableGuiElement protected constructor(layer: Layer, width: Int, height: Int) : GuiElement(layer, width, height) {

    val isFocused: Boolean
        get() = this == layer.focusedElement

    /** When focused an element receives input from the keyboard  */
    open fun handleInput(input: Input): Boolean {
        return false
    }
}
