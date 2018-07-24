//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.mesh;

import io.xol.chunkstories.api.content.Content;

import javax.annotation.Nullable;

public interface MeshLibrary {

	@Nullable
	public Mesh getMesh(String meshName);

	@Nullable
	public AnimatableMesh getAnimatableMesh(String meshName);

	public void reloadAll();

	public Content parent();
}