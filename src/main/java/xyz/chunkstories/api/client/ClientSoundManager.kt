//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.client

import org.joml.Vector3dc

import xyz.chunkstories.api.sound.SoundManager
import xyz.chunkstories.api.sound.SoundSource
import xyz.chunkstories.api.sound.SoundSource.Mode

interface ClientSoundManager : SoundManager {
    fun replicateServerSoundSource(soundName: String, mode: Mode, position: Vector3dc, pitch: Float, gain: Float, attenuationStart: Float, attenuationEnd: Float, UUID: Long): SoundSource
}
