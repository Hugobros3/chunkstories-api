//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.particles;

import org.joml.Vector3dc;

public interface ParticlesManager
{
	public void spawnParticleAtPosition(String particleTypeName, Vector3dc position);
	
	public void spawnParticleAtPositionWithVelocity(String particleTypeName, Vector3dc position, Vector3dc velocity);
}
