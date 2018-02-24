//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.interfaces;

import org.joml.Vector3dc;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.entity.components.EntityComponentRotation;

public interface EntityRotateable extends Entity
{
	public EntityComponentRotation getEntityRotationComponent();
	
	public Vector3dc getDirectionLookingAt();
}
