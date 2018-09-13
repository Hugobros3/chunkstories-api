//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.graphics

/** Client game window */
interface Window {
    val width: Int
    val height: Int

    fun hasFocus(): Boolean

    fun takeScreenshot(): String
}