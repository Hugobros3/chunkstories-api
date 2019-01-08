//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity;

import xyz.chunkstories.api.graphics.systems.dispatching.DecalsManager;
import xyz.chunkstories.api.input.InputsManager;
import xyz.chunkstories.api.particles.ParticlesManager;
import xyz.chunkstories.api.sound.SoundManager;
import xyz.chunkstories.api.world.WorldUser;

import javax.annotation.Nullable;

/** The Controller is a special subscriber that don't receive normal tracking
 * updates and can push changes to the controlled entity */
public interface Controller extends Subscriber, WorldUser {
	public InputsManager getInputsManager();

	@Nullable
	public Entity getControlledEntity();

	public boolean setControlledEntity(@Nullable Entity entity);

	// TODO make those explicit shorthands...
	public SoundManager getSoundManager();

	public ParticlesManager getParticlesManager();

	public DecalsManager getDecalsManager();
}
