//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering;

import org.joml.Matrix4f;

import io.xol.chunkstories.api.client.ClientContent.ShadersLibrary;
import io.xol.chunkstories.api.client.ClientContent.TexturesLibrary;
import io.xol.chunkstories.api.client.ClientInterface;
import io.xol.chunkstories.api.exceptions.rendering.AttributeNotPresentException;
import io.xol.chunkstories.api.exceptions.rendering.InvalidShaderException;
import io.xol.chunkstories.api.exceptions.rendering.ShaderCompileException;
import io.xol.chunkstories.api.rendering.StateMachine.BlendMode;
import io.xol.chunkstories.api.rendering.StateMachine.CullingMode;
import io.xol.chunkstories.api.rendering.StateMachine.DepthTestMode;
import io.xol.chunkstories.api.rendering.StateMachine.PolygonFillMode;
import io.xol.chunkstories.api.rendering.lightning.Light;
import io.xol.chunkstories.api.rendering.mesh.ClientMeshLibrary;
import io.xol.chunkstories.api.rendering.pass.RenderPass;
import io.xol.chunkstories.api.rendering.shader.Shader;
import io.xol.chunkstories.api.rendering.target.RenderTargets;
import io.xol.chunkstories.api.rendering.text.FontRenderer;
import io.xol.chunkstories.api.rendering.textures.ArrayTexture;
import io.xol.chunkstories.api.rendering.textures.Cubemap;
import io.xol.chunkstories.api.rendering.textures.Texture1D;
import io.xol.chunkstories.api.rendering.textures.Texture2D;
import io.xol.chunkstories.api.rendering.textures.Texture2DRenderTarget;
import io.xol.chunkstories.api.rendering.textures.Texture3D;
import io.xol.chunkstories.api.rendering.textures.TextureFormat;
import io.xol.chunkstories.api.rendering.vertex.AttributeSource;
import io.xol.chunkstories.api.rendering.vertex.AttributesConfiguration;
import io.xol.chunkstories.api.rendering.vertex.VertexBuffer;
import io.xol.chunkstories.api.rendering.world.WorldRenderer;

public interface RenderingInterface
{
	public CameraInterface getCamera();

	public ClientInterface getClient();
	
	public GameWindow getWindow();
	
	public RenderTargets getRenderTargetManager();
	
	/* Shaders */
	
	public ShadersLibrary shaders();
	
	public Shader useShader(String shaderName) throws InvalidShaderException, ShaderCompileException;
	
	public Shader currentShader();
	
	/* Texturing configuration */
	
	public void bindAlbedoTexture(Texture2D texture);
	
	public void bindNormalTexture(Texture2D texture);
	
	public void bindMaterialTexture(Texture2D texture);

	public void bindTexture1D(String textureSamplerName, Texture1D texture);
	
	public void bindTexture2D(String textureSamplerName, Texture2D texture);
	
	public void bindTexture3D(String textureSamplerName, Texture3D texture);
	
	public void bindCubemap(String cubemapSamplerName, Cubemap cubemapTexture);
	
	public void bindArrayTexture(String textureSamplerName, ArrayTexture texture);

	public TexturesLibrary textures();
	
	public Texture2DRenderTarget newTexture2D(TextureFormat type, int width, int height);
	
	public Texture3D newTexture3D(TextureFormat type, int width, int height, int depth);
	
	/* Object location & Instance Data */
	
	/** returns the current object matrix */
	public Matrix4f getObjectMatrix();
	
	/** Feeds the 'objectMatrix' and 'objectMatrixNormal' shader inputs ( either uniform or texture-based instanced if shader has support ) */
	public Matrix4f setObjectMatrix(Matrix4f objectMatrix);
	
	///** Feeds the 'worldLight' shader inputs ( either uniform or texture-based instanced if shader has support ) */
	//public void setWorldLight(int sunLight, int blockLight);
	
	/* Pipeline configuration */
	
	/** @return The current PipelineConfiguration */
	public StateMachine getStateMachine();

	public void setDepthTestMode(DepthTestMode depthTestMode);
	
	public void setCullingMode(CullingMode cullingMode);

	public void setBlendMode(BlendMode blendMode);

	public void setPolygonFillMode(PolygonFillMode polygonFillMode);
	
	/* Attributes */
	
	/**
	 * Returns the configuration of the bound vertex shader inputs
	 */
	//public AttributesConfiguration getAttributesConfiguration();
	
	/** If attributeSource != null, setups the currently bound vertex shader attribute input 'attributeName' with it
	 * If attibuteSource == null, disables the shader input 'attributeName'
	 * @throws AttributeNotPresentException If 'attributeName' doesn't resolve to a real attribute location
	 * Returns the configuration of the bound vertex shader inputs
	 */
	public AttributesConfiguration bindAttribute(String attributeName, AttributeSource attributeSource) throws AttributeNotPresentException;
	
	/** Ensures no attributes are bound left over from previous draw instructions */
	public AttributesConfiguration unbindAttributes();
	
	public ClientMeshLibrary meshes();
	
	public VertexBuffer newVertexBuffer();
	
	/**
	 * Draws N primitives made of 'count' vertices, offset at vertice 'startAt', using data specified in the AttributesConfiguration
	 */
	public void draw(Primitive primitive, int startAt, int count);
	
	/** For instanced rendering */
	public void draw(Primitive primitive, int startAt, int count, int instances);
	
	/** Equivalent to glMultiDrawArrays */
	public void drawMany(Primitive primitive, int... startAndCountPairs);

	/** Renders a fullsize quad for whole-screen effects */
	public void drawFSQuad();
	
	/**
	 * Executes ALL commands in the queue up to this point before continuing
	 */
	//public void flush();
	
	/* Statistics */
	
	public default long getTotalVramUsage()
	{
		return getVertexDataVramUsage() + getTextureDataVramUsage();
	}
	
	public long getVertexDataVramUsage();
	
	public long getTextureDataVramUsage();
	
	/* Specific renderers / helpers */

	public GuiRenderer getGuiRenderer();

	public FontRenderer getFontRenderer();
	
	public WorldRenderer getWorldRenderer();

	/** Shortcut to getWorldRenderer().renderPasses().getcurrentPass() */
	public RenderPass getCurrentPass();
	
	public LightsAccumulator getLightsRenderer();
	
	interface LightsAccumulator {
		public void queueLight(Light light);

		public void renderPendingLights(RenderingInterface renderingContext);
	}
}
