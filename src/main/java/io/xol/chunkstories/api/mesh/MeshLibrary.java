//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.mesh;

import io.xol.chunkstories.api.content.Content;

public interface MeshLibrary {
	
	public Mesh getMeshByName(String meshName);
	
	public MultiPartMesh getMultiPartMeshByName(String meshName);
	
	public Content parent();
}