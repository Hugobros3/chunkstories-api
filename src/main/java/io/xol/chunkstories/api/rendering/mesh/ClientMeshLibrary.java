//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.mesh;

import io.xol.chunkstories.api.client.ClientContent;
import io.xol.chunkstories.api.mesh.MeshLibrary;
import io.xol.chunkstories.api.rendering.vertex.VertexBuffer;

/**
 * Extends {@link MeshLibrary} by giving access to meshes in a renderable
 * fashion.
 */
public interface ClientMeshLibrary extends MeshLibrary {

	public RenderableMesh getRenderableMesh(String meshName);

	public RenderableAnimatableMesh getRenderableAnimatableMesh(String meshName);

	public ClientContent parent();

	public void reloadAll();

	public VertexBuffer getIdentityCube();
}