//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.item;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import io.xol.chunkstories.api.Location;
import io.xol.chunkstories.api.client.ClientContent;
import io.xol.chunkstories.api.item.ItemVoxel;
import io.xol.chunkstories.api.item.inventory.ItemPile;
import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.rendering.StateMachine.CullingMode;
import io.xol.chunkstories.api.rendering.StateMachine.DepthTestMode;
import io.xol.chunkstories.api.rendering.lightning.Light;
import io.xol.chunkstories.api.rendering.shader.Shader;
import io.xol.chunkstories.api.rendering.textures.Texture2D;
import io.xol.chunkstories.api.rendering.vertex.Primitive;
import io.xol.chunkstories.api.rendering.vertex.VertexBuffer;
import io.xol.chunkstories.api.rendering.vertex.VertexFormat;
import io.xol.chunkstories.api.rendering.voxel.VoxelBakerCubic;
import io.xol.chunkstories.api.rendering.voxel.VoxelBakerHighPoly;
import io.xol.chunkstories.api.rendering.voxel.VoxelRenderer;
import io.xol.chunkstories.api.rendering.world.chunk.ChunkMeshDataSubtypes.LodLevel;
import io.xol.chunkstories.api.rendering.world.chunk.ChunkMeshDataSubtypes.ShadingType;
import io.xol.chunkstories.api.rendering.world.chunk.ChunkRenderer;
import io.xol.chunkstories.api.rendering.world.chunk.ChunkRenderer.ChunkRenderContext;
import io.xol.chunkstories.api.rendering.world.chunk.vertexlayout.BaseLayoutBaker;
import io.xol.chunkstories.api.rendering.world.chunk.vertexlayout.IntricateLayoutBaker;
import io.xol.chunkstories.api.voxel.Voxel;
import io.xol.chunkstories.api.voxel.VoxelSide.Corners;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.cell.CellData;
import io.xol.chunkstories.api.world.cell.DummyCell;
import io.xol.chunkstories.api.world.chunk.DummyChunk;

// TODO : Explicit thumbnail rendering path in VoxelRenderer to avoid this mess ?
/** Over-engineered way of turning VoxelRenderer output to render a voxel as an item */
public class VoxelItemRenderer extends ItemRenderer {
	final ClientContent content;

	Matrix4f transformation = new Matrix4f();
	Map<String, VertexBuffer> voxelItemsModelBuffer = new HashMap<>();
	private ChunkRenderContext bakingContext;

	public VoxelItemRenderer(ClientContent content, ItemRenderer fallbackRenderer) {
		super(fallbackRenderer);
		this.content = content;

		bakingContext = new ChunkRenderContext() {
			private VoxelLighter lighter = new VoxelLighter() {
				@Override
				public byte getSunlightLevelForCorner(Corners corner) {
					return 15;
				}

				@Override
				public byte getBlocklightLevelForCorner(Corners corner) {
					return 0;
				}

				@Override
				public byte getAoLevelForCorner(Corners corner) {
					return 0;
				}

				@Override
				public byte getSunlightLevelInterpolated(float vertX, float vertY, float vertZ) {
					return 15;
				}

				@Override
				public byte getBlocklightLevelInterpolated(float vertX, float vertY, float vertZ) {
					return 0;
				}

				@Override
				public byte getAoLevelInterpolated(float vertX, float vertY, float vertZ) {
					return 0;
				}

			};

			@Override
			public boolean isTopChunkLoaded() {
				return true;
			}

			@Override
			public boolean isBottomChunkLoaded() {
				return true;
			}

			@Override
			public boolean isLeftChunkLoaded() {
				return true;
			}

			@Override
			public boolean isRightChunkLoaded() {
				return true;
			}

			@Override
			public boolean isFrontChunkLoaded() {
				return true;
			}

			@Override
			public boolean isBackChunkLoaded() {
				return true;
			}

			@Override
			public int getRenderedVoxelPositionInChunkX() {
				return 0;
			}

			@Override
			public int getRenderedVoxelPositionInChunkY() {
				return 0;
			}

			@Override
			public int getRenderedVoxelPositionInChunkZ() {
				return 0;
			}

			@Override
			public VoxelLighter getCurrentVoxelLighter() {
				return lighter;
			}

		};
	}

