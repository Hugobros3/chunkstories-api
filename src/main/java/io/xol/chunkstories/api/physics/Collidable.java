package io.xol.chunkstories.api.physics;

import org.joml.Vector3dc;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public interface Collidable
{
	public boolean collidesWith(Collidable box);
	
	public Vector3dc lineIntersection(Vector3dc lineStart, Vector3dc lineDirection);
}
