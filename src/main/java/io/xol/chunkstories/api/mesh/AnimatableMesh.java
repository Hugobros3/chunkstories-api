//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.mesh;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class AnimatableMesh extends Mesh {

	protected final String[] boneNames; // Store the names of the bones for each ID
	protected final ByteBuffer boneIds; // Store 4 bone ids per vertice
	protected final ByteBuffer boneWeights; // Store the relative weights of these referenced bones.
	// Normalized by the engine from 0-255 range to 0.0-1.0

	public AnimatableMesh(FloatBuffer vertices, FloatBuffer textureCoordinates, FloatBuffer normals, String[] names,
			ByteBuffer ids, ByteBuffer weights, MeshMaterial materials[]) {
		super(vertices, textureCoordinates, normals, materials);
		this.boneNames = names;
		this.boneIds = ids;
		this.boneWeights = weights;
	}

	public String[] getBoneNames() {
		return boneNames;
	}

	public ByteBuffer getBoneIds() {
		return boneIds;
	}

	public ByteBuffer getBoneWeights() {
		return boneWeights;
	}
}
