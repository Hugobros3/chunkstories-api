package io.xol.chunkstories.api.client;

import java.util.Iterator;

import org.slf4j.Logger;

import io.xol.chunkstories.api.content.Content;
import io.xol.chunkstories.api.rendering.mesh.ClientMeshLibrary;
import io.xol.chunkstories.api.rendering.pipeline.ShaderInterface;
import io.xol.chunkstories.api.rendering.textures.Cubemap;
import io.xol.chunkstories.api.rendering.textures.Texture2D;
import io.xol.chunkstories.api.rendering.textures.TextureFormat;
import io.xol.chunkstories.api.voxel.textures.VoxelTexture;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

public interface ClientContent extends Content
{
	public ClientInterface getClient();
	
	public ClientMeshLibrary meshes();
	
	public ClientVoxels voxels();
	public interface ClientVoxels extends Voxels {
		
		public ClientVoxelTextures textures();
		public interface ClientVoxelTextures extends VoxelTextures {
			
			public Texture2D getDiffuseAtlasTexture();
			
			public Texture2D getNormalAtlasTexture();
			
			public Texture2D getMaterialAtlasTexture();
			
			public VoxelTexture getVoxelTextureByName(String voxelTextureName);
			
			public Iterator<VoxelTexture> all();

			public Voxels parent();
			
			public Logger logger();
		}
	}

	public TexturesLibrary textures();
	public interface TexturesLibrary {

		public Texture2D nullTexture();
		
		public Texture2D getTexture(String assetName);
		
		public Texture2D newTexture2D(TextureFormat type, int width, int height);
		
		public Cubemap getCubemap(String cubemapName);
		
		/** Drops all textures loaded in VRAM */
		public void reloadAll();
		
		public ClientContent parent();
		
		public Logger logger();
	}
	
	public ShadersLibrary shaders();
	public interface ShadersLibrary {

		public ShaderInterface getShaderProgram(String shaderName);
		
		public void reloadShader(String shaderName);
		
		public void reloadAll();
		
		public ClientContent parent();
		
		public Logger logger();
	}
}
