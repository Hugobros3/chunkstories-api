//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.world;

import io.xol.chunkstories.api.particles.ParticlesRenderer;
import io.xol.chunkstories.api.rendering.GameWindow;
import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.rendering.effects.DecalsRenderer;
import io.xol.chunkstories.api.rendering.pass.RenderPasses;
import io.xol.chunkstories.api.rendering.shader.Shader;
import io.xol.chunkstories.api.rendering.textures.ArrayTexture;
import io.xol.chunkstories.api.rendering.world.chunk.ChunksRenderer;
import io.xol.chunkstories.api.world.WorldClient;

/** Contains the necessary tools to render a world and all it's objects */
public interface WorldRenderer {
	public WorldClient getWorld();

	public RenderPasses renderPasses();

	/** Tells the chunks renderer to rebuilt it's PVS set */
	public void flagChunksModified();

	/** Resizes the rendering buffers to fit the game window */
	// public void setupRenderSize();

	/** Sets the various world-related shader uniforms the shader uses */
	public void setupShaderUniforms(Shader shader);

	public float getAnimationTimer();

	public SkyRenderer getSkyRenderer();

	public void setSkyRenderer(SkyRenderer skyRenderer);

	public ChunksRenderer getChunksRenderer();

	public EntitiesRenderer getEntitiesRenderer();

	public interface EntitiesRenderer {

		public int renderEntities(RenderingInterface renderingContext);
	}

	public FarTerrainRenderer getFarTerrainRenderer();

	/** Takes charge of rendering distant region summaries using only 2D data */
	public interface FarTerrainRenderer {

		/**
		 * Tells the far terrain renderer to trash it's (mesh) data and to rebuild it
		 * anew
		 */
		public default void markFarTerrainMeshDirty() {
			// Only relevant if the far terrain actually meshes anything on the CPU
		}

		/** Renders the terrain using this renderer and an optional mask */
		public void renderTerrain(RenderingInterface renderer, ReadyVoxelMeshesMask mask);

		public default void destroy() {
			// You should free whatever ressources you use there
		}

		/**
		 * Stops the far terrain system to draw what is already shown using actual voxel
		 * data
		 */
		public interface ReadyVoxelMeshesMask {

			/**
			 * Default implementations of the FarTerrainRenderer subdivide each region in
			 * 8x8 slabs that coincide with the world data chunks, and that have a min/max
			 * height parameter pre-computed. This methods allows to check whether or not
			 * that data is present. You don't have to implement it, this interface is
			 * reference only.
			 */
			public boolean shouldMaskSlab(int chunkX, int chunkZ, int min, int max);
		}
	}

	public SummariesTexturesHolder getSummariesTexturesHolder();

	/**
	 * Takes charge of keeping the summaries data in VRAM and gives you an index to
	 * them Hint: Array Textures.
	 */
	interface SummariesTexturesHolder {

		/**
		 * Returns -1 if the summary isn't available in VRAM, an index between 0 and 80
		 * (included) if it is
		 */
		public int getSummaryIndex(int regionX, int regionZ);

		/** Notifies new data is available for the following region */
		public void warnDataHasArrived(int regionX, int regionZ);

		public ArrayTexture getHeightsArrayTexture();

		public ArrayTexture getTopVoxelsArrayTexture();

		public void destroy();
	}

	public GameWindow getWindow();

	public DecalsRenderer getDecalsRenderer();

	public ParticlesRenderer getParticlesRenderer();

	public WorldEffectsRenderer getWorldEffectsRenderer();

	/**
	 * Warning: Most methods of the RenderingInterface are intended to be run only
	 * from the main thread. Don't play with this until you know what you are doing
	 * !
	 */
	public RenderingInterface getRenderingInterface();

	public String screenShot();
}
