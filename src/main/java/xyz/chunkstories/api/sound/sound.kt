//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//
package xyz.chunkstories.api.sound

import org.joml.Vector3dc
import org.joml.Vector3fc

data class Sound(val expression: String)

typealias SoundSourceID = Long

interface SoundSource {
    val id: SoundSourceID

    val soundName: String

    var position: Vector3dc?
    val mode: Mode
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

interface SoundManager {
    fun playSoundEffect(soundEffect: String): SoundSource? =
            this.playSoundEffect(soundEffect, SoundSource.Mode.NORMAL, null, 1.0f, 1.0f, 1.0f, 100.0f)

    fun playSoundEffect(soundEffect: String, mode: SoundSource.Mode, position: Vector3dc?, pitch: Float, gain: Float): SoundSource? =
            this.playSoundEffect(soundEffect, mode, position, pitch, gain, 5f, 25f)

    fun playSoundEffect(soundEffect: String, mode: SoundSource.Mode, position: Vector3dc? = null, pitch: Float, gain: Float, attenuationStart: Float = 5f, attenuationEnd: Float = 25f): SoundSource?

    fun getSoundSource(id: SoundSourceID): SoundSource?

    fun stopAllSounds()
    val playingSounds: Collection<SoundSource>
}

interface ClientSoundEngine : SoundManager {
    fun setListenerPosition(position: Vector3fc?, lookAt: Vector3fc?, up: Vector3fc?)
}