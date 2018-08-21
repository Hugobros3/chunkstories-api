//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.mesh;

import io.xol.chunkstories.api.content.Asset;
import io.xol.chunkstories.api.exceptions.content.MeshLoadException;

public interface MeshLoader {

	/** What extension does this loader provides support for ? (ie: .obj .col .dae
	 * .h3d ) */
	public String getExtension();

	/** Returns a loaded Mesh or MultiPartMesh */
	public Mesh loadMeshFromAsset(Asset a) throws MeshLoadException;
}
