package io.xol.chunkstories.api.voxel.models;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import io.xol.chunkstories.api.content.Content;
import io.xol.chunkstories.api.content.Content.Voxels.VoxelModels;
import io.xol.chunkstories.api.mesh.Mesh;
import io.xol.chunkstories.api.mesh.MeshMaterial;
import io.xol.chunkstories.api.rendering.voxel.VoxelBakerHighPoly;
import io.xol.chunkstories.api.rendering.voxel.VoxelRenderer;
import io.xol.chunkstories.api.rendering.world.chunk.ChunkMeshDataSubtypes.LodLevel;
import io.xol.chunkstories.api.rendering.world.chunk.ChunkMeshDataSubtypes.ShadingType;
import io.xol.chunkstories.api.rendering.world.chunk.ChunkRenderer;
import io.xol.chunkstories.api.rendering.world.chunk.ChunkRenderer.ChunkRenderContext;
import io.xol.chunkstories.api.world.cell.CellData;
import io.xol.chunkstories.api.world.chunk.Chunk;

public class MeshAsVoxelModel implements VoxelRenderer {
	private final Content.Voxels.VoxelModels store;
	final Mesh mesh;

	public MeshAsVoxelModel(VoxelModels store, Mesh mesh) {
		this.store = store;
		this.mesh = mesh;
	}

	@Override
	public int bakeInto(ChunkRenderer chunkRenderer, ChunkRenderContext bakingContext, Chunk chunk, CellData cell) {
		VoxelBakerHighPoly baker = chunkRenderer.getHighpolyBakerFor(LodLevel.ANY, ShadingType.OPAQUE);
		int x = cell.getX() & 0x1f;
		int y = cell.getY() & 0x1f;
		int z = cell.getZ() & 0x1f;
		
		Vector4f vertex = new Vector4f(0,0,0,1f);
		Vector4f normal = new Vector4f(0,0,0,0f);
		
		Matrix4f matrix = new Matrix4f();
		
		for(MeshMaterial meshMaterial : mesh.getMaterials()) {
			//System.out.println("material:"+simplify(meshMaterial.getAlbedoTextureName()));
			baker.usingTexture(store.parent().textures().getVoxelTexture(simplify(meshMaterial.getAlbedoTextureName())));
			baker.setVoxelLight((byte)cell.getSunlight(), (byte)cell.getBlocklight(), (byte)0);
			
			for(int j = meshMaterial.firstVertex() / 3; j <= meshMaterial.lastVertex() / 3; j++) {
				for(int k : new int[] {0,1,2}) {
					int i = j*3 + k;
					vertex.set(mesh.getVertices().get(i * 3 + 0), mesh.getVertices().get(i * 3 + 1), mesh.getVertices().get(i * 3 + 2), 1.0f);
					normal.set(mesh.getNormals().get(i * 3 + 0), mesh.getNormals().get(i * 3 + 1), mesh.getNormals().get(i * 3 + 2), 1.0f);
					
					matrix.transform(vertex);
					matrix.transform(normal);
					
					baker.beginVertex(x + vertex.x, y + vertex.y, z + vertex.z);
					baker.setNormal(normal.x, normal.y, normal.z);
					//baker.beginVertex(x + mesh.getVertices().get(i * 3 + 0), y + mesh.getVertices().get(i * 3 + 1), z + mesh.getVertices().get(i * 3 + 2));
					//baker.setNormal(mesh.getNormals().get(i * 3 + 0), mesh.getNormals().get(i * 3 + 1), mesh.getNormals().get(i * 3 + 2));
					baker.setTextureCoordinates(mesh.getTextureCoordinates().get(i * 2 + 0), mesh.getTextureCoordinates().get(i * 2 + 1));
					baker.endVertex();
				}
			}
		}
		return 0;
	}

	private String simplify(String name) {
		name = name.substring(name.lastIndexOf('/') + 1);
		name = name.substring(0, name.lastIndexOf('.'));
		return name;
	}
}
