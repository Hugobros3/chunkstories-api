//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.mesh;

import io.xol.chunkstories.api.client.ClientContent;
import io.xol.chunkstories.api.mesh.MeshLibrary;
import io.xol.chunkstories.api.rendering.vertex.VertexBuffer;

public interface ClientMeshLibrary extends MeshLibrary {
	
	public RenderableMesh getRenderableMeshByName(String meshName);
	
	//Any multi-part mesh can have animations slapped on, no matter how silly
	//public RenderableMultiPartMesh getMultiPartMeshByName(String meshName);
	
	public RenderableMultiPartAnimatableMesh getRenderableMultiPartAnimatableMeshByName(String meshName);
	
	public ClientContent parent();

	public VertexBuffer getIdentityCube();
}