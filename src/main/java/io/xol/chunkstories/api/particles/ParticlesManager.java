package io.xol.chunkstories.api.particles;

import org.joml.Vector3dc;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public interface ParticlesManager
{
	public void spawnParticleAtPosition(String particleTypeName, Vector3dc position);
	
	public void spawnParticleAtPositionWithVelocity(String particleTypeName, Vector3dc position, Vector3dc velocity);
}
