//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.world.chunk.vertexlayout;

import java.nio.ByteBuffer;

import io.xol.chunkstories.api.client.ClientContent;
import io.xol.chunkstories.api.rendering.voxel.VoxelBakerCubic;
import io.xol.chunkstories.api.rendering.world.chunk.ChunkMeshDataSubtypes.VertexLayout;

/** Defines the (default) layout used by the game when using the WHOLE_BLOCKS
 * VertexLayout */
public class WholeBlocksLayoutBaker extends BaseLayoutBaker implements VoxelBakerCubic {

	public static final VertexLayout IMPLEMENTS() {
		return VertexLayout.WHOLE_BLOCKS;
	}

	protected int i0, i1, i2;

	public WholeBlocksLayoutBaker(ClientContent content, ByteBuffer output) {
		super(content, output);
	}

	@Override
	public void reset() {
		i0 = 0;
		i1 = 0;
		i2 = 0;
	}

	@Override
	public void beginVertex(int i0, int i1, int i2) {
		this.i0 = i0;
		this.i1 = i1;
		this.i2 = i2;
	}

	@Override
	public void endVertex() {
		if (output.capacity() - output.position() < IMPLEMENTS().bytesPerVertex)
			return;

		// The first three bytes of output
		output.put((byte) i0);
		output.put((byte) i1);
		output.put((byte) i2);
		output.put(materialFlags);

		// Resolve the positions in the texture atlas ( as non-float shorts! )
		output.putShort((short) (currentTexture.getAtlasS() + texCoords.x * currentTexture.getAtlasOffset()));
		output.putShort((short) (currentTexture.getAtlasT() + texCoords.y * currentTexture.getAtlasOffset()));

		// The voxel light data + another byte of padding
		output.put(blockLight);
		output.put(sunLight);
		output.put(ao);
		output.put(materialFlags);

		// 1010102 Layout, 3 float components ( precision overkill ? ) + 2-bit flag for
		// wavy grass etc
		int n0 = BaseLayoutBaker.floatToUnsigned10Bit(normal.x);
		int n1 = BaseLayoutBaker.floatToUnsigned10Bit(normal.y);
		int n2 = BaseLayoutBaker.floatToUnsigned10Bit(normal.z);
		output.putInt(BaseLayoutBaker.pack1010102(n0, n1, n2, wavyFlag ? 3 : 0));

		// TODO Investigate making use of the padding available and come up with a
		// better default layout
	}
}
