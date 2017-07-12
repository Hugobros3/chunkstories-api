package io.xol.chunkstories.api.entity.interfaces;

import org.joml.Vector3dc;

import io.xol.chunkstories.api.entity.Entity;
import io.xol.chunkstories.api.entity.components.EntityComponentRotation;

//(c) 2015-2017 XolioWare Interactive
// http://chunkstories.xyz
// http://xol.io

public interface EntityRotateable extends Entity
{
	public EntityComponentRotation getEntityRotationComponent();
	
	public Vector3dc getDirectionLookingAt();
}
