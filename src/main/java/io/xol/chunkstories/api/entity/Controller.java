//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity;

import io.xol.chunkstories.api.entity.components.Subscriber;
import io.xol.chunkstories.api.entity.interfaces.EntityControllable;
import io.xol.chunkstories.api.input.InputsManager;
import io.xol.chunkstories.api.particles.ParticlesManager;
import io.xol.chunkstories.api.rendering.effects.DecalsManager;
import io.xol.chunkstories.api.sound.SoundManager;
import io.xol.chunkstories.api.world.chunk.WorldUser;

/**
 * The Controller is a special subscriber that don't receive normal tracking updates and can push changes to the controlled entity
 */
public interface Controller extends Subscriber, WorldUser
{
	public InputsManager getInputsManager();
	
	public EntityControllable getControlledEntity();
	
	public boolean setControlledEntity(EntityControllable entityControllable);
	
	public SoundManager getSoundManager();

	public ParticlesManager getParticlesManager();

	public DecalsManager getDecalsManager();
}
