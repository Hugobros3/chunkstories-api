package io.xol.chunkstories.api.client;

import org.joml.Vector3dc;

import io.xol.chunkstories.api.sound.SoundManager;
import io.xol.chunkstories.api.sound.SoundSource;
import io.xol.chunkstories.api.sound.SoundSource.Mode;

public interface ClientSoundManager extends SoundManager {

	public SoundSource replicateServerSoundSource(String soundName, Mode mode, Vector3dc position,
			float pitch, float gain, float attenuationStart, float attenuationEnd, long UUID);

}
