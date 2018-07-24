//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.mesh;

import io.xol.chunkstories.api.exceptions.rendering.RenderingException;
import io.xol.chunkstories.api.rendering.Renderable;
import io.xol.chunkstories.api.rendering.RenderingInterface;

/** Renders a {@link Mesh}, obtained from a {@link ClientMeshLibrary}. */
public interface RenderableMesh extends Renderable {

	/** Renders the mesh geometry */
	public void render(RenderingInterface renderer) throws RenderingException;

	/**
	 * Renders the mesh using the materials and textures defined in the mesh file,
	 * if present
	 */
	public void renderUsingMaterials(RenderingInterface renderer) throws RenderingException;
}
