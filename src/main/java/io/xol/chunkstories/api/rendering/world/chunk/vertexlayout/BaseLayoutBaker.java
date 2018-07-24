//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.world.chunk.vertexlayout;

import java.nio.ByteBuffer;

import org.joml.Vector2f;
import org.joml.Vector3f;

import io.xol.chunkstories.api.client.ClientContent;
import io.xol.chunkstories.api.rendering.voxel.VoxelBakerCommon;
import io.xol.chunkstories.api.rendering.world.chunk.ChunkRenderer.ChunkRenderContext.VoxelLighter;
import io.xol.chunkstories.api.voxel.VoxelSide.Corners;
import io.xol.chunkstories.api.voxel.textures.VoxelTexture;

/**
 * In a move to abstract away the buffer layout from mods, these classes now
 * serve to link the abstract layout in the VoxelBaker* classes to the actual
 * layout used by the game, which may now change without breaking mods, since
 * they won't rely on knowing it explicitly to send their mesh data to the
 * engine.
 * 
 * These classes effectively replace RenderByteBuffer.java
 */
public abstract class BaseLayoutBaker implements VoxelBakerCommon {

	protected final ClientContent content;
	protected final VoxelTexture defaultTexture;

	protected ByteBuffer output;

	/** If you want to make your own layout baker that does not use a ByteBuffer */
	public BaseLayoutBaker(ClientContent content) {
		this.content = content;

		this.defaultTexture = content.voxels().textures().getVoxelTexture("notex");
		this.currentTexture = defaultTexture;

		// Leaving this in causes a NPE in IntricateLayoutBaker :( Silly Java
		// this.reset();
	}

	/** Used by subclasses */
	BaseLayoutBaker(ClientContent content, ByteBuffer output) {
		if (output == null)
			throw new NullPointerException();

		this.content = content;
		this.output = output;

		this.defaultTexture = content.voxels().textures().getVoxelTexture("notex");
		this.currentTexture = defaultTexture;

		// Leaving this in causes a NPE in IntricateLayoutBaker :( Silly Java
		// this.reset();
	}

	protected byte sunLight, blockLight, ao;
	protected VoxelTexture currentTexture = null;
	protected Vector2f texCoords = new Vector2f();
	protected Vector3f normal = new Vector3f();
	protected byte materialFlags;

	protected boolean wavyFlag;

	public void reset() {
		sunLight = 15;
		blockLight = 0;
		ao = 0;
		currentTexture = defaultTexture;
		texCoords.set(0f);
		normal.set(0f, 1f, 0f);
		materialFlags = 0;
		wavyFlag = false;
	}

	@Override
	public void setVoxelLight(byte sunLight, byte blockLight, byte ao) {
		this.sunLight = (byte) (sunLight & 0xF);
		this.blockLight = (byte) (blockLight & 0xF);
		this.ao = (byte) (ao & 0x3);
	}

	public void setMaterialFlags(byte materialFlags) {
		this.materialFlags = materialFlags;
	}

	@Override
	public void setVoxelLightAuto(VoxelLighter voxelLighter, Corners corner) {
		setVoxelLight(voxelLighter.getSunlightLevelForCorner(corner), voxelLighter.getBlocklightLevelForCorner(corner),
				voxelLighter.getAoLevelForCorner(corner));
	}

	@Override
	public void usingTexture(VoxelTexture voxelTexture) {
		if (voxelTexture == null) {
			throw new NullPointerException("usingTexture(null)!");
		}

		this.currentTexture = voxelTexture;
	}

	@Override
	public void setTextureCoordinates(float s, float t) {
		texCoords.set(s, t);
	}

	@Override
	public void setNormal(float x, float y, float z) {
		normal.set(x, y, z);
	}

	@Override
	public void setWavyFlag(boolean wavy) {
		this.wavyFlag = wavy;
	}

	public void changeOutput(ByteBuffer output) {
		this.output = output;
	}

	public static final byte floatToSignedByte(float f) {
		int i = (int) (f * 128);
		if (i > 127)
			return (byte) 127;
		else if (i < -128)
			return (byte) -128;

		byte b = (byte) (i & 0xFF);

		return b;
	}

	public static final int floatToUnsigned10Bit(float f) {
		int i = (int) (512 + f * 512);
		if (i > 1023)
			return 1023;
		else if (i < 0)
			return 0;

		return i;
	}

	public static final int pack1010102(int i0, int i1, int i2, int extra) {
		int a = (i0) & 0x3FF;
		int b = ((i1) & 0x3FF) << 10;
		int c = ((i2) & 0x3FF) << 20;

		int d = (extra & 0x3) << 30;
		return a | b | c | d;
	}

	@Override
	/** Actual dirty work goes here: the rest is just interface work */
	public abstract void endVertex();

}
