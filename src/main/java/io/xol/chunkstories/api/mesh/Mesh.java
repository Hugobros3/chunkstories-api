//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.mesh;

import java.nio.FloatBuffer;

/** Describes a mesh in an abstracted, in-engine way. */
public class Mesh {

	protected final FloatBuffer vertices; // Stored as triplets of coordinates
	protected final FloatBuffer textureCoordinates; // Stored as couples of coordinates
	protected final FloatBuffer normals; // Stored as normalized triplets

	protected final MeshMaterial materials[];

	public Mesh(FloatBuffer vertices, FloatBuffer textureCoordinates, FloatBuffer normals, MeshMaterial materials[]) {
		this.vertices = vertices;
		this.textureCoordinates = textureCoordinates;
		this.normals = normals;

		this.materials = materials;
	}

	public int getVerticesCount() {
		return vertices.capacity() / 3;
	}

	public FloatBuffer getVertices() {
		return vertices;
	}

	public FloatBuffer getTextureCoordinates() {
		return textureCoordinates;
	}

	public FloatBuffer getNormals() {
		return normals;
	}

	public MeshMaterial[] getMaterials() {
		return materials;
	}
}
