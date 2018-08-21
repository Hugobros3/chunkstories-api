//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity;

import io.xol.chunkstories.api.input.InputsManager;
import io.xol.chunkstories.api.particles.ParticlesManager;
import io.xol.chunkstories.api.rendering.effects.DecalsManager;
import io.xol.chunkstories.api.sound.SoundManager;
import io.xol.chunkstories.api.world.WorldUser;

import javax.annotation.Nullable;

/** The Controller is a special subscriber that don't receive normal tracking
 * updates and can push changes to the controlled entity */
public interface Controller extends Subscriber, WorldUser {
	public InputsManager getInputsManager();

	@Nullable
	public Entity getControlledEntity();

	public boolean setControlledEntity(@Nullable Entity entity);

	public SoundManager getSoundManager();

	public ParticlesManager getParticlesManager();

	public DecalsManager getDecalsManager();
}
