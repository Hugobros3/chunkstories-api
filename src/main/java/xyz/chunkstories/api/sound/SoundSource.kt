//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.sound

import org.joml.Vector3dc

interface SoundSource {
    val uuid: Long
    val name: String

    var position: Vector3dc?
    val mode: SoundSource.Mode
    var gain: Float
    var pitch: Float
    var attenuationStart: Float
    var attenuationEnd: Float

    val isDonePlaying: Boolean

    enum class Mode {
        NORMAL, STREAMED, LOOPED
    }

    fun stop()
}