	@Override
	public void renderItemInInventory(RenderingInterface renderer, ItemPile pile, float screenPositionX, float screenPositionY, int scaling) {

		/*if (((ItemVoxel) pile.getItem()).getVoxel() instanceof VoxelCustomIcon) {
			fallbackRenderer.renderItemInInventory(renderer, pile, screenPositionX, screenPositionY, scaling);
			return;
		}*/

		int slotSize = 24 * scaling;
		Shader program = renderer.useShader("inventory_blockmodel");

		renderer.setCullingMode(CullingMode.COUNTERCLOCKWISE);
		renderer.setDepthTestMode(DepthTestMode.LESS_OR_EQUAL);

		program.setUniform2f("screenSize", renderer.getWindow().getWidth(), renderer.getWindow().getHeight());
		program.setUniform2f("dekal", screenPositionX + pile.getItem().getDefinition().getSlotsWidth() * slotSize / 2, screenPositionY + pile.getItem().getDefinition().getSlotsHeight() * slotSize / 2);
		program.setUniform1f("scaling", slotSize / 1.65f);

		transformation.identity();
		transformation.scale(new Vector3f(-1f, 1f, 1f));
		transformation.rotate(toRad(-22.5f), new Vector3f(1.0f, 0.0f, 0.0f));
		transformation.rotate(toRad(45f), new Vector3f(0.0f, 1.0f, 0.0f));
		transformation.translate(new Vector3f(-0.5f, -0.5f, -0.5f));

		program.setUniformMatrix4f("transformation", transformation);
		Voxel voxel = ((ItemVoxel) pile.getItem()).getVoxel();
		if (voxel == null) {
			int width = slotSize * pile.getItem().getDefinition().getSlotsWidth();
			int height = slotSize * pile.getItem().getDefinition().getSlotsHeight();
			renderer.getGuiRenderer().drawBoxWindowsSpaceWithSize(screenPositionX, screenPositionY, width,
					height, 0, 1, 1, 0, content.textures().getTexture("./items/icons/notex.png"), true, true, null);
			return;
		}
		
		Texture2D texture = content.voxels().textures().getDiffuseAtlasTexture();
		texture.setLinearFiltering(false);
		renderer.bindAlbedoTexture(texture);

		Texture2D normalTexture = content.voxels().textures().getNormalAtlasTexture();
		normalTexture.setLinearFiltering(false);
		renderer.bindNormalTexture(normalTexture);

		Texture2D materialTexture = content.voxels().textures().getMaterialAtlasTexture();
		materialTexture.setLinearFiltering(false);
		renderer.bindMaterialTexture(materialTexture);

		CellData fakeCell = new DummyCell(0, 0, 0, voxel, 0, 0, 0) {
			
			CellData air = new DummyCell(0, 1, 0, voxel.store().air(), 0, 0, 0);
			
			@Override
			public int getBlocklight() {
				return voxel.getEmittedLightLevel(this);
			}

			@Override
			public CellData getNeightbor(int side) {
				return air;
			}

			@Override
			public int getMetaData() {
				return ((ItemVoxel) pile.getItem()).getVoxelMeta();
			}
		};
		
		renderFakeVoxel(renderer, fakeCell);
	}

	@Override
	public void renderItemInWorld(RenderingInterface renderer, ItemPile pile, World world, Location location, Matrix4f handTransformation) {

		/*if (((ItemVoxel) pile.getItem()).getVoxel() instanceof VoxelCustomIcon) {
			fallbackRenderer.renderItemInWorld(renderer, pile, world, location, handTransformation);
			return;
		}*/

		Voxel voxel = ((ItemVoxel) pile.getItem()).getVoxel();
		if (voxel == null)
			return;

		CellData fakeCell = new DummyCell(0, 0, 0, voxel, 0, 0, 0) {
			
			CellData air = new DummyCell(0, 1, 0, voxel.store().air(), 0, 0, 0);
			
			@Override
			public int getBlocklight() {
				return voxel.getEmittedLightLevel(this);
			}

			@Override
			public CellData getNeightbor(int side) {
				return air;
			}

			@Override
			public int getMetaData() {
				return ((ItemVoxel) pile.getItem()).getVoxelMeta();
			}
		};

		float s = 0.45f;
		handTransformation.scale(new Vector3f(s, s, s));
		handTransformation.translate(new Vector3f(-0.25f, -0.5f, -0.5f));
		renderer.setObjectMatrix(handTransformation);

		// Add a light only on the opaque pass
		if (fakeCell.getBlocklight() > 0
				&& renderer.getCurrentPass().name.contains("gBuffers")) {
			Vector4f lightposition = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);

			handTransformation.transform(lightposition);

			Light heldBlockLight = new Light(new Vector3f(0.6f, 0.50f, 0.4f).mul(0.5f), new Vector3f(lightposition.x(), lightposition.y(), lightposition.z()), 15f);
			renderer.getLightsRenderer().queueLight(heldBlockLight);

			// If we hold a light source, prepare the shader accordingly
			renderer.currentShader().setUniform2f("worldLightIn",
					Math.max(world.peekSafely(location).getBlocklight(), fakeCell.getBlocklight()),
					world.peekSafely(location).getSunlight());
		}

		Texture2D texture = content.voxels().textures().getDiffuseAtlasTexture();
		texture.setLinearFiltering(false);
		renderer.bindAlbedoTexture(texture);

		Texture2D normalTexture = content.voxels().textures().getNormalAtlasTexture();
		normalTexture.setLinearFiltering(false);
		renderer.bindNormalTexture(normalTexture);

