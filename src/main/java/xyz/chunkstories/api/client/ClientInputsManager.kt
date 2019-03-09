//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.client

import xyz.chunkstories.api.input.Input
import xyz.chunkstories.api.input.InputsManager
import xyz.chunkstories.api.input.Mouse

interface ClientInputsManager : InputsManager {
    val mouse: Mouse
    fun onInputPressed(input: Input): Boolean

    fun onInputReleased(input: Input): Boolean
}
