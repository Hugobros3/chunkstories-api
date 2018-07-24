//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.sound;

import java.util.Iterator;

import org.joml.Vector3dc;
import org.joml.Vector3fc;

import io.xol.chunkstories.api.sound.SoundSource.Mode;

import javax.annotation.Nullable;

public interface SoundManager {
	/**
	 * Plays a soundEffect with no context in the world, for gui or other sounds
	 * that need no attenuation or position.
	 */
	public default SoundSource playSoundEffect(String soundEffect) {
		return this.playSoundEffect(soundEffect, Mode.NORMAL, null, 1.0f, 1.0f, 1.0f, 100.0f);
	}

	public default SoundSource playSoundEffect(String soundEffect, Mode mode, Vector3dc position, float pitch,
			float gain) {
		return this.playSoundEffect(soundEffect, mode, position, pitch, gain, 5f, 25f);
	}

	/**
	 * Plays a soundEffect in the world
	 * 
	 * @param soundEffect The name of the soundEffect asset, including the file
	 *                    extension
	 * @param mode        The Mode to use for this sound
	 * @param position    The position of the sound, or NULL to play it as an
	 *                    ambiant sound
	 * @param pitch       The pitch of the sound, keep it between 0.5 - 2.0 for best
	 *                    results
	 * @param gain        The gain of the sound, ie volume
	 * @param attStart    The gain of the sound, ie volume
	 * @param attEnd      The gain of the sound, ie volume
	 * @return
	 */
	public SoundSource playSoundEffect(String soundEffect, Mode mode, @Nullable Vector3dc position, float pitch,
			float gain, float attStart, float attEnd);

	@Nullable
	public default SoundSource getSoundSourceByUUID(long UUID) {
		Iterator<SoundSource> i = getAllPlayingSounds();
		while (i.hasNext()) {
			SoundSource s = i.next();
			if (s.getUUID() == UUID)
				return s;
		}
		return null;
	}

	/**
	 * Stops all currently playing sounds matching this name
	 * 
	 * @param soundEffect
	 */
	public void stopAnySound(String soundEffect);

	/**
	 * Stops all currently playing sounds
	 * 
	 * @param effect
	 */
	public void stopAnySound();

	public Iterator<SoundSource> getAllPlayingSounds();

	public abstract void setListenerPosition(float x, float y, float z, Vector3fc lookAt, Vector3fc up);
}
