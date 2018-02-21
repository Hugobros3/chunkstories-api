package io.xol.chunkstories.api.rendering.world.chunk.vertexlayout;

import java.nio.ByteBuffer;

import org.joml.Vector3dc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import io.xol.chunkstories.api.client.ClientContent;
import io.xol.chunkstories.api.rendering.voxel.VoxelBakerHighPoly;
import io.xol.chunkstories.api.rendering.world.chunk.ChunkMeshDataSubtypes.VertexLayout;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** Defines the (default) layout used by the game when using the INTRICATE VertexLayout */
public class IntricateLayoutBaker extends BaseLayoutBaker implements VoxelBakerHighPoly {

	public static final VertexLayout IMPLEMENTS() {
		return VertexLayout.INTRICATE;
	}
	
	protected Vector3f currentVertex = new Vector3f();
	
	public IntricateLayoutBaker(ClientContent content, ByteBuffer output) {
		super(content, output);
	}

	@Override
	public void reset() {
		currentVertex.set(0f);
	}

	@Override
	public void beginVertex(float f0, float f1, float f2) {
		this.currentVertex.set(f0, f1, f2);
	}

	@Override
	public void beginVertex(Vector3fc vertex) {
		this.currentVertex.set(vertex);
	}

	@Override
	public void beginVertex(Vector3dc vertex) {
		this.currentVertex.set(vertex);
	}

	@Override
	public void endVertex() {
		if(output.capacity() - output.position() < IMPLEMENTS().bytesPerVertex)
			return;
		
		//The first floats are just outputed bare
		output.putFloat(currentVertex.x);
		output.putFloat(currentVertex.y);
		output.putFloat(currentVertex.z);
		
		//Resolve the positions in the texture atlas ( as non-float shorts! )
		output.putShort((short) (currentTexture.getAtlasS() + texCoords.x * currentTexture.getAtlasOffset()));
		output.putShort((short) (currentTexture.getAtlasT() + texCoords.y * currentTexture.getAtlasOffset()));
		
		//The voxel light data + another byte of padding
		output.put(blockLight);
		output.put(sunLight);
		output.put(ao);
		output.put((byte)0x00);
		
		//1010102 Layout, 3 float components ( precision overkill ? ) + 2-bit flag for wavy grass etc
		int n0 = BaseLayoutBaker.floatToUnsigned10Bit(normal.x);
		int n1 = BaseLayoutBaker.floatToUnsigned10Bit(normal.y);
		int n2 = BaseLayoutBaker.floatToUnsigned10Bit(normal.z);
		output.putInt(BaseLayoutBaker.pack1010102(n0, n1, n2, wavyFlag ? 3 : 0));
		
		//TODO Investigate making use of the padding available and come up with a better default layout
	}
}
