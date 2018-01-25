package io.xol.chunkstories.api.particles;

import org.joml.Vector3f;

import io.xol.chunkstories.api.physics.CollisionBox;
import io.xol.chunkstories.api.rendering.RenderingInterface;
import io.xol.chunkstories.api.rendering.pipeline.PipelineConfiguration.BlendMode;
import io.xol.chunkstories.api.rendering.pipeline.PipelineConfiguration.CullingMode;
import io.xol.chunkstories.api.rendering.textures.Texture2D;
import io.xol.chunkstories.api.rendering.pipeline.ShaderInterface;
import io.xol.chunkstories.api.world.VoxelContext;
import io.xol.chunkstories.api.world.World;

//(c) 2015-2017 XolioWare Interactive
//http://chunkstories.xyz
//http://xol.io

/** Determines how a specific particle type should be handled, what type of metadata to keep for each particle, how to render them etc. */
public abstract class ParticleTypeHandler {

	private final ParticleTypeDefinition type;
	
	public ParticleTypeHandler(ParticleTypeDefinition type)
	{
		this.type = type;
	}
	
	public ParticleTypeDefinition getType()
	{
		return type;
	}
	
	public String getName()
	{
		return type.getName();
	}
	
	/**
	 * Particle data is at least a vector3f
	 */
	public class ParticleData extends Vector3f
	{
		boolean ded = false;
		
		public ParticleData(float x, float y, float z)
		{
			super(x, y, z);
		}
		
		public void destroy()
		{
			ded = true;
		}
		
		public boolean isDed()
		{
			return ded;
		}
		
		/** Helper method for particles to check their collisions efficiently and concisely */
		public boolean isCollidingAgainst(World world) {
			return isCollidingAgainst(world, x, y, z);
		}

		/** Helper method for particles to check their collisions efficiently and concisely */
		public boolean isCollidingAgainst(World world, float x, float y, float z) {
			
			VoxelContext peek = world.peekSafely((int)x, (int)y, (int)z);
			
			if (peek.getVoxel().getDefinition().isSolid())
			{
				//Fast check if the voxel is just a solid block
				//TODO isOpaque doesn't mean that exactly, create a new type variable that represents that specific trait
				if (peek.getVoxel().getDefinition().isOpaque())
					return true;
				
				//Else iterate over each box that make up that block
				CollisionBox[] boxes = peek.getVoxel().getTranslatedCollisionBoxes(peek);
				if (boxes != null)
					for (CollisionBox box : boxes)
						if (box.isPointInside(x, y, z))
							return true;

			}
			return false;
		}
	}
	
	public ParticleData createNew(World world, float x, float y, float z)
	{
		return new ParticleData(x, y, z);
	}
	
	public abstract void forEach_Physics(World world, ParticleData data);
	
	public abstract class ParticleTypeRenderer {
		protected final ParticlesRenderer particlesRenderer;
		private final Texture2D albedoTexture;
		private final Texture2D normalTexture;
		private final Texture2D materialTexture;
		
		public ParticleTypeRenderer(ParticlesRenderer particlesRenderer) {
			this.particlesRenderer = particlesRenderer;
			
			//Get those at initialization of de renderer
			albedoTexture   = type.getAlbedoTexture() != null   ? particlesRenderer.getContent().textures().getTexture(type.getAlbedoTexture())   :  particlesRenderer.getContent().textures().nullTexture();
			normalTexture   = type.getNormalTexture() != null   ? particlesRenderer.getContent().textures().getTexture(type.getNormalTexture())   :  particlesRenderer.getContent().textures().nullTexture();
			materialTexture = type.getMaterialTexture() != null ? particlesRenderer.getContent().textures().getTexture(type.getMaterialTexture()) :  particlesRenderer.getContent().textures().nullTexture();
		}
		
		public Texture2D getAlbedoTexture() 
		{
			return albedoTexture;
		}
		
		public Texture2D getNormalTexture()
		{
			return normalTexture;
		}

		public Texture2D getMaterialTexture()
		{
			return materialTexture;
		}
		
		public void beginRenderingForType(RenderingInterface renderingInterface)
		{
			renderingInterface.setCullingMode(CullingMode.DISABLED);
			renderingInterface.setBlendMode(BlendMode.MIX);
			ShaderInterface particlesShader = renderingInterface.useShader(type.getShaderName());
			particlesShader.setUniform2f("screenSize", renderingInterface.getWindow().getWidth(), renderingInterface.getWindow().getHeight());
			renderingInterface.getCamera().setupShader(particlesShader);
			renderingInterface.bindTexture2D("lightColors", particlesRenderer.getContent().textures().getTexture("./textures/environement/light.png"));
			
			renderingInterface.bindAlbedoTexture(getAlbedoTexture());
			renderingInterface.currentShader().setUniform1f("billboardSize", type.getBillboardSize());
			//TODO refactor this crappy class
			renderingInterface.bindNormalTexture(particlesRenderer.getContent().textures().getTexture("./textures/normalnormal.png"));
		}

		/** Called at each iteration, on the rendering thread. */
		public abstract void forEach_Rendering(RenderingInterface renderingInterface, ParticleData data);

		/** You must free any non-auto destructing graphics objects here. Freeing up textures and models is a nice touch. */
		public abstract void destroy();
	}
	
	public abstract ParticleTypeRenderer getRenderer(ParticlesRenderer particlesRenderer);
}
