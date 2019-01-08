//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.particles;

import org.joml.Vector3dc;
import org.joml.Vector3fc;

public interface ParticleDataWithVelocity {
	public void setVelocity(Vector3fc vel);

	public void setVelocity(Vector3dc vel);
}
