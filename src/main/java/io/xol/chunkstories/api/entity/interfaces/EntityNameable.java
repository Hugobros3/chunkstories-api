//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.entity.interfaces;

import io.xol.chunkstories.api.entity.components.EntityComponentName;

public interface EntityNameable
{
	public abstract String getName();
	public abstract void setName(String n);
	
	public abstract EntityComponentName getNameComponent();
}
