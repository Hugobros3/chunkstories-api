//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity;

public interface EntityDamageCause extends DamageCause
{
	public Entity getResponsibleEntity();
}
