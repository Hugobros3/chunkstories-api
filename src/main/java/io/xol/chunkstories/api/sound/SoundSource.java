//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.sound;

import org.joml.Vector3dc;

import javax.annotation.Nullable;

public interface SoundSource {
	public enum Mode {
		NORMAL, STREAMED, LOOPED
	}

	/** The string that was used to start this sound source */
	public String getSoundName();

	public long getUUID();

	public Mode getMode();

	/** Sets the pitch to a specific source */
	public SoundSource setPitch(float pitch);

	public float getPitch();

	/** Sets the gain of the source. 1.0f is the default. */
	public SoundSource setGain(float gain);

	public float getGain();

	/**
	 * Ambient SoundSources have the special property of always being "on" the
	 * listener, and don't have directional effects either. Ex: GUI sounds, music
	 * etc
	 */
	// public SoundSource setAmbient(boolean ambient);

	/**
	 * * Used to control the sound attenuation, gain = (1 - isAmbient)*(distance -
	 * start)/(max - start)
	 * 
	 * @param start distance in blocks units
	 * @return The modified SoundSource
	 */
	public SoundSource setAttenuationStart(float start);

	public float getAttenuationStart();

	/**
	 * Used to control the sound attenuation, gain = (1 - isAmbient)*(distance -
	 * start)/(max - start)
	 * 
	 * @param end distance in blocks units
	 * @return The modified SoundSource
	 */
	public SoundSource setAttenuationEnd(float end);

	public float getAttenuationEnd();

	/** Sets the source position */
	public SoundSource setPosition(float x, float y, float z);

	/**
	 * Alternative call for setPosition(). Passing null will throw an exception,
	 * ambiant sound sources must be constructed so.
	 */
	public SoundSource setPosition(Vector3dc location);

	@Nullable
	public Vector3dc getPosition();

	/**
	 * Removes and stops the SoundSource. In case this source was using an unique
	 * SoundData (ie streamed/buffered) it also deletes the said source and frees
	 * ressources.
	 */
	public void stop();

	/** @return wether the sound source is not active anymore */
	boolean isDonePlaying();
}
