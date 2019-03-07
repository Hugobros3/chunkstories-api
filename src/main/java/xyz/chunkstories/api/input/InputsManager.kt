//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.input

interface InputsManager {

    val allInputs: Collection<Input>

    fun getInputByName(inputName: String): Input?
    fun getInputFromHash(hash: Long): Input?
}
