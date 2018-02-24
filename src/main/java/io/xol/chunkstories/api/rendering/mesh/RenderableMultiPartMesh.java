//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.mesh;

import io.xol.chunkstories.api.exceptions.rendering.RenderingException;
import io.xol.chunkstories.api.rendering.RenderingInterface;

public interface RenderableMultiPartMesh extends RenderableMesh {
	
	/**
	 * Render only the meshes part selected
	 * @throws RenderingException 
	 */
	public void render(RenderingInterface renderingInterface, String... parts ) throws RenderingException;
	
	/** Lists the different parts this mesh is comprised of */
	public Iterable<String> allParts();
}