		Texture2D materialTexture = content.voxels().textures().getMaterialAtlasTexture();
		materialTexture.setLinearFiltering(false);
		renderer.bindMaterialTexture(materialTexture);
		
		renderFakeVoxel(renderer, fakeCell);
	}

	/**
	 * The purpose of this class is to bake the voxel mesh by itself in a single VBO
	 * used by the item render, and that uses a specific layout
	 */
	private class VoxelInHandLayoutBaker extends IntricateLayoutBaker implements VoxelBakerCubic {

		VoxelInHandLayoutBaker(ClientContent content, ByteBuffer output) {
			super(content, output);
		}

		@Override
		// We don't care about saving a few bytes since the point is to fill a buffer
		// with the data from a single block
		public void beginVertex(int i0, int i1, int i2) {
			this.beginVertex((float) i0, (float) i1, (float) i2);
		}

		@Override
		public void endVertex() {
			output.putFloat(currentVertex.x);
			output.putFloat(currentVertex.y);
			output.putFloat(currentVertex.z);

			// We divide by 32768f because that's the max size of our atlas ( and thus int
			// coordinates assume a 32768 pixel wide and tall atlas )
			// The blocks-specific shader knows to divide the texture coordinates, but the
			// general-purpose Entity shader this uses doesn't
			output.putFloat((currentTexture.getAtlasS() + texCoords.x * currentTexture.getAtlasOffset()) / 32768f);
			output.putFloat((currentTexture.getAtlasT() + texCoords.y * currentTexture.getAtlasOffset()) / 32768f);

			// 1010102 Layout, 3 float components ( precision overkill ? ) + 2-bit flag for
			// wavy grass etc
			int n0 = BaseLayoutBaker.floatToUnsigned10Bit(normal.x);
			int n1 = BaseLayoutBaker.floatToUnsigned10Bit(normal.y);
			int n2 = BaseLayoutBaker.floatToUnsigned10Bit(normal.z);
			output.putInt(BaseLayoutBaker.pack1010102(n0, n1, n2, wavyFlag ? 3 : 0));
		}
	}

	/** Bake a single cell's content into a buffer */
	private void bakeVoxelRenderer(CellData cell, VertexBuffer mesh) {
		ByteBuffer buffer = ByteBuffer.allocateDirect(1024000).order(ByteOrder.nativeOrder());
		VoxelInHandLayoutBaker specialSauce = new VoxelInHandLayoutBaker(this.content, buffer);

		// Dummy objects that point to our special sauce
		ChunkRenderer chunkRenderer = new ChunkRenderer() {

			@Override
			public VoxelBakerHighPoly getHighpolyBakerFor(LodLevel lodLevel, ShadingType renderPass) {
				return specialSauce;
			}

			@Override
			public VoxelBakerCubic getLowpolyBakerFor(LodLevel lodLevel, ShadingType renderPass) {
				return specialSauce;
			}

		};

		VoxelRenderer voxelRenderer = cell.getVoxelRenderer();
		if (voxelRenderer == null) {
			voxelRenderer = cell.getVoxel().store().models().getVoxelModel("default");
		}
		
		// Render into a dummy chunk ( containing only that one voxel we want )
		voxelRenderer.bakeInto(chunkRenderer, bakingContext, new DummyChunk() {

			@Override
			public int peekRaw(int x, int y, int z) {
				// if(x == 0 && y == 0 && z == 0)
				// return bri.getData();
				return 0;
			}

		}, cell);

		// Flip the buffer and upload it
		buffer.flip();
		mesh.uploadData(buffer);
	}
	
	/** Look for this configuration of the Voxel in the local cache */
	private void renderFakeVoxel(RenderingInterface renderingContext, CellData cell) {
		String hash = cell.getVoxel().getName() + cell.getMetaData();
		
		// If we did not already cache this variant of the model
		if (!voxelItemsModelBuffer.containsKey(hash)) {
			// Generous allocation TODO use JEmalloc here
			VertexBuffer mesh = renderingContext.newVertexBuffer();
			bakeVoxelRenderer(cell, mesh);
			voxelItemsModelBuffer.put(hash, mesh);
		}

		if (voxelItemsModelBuffer.containsKey(hash)) {
			VertexBuffer mesh = voxelItemsModelBuffer.get(hash);

			// This is why we needed VoxelInHandLayoutBaker!
			renderingContext.bindAttribute("vertexIn", mesh.asAttributeSource(VertexFormat.FLOAT, 3, 24, 0));
			renderingContext.bindAttribute("texCoordIn", mesh.asAttributeSource(VertexFormat.FLOAT, 2, 24, 12));
			renderingContext.bindAttribute("normalIn", mesh.asAttributeSource(VertexFormat.U1010102, 4, 24, 20));

			renderingContext.draw(Primitive.TRIANGLE, 0, (int) (mesh.getVramUsage() / 24));
		}
	}

	private float toRad(float f) {
		return (float) (f / 180 * Math.PI);
	}
}
