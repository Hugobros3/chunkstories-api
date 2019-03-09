//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.gui

import xyz.chunkstories.api.input.Mouse.MouseButton

interface ClickableGuiElement {
    fun handleClick(mouseButton: MouseButton): Boolean
}
