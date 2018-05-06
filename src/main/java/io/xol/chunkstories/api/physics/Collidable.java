//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.physics;

import org.joml.Vector3dc;

import javax.annotation.Nullable;

public interface Collidable
{
	public boolean collidesWith(Collidable box);
	
	@Nullable
	public Vector3dc lineIntersection(Vector3dc lineStart, Vector3dc lineDirection);
}
