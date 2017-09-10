package io.xol.chunkstories.api.item.renderer;

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
import io.xol.chunkstories.api.rendering.lightning.Light;
import io.xol.chunkstories.api.rendering.pipeline.ShaderInterface;
import io.xol.chunkstories.api.rendering.textures.Texture2D;
import io.xol.chunkstories.api.rendering.vertex.VertexBuffer;
import io.xol.chunkstories.api.rendering.vertex.VertexFormat;
import io.xol.chunkstories.api.rendering.pipeline.PipelineConfiguration.CullingMode;
import io.xol.chunkstories.api.rendering.pipeline.PipelineConfiguration.DepthTestMode;
import io.xol.chunkstories.api.rendering.Primitive;
import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.rendering.WorldRenderer.RenderingPass;
import io.xol.chunkstories.api.voxel.Voxel;
import io.xol.chunkstories.api.voxel.VoxelCustomIcon;
import io.xol.chunkstories.api.voxel.VoxelFormat;
import io.xol.chunkstories.api.voxel.VoxelSides.Corners;
import io.xol.chunkstories.api.voxel.models.ChunkMeshDataSubtypes.LodLevel;
import io.xol.chunkstories.api.voxel.models.ChunkMeshDataSubtypes.ShadingType;
import io.xol.chunkstories.api.voxel.models.ChunkRenderer;
import io.xol.chunkstories.api.voxel.models.ChunkRenderer.ChunkRenderContext;
import io.xol.chunkstories.api.voxel.models.VoxelBakerCubic;
import io.xol.chunkstories.api.voxel.models.VoxelBakerHighPoly;
import io.xol.chunkstories.api.voxel.models.VoxelRenderer;
import io.xol.chunkstories.api.voxel.models.layout.BaseLayoutBaker;
import io.xol.chunkstories.api.voxel.models.layout.IntricateLayoutBaker;
import io.xol.chunkstories.api.world.VoxelContext;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.world.chunk.DummyChunk;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public class VoxelItemRenderer extends ItemRenderer
{
	final ClientContent content;
	
	Matrix4f transformation = new Matrix4f();
	Map<Integer, VertexBuffer> voxelItemsModelBuffer = new HashMap<Integer, VertexBuffer>();
	private ChunkRenderContext bakingContext;

	public VoxelItemRenderer(ClientContent content, ItemRenderer fallbackRenderer)
	{
		super(fallbackRenderer);
		this.content = content;
		
		bakingContext = new ChunkRenderContext() {

			private VoxelLighter lighter = new VoxelLighter() {

				@Override
				public byte getSunlightLevelForCorner(Corners corner)
				{
					// TODO Auto-generated method stub
					return 15;
				}

				@Override
				public byte getBlocklightLevelForCorner(Corners corner)
				{
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public byte getAoLevelForCorner(Corners corner)
				{
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public byte getSunlightLevelInterpolated(float vertX, float vertY, float vertZ)
				{
					// TODO Auto-generated method stub
					return 15;
				}

				@Override
				public byte getBlocklightLevelInterpolated(float vertX, float vertY, float vertZ)
				{
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public byte getAoLevelInterpolated(float vertX, float vertY, float vertZ)
				{
					// TODO Auto-generated method stub
					return 0;
				}
				
			};

			@Override
			public boolean isTopChunkLoaded()
			{
				return true;
			}

			@Override
			public boolean isBottomChunkLoaded()
			{
				return true;
			}

			@Override
			public boolean isLeftChunkLoaded()
			{
				return true;
			}

			@Override
			public boolean isRightChunkLoaded()
			{
				return true;
			}

			@Override
			public boolean isFrontChunkLoaded()
			{
				return true;
			}

			@Override
			public boolean isBackChunkLoaded()
			{
				return true;
			}

			@Override
			public int getRenderedVoxelPositionInChunkX()
			{
				return 0;
			}

			@Override
			public int getRenderedVoxelPositionInChunkY()
			{
				return 0;
			}

			@Override
			public int getRenderedVoxelPositionInChunkZ()
			{
				return 0;
			}

			@Override
			public VoxelLighter getCurrentVoxelLighter()
			{
				// TODO Auto-generated method stub
				return lighter ;
			}
			
		};
	}

	@Override
	public void renderItemInInventory(RenderingInterface renderingContext, ItemPile pile, float screenPositionX, float screenPositionY, int scaling)
	{
		//voxelItemsModelBuffer.clear();
		//ClientContent content = ((ClientContent)pile.getItem().getType().store().parent())
		
		if (((ItemVoxel) pile.getItem()).getVoxel() instanceof VoxelCustomIcon)
		{
			fallbackRenderer.renderItemInInventory(renderingContext, pile, screenPositionX, screenPositionY, scaling);
			return;
		}

		int slotSize = 24 * scaling;
		ShaderInterface program = renderingContext.useShader("inventory_blockmodel");
		
		renderingContext.setCullingMode(CullingMode.COUNTERCLOCKWISE);
		renderingContext.setDepthTestMode(DepthTestMode.LESS_OR_EQUAL);

		program.setUniform2f("screenSize", renderingContext.getWindow().getWidth(), renderingContext.getWindow().getHeight());
		program.setUniform2f("dekal", screenPositionX + pile.getItem().getType().getSlotsWidth() * slotSize / 2, screenPositionY + pile.getItem().getType().getSlotsHeight() * slotSize / 2);
		program.setUniform1f("scaling", slotSize / 1.65f);
		transformation.identity();
		transformation.scale(new Vector3f(-1f, 1f, 1f));
		transformation.rotate(toRad(-22.5f), new Vector3f(1.0f, 0.0f, 0.0f));
		transformation.rotate(toRad(45f), new Vector3f(0.0f, 1.0f, 0.0f));
		transformation.translate(new Vector3f(-0.5f, -0.5f, -0.5f));
		
		program.setUniformMatrix4f("transformation", transformation);
		Voxel voxel = ((ItemVoxel) pile.getItem()).getVoxel();
		if (voxel == null)
		{
			int width = slotSize * pile.getItem().getType().getSlotsWidth();
			int height = slotSize * pile.getItem().getType().getSlotsHeight();
			renderingContext.getGuiRenderer().drawBoxWindowsSpaceWithSize(screenPositionX, screenPositionY, width, height, 0, 1, 1, 0, content.textures().getTexture("./items/icons/notex.png"), true, true, null);
			return;
		}
		Texture2D texture = content.voxels().textures().getDiffuseAtlasTexture();
		texture.setLinearFiltering(false);
		renderingContext.bindAlbedoTexture(texture);
		
		Texture2D normalTexture = content.voxels().textures().getNormalAtlasTexture();
		normalTexture.setLinearFiltering(false);
		renderingContext.bindNormalTexture(normalTexture);
		
		Texture2D materialTexture = content.voxels().textures().getMaterialAtlasTexture();
		materialTexture.setLinearFiltering(false);
		renderingContext.bindMaterialTexture(materialTexture);

		//'Fake' voxel context
		VoxelContext bri = new VoxelContext() {
			@Override
			public Voxel getVoxel() {
				return voxel;
			}
			@Override
			public int getData() {
				return VoxelFormat.format(voxel.getId(), ((ItemVoxel) pile.getItem()).getVoxelMeta(), 15, voxel.getLightLevel(0));
			}
			@Override
			public int getX() {return 0;}
			@Override
			public int getY() {return 0;}
			@Override
			public int getZ() {return 0;}
			@Override
			public int getNeightborData(int side) {return 0;}
			@Override
			public World getWorld() {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
		//new VoxelContextOlder(VoxelFormat.format(voxel.getId(), ((ItemVoxel) pile.getItem()).getVoxelMeta(), 15, voxel.getLightLevel(0)), 0, 0, 0);
		VoxelRenderer model = voxel.getVoxelRenderer(bri);
		if (model == null)
		{
			model = voxel.store().models().getVoxelModelByName("default");
		}
		renderVoxel(renderingContext, voxel, model, bri);
	}
	
	@Override
	public void renderItemInWorld(RenderingInterface context, ItemPile pile, World world, Location location, Matrix4f handTransformation)
	{
		if (((ItemVoxel) pile.getItem()).getVoxel() instanceof VoxelCustomIcon)
		{
			fallbackRenderer.renderItemInWorld(context, pile, world, location, handTransformation);
			return;
		}
		
		float s = 0.45f;
		handTransformation.scale(new Vector3f(s, s, s));
		handTransformation.translate(new Vector3f(-0.25f, -0.5f, -0.5f));
		context.setObjectMatrix(handTransformation);
		Voxel voxel = ((ItemVoxel) pile.getItem()).getVoxel();
		if (voxel == null)
		{
			return;
		}

		//Add a light only on the opaque pass
		if (((ItemVoxel) pile.getItem()).getVoxel().getLightLevel(0x00) > 0 && context.getWorldRenderer().getCurrentRenderingPass() == RenderingPass.NORMAL_OPAQUE)
		{
			Vector4f lightposition = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
			
			handTransformation.transform(lightposition);
			
			Light heldBlockLight = new Light(new Vector3f(0.5f, 0.45f, 0.4f).mul(2.0f), new Vector3f(lightposition.x(), lightposition.y(), lightposition.z()), 15f);
			context.getLightsRenderer().queueLight(heldBlockLight);	
			
			//If we hold a light source, prepare the shader accordingly
			context.currentShader().setUniform2f("worldLightIn", ((ItemVoxel) pile.getItem()).getVoxel().getLightLevel(0x00), world.getSunlightLevelLocation(location));
		}
		
		Texture2D texture = content.voxels().textures().getDiffuseAtlasTexture();
		texture.setLinearFiltering(false);
		context.bindAlbedoTexture(texture);
		
		Texture2D normalTexture = content.voxels().textures().getNormalAtlasTexture();
		normalTexture.setLinearFiltering(false);
		context.bindNormalTexture(normalTexture);
		
		Texture2D materialTexture = content.voxels().textures().getMaterialAtlasTexture();
		materialTexture.setLinearFiltering(false);
		context.bindMaterialTexture(materialTexture);

		//'Fake' voxel context
		VoxelContext bri = new VoxelContext() {
			@Override
			public Voxel getVoxel() {
				return voxel;
			}
			@Override
			public int getData() {
				return VoxelFormat.format(voxel.getId(), ((ItemVoxel) pile.getItem()).getVoxelMeta(), 15, voxel.getLightLevel(0));
			}
			@Override
			public int getX() {return 0;}
			@Override
			public int getY() {return 0;}
			@Override
			public int getZ() {return 0;}
			@Override
			public int getNeightborData(int side) {return 0;}
			@Override
			public World getWorld() {
				return world;
			}
			
		};
		
		VoxelRenderer model = voxel.getVoxelRenderer(bri);
		if (model == null)
		{
			model = voxel.store().models().getVoxelModelByName("default");
		}
		renderVoxel(context, voxel, model, bri);
	}

	/** The purpose of this class is to bake the voxel mesh by itself in a single VBO used by the item render, and that uses a specific layout */
	private class VoxelInHandLayoutBaker extends IntricateLayoutBaker implements VoxelBakerCubic {
		
		VoxelInHandLayoutBaker(ClientContent content, ByteBuffer output) {
			super(content, output);
		}

		@Override
		//We don't care about saving a few bytes since the point is to fill a buffer with the data from a single block
		public void beginVertex(int i0, int i1, int i2)
		{
			this.beginVertex((float)i0, (float)i1, (float)i2);
		}

		@Override
		public void endVertex() {
			output.putFloat(currentVertex.x);
			output.putFloat(currentVertex.y);
			output.putFloat(currentVertex.z);
			
			//We divide by 32768f because that's the max size of our atlas ( and thus int coordinates assume a 32768 pixel wide and tall atlas )
			//The blocks-specific shader knows to divide the texture coordinates, but the general-purpose Entity shader this uses doesn't
			output.putFloat((currentTexture.getAtlasS() + texCoords.x * currentTexture.getAtlasOffset()) / 32768f );
			output.putFloat((currentTexture.getAtlasT() + texCoords.y * currentTexture.getAtlasOffset()) / 32768f );
			
			//1010102 Layout, 3 float components ( precision overkill ? ) + 2-bit flag for wavy grass etc
			int n0 = BaseLayoutBaker.floatToUnsigned10Bit(normal.x);
			int n1 = BaseLayoutBaker.floatToUnsigned10Bit(normal.y);
			int n2 = BaseLayoutBaker.floatToUnsigned10Bit(normal.z);
			output.putInt(BaseLayoutBaker.pack1010102(n0, n1, n2, wavyFlag ? 3 : 0));
		}
		
		/*@Override
		public void addTexCoordInt(int i0, int i1)
		{
			byteBuffer.putFloat(i0 / 32768f);
			byteBuffer.putFloat(i1 / 32768f);
		}*/
	}

	private void renderVoxel(RenderingInterface renderingContext, Voxel voxel, VoxelRenderer voxelRenderer, VoxelContext bri)
	{
		//If we did not already cache this model
		if (!voxelItemsModelBuffer.containsKey(bri.getMetaData() + 16 * voxel.getId()))
		{
			//Generous allocation
			//TODO Jemalloc all the things
			ByteBuffer buffer = ByteBuffer.allocateDirect(16384).order(ByteOrder.nativeOrder()); //BufferUtils.createByteBuffer(16384);
			VoxelInHandLayoutBaker specialSauce = new VoxelInHandLayoutBaker(this.content, buffer);
			
			//Dummy objects that point to our special sauce
			ChunkRenderer chunkRenderer = new ChunkRenderer() {

				@Override
				public VoxelBakerHighPoly getHighpolyBakerFor(LodLevel lodLevel, ShadingType renderPass)
				{
					return specialSauce;
				}

				@Override
				public VoxelBakerCubic getLowpolyBakerFor(LodLevel lodLevel, ShadingType renderPass)
				{
					return specialSauce;
				}
				
			};
			
			//Render into a dummy chunk ( containing only that one voxel we want )
			voxelRenderer.renderInto(chunkRenderer, bakingContext, new DummyChunk() {
				
				@Override
				public int getVoxelData(int x, int y, int z)
				{
					if(x == 0 && y == 0 && z == 0)
						return bri.getData();
					return 0;
				}
				
			}, bri);
			
			
			//Flip the buffer and upload it
			buffer.flip();
			VertexBuffer mesh = renderingContext.newVertexBuffer();
			mesh.uploadData(buffer);
			
			voxelItemsModelBuffer.put(bri.getMetaData() + 16 * voxel.getId(), mesh);
		}
		
		//Fail-safe in case above step fails ?
		if (voxelItemsModelBuffer.containsKey(bri.getMetaData() + 16 * voxel.getId()))
		{
			VertexBuffer mesh = voxelItemsModelBuffer.get(bri.getMetaData() + 16 * voxel.getId());
			
			//This is why we needed VoxelInHandLayoutBaker!
			renderingContext.bindAttribute("vertexIn", mesh.asAttributeSource(VertexFormat.FLOAT, 3, 24, 0));
			renderingContext.bindAttribute("texCoordIn", mesh.asAttributeSource(VertexFormat.FLOAT, 2, 24, 12));
			renderingContext.bindAttribute("normalIn", mesh.asAttributeSource(VertexFormat.U1010102, 4, 24, 20));
			
			renderingContext.draw(Primitive.TRIANGLE, 0, (int) (mesh.getVramUsage() / 24));
		}
	}

	private float toRad(float f)
	{
		return (float) (f / 180 * Math.PI);
	}
}
