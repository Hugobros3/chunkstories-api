//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.particles;

import io.xol.chunkstories.api.content.Content;
import io.xol.chunkstories.api.content.Definition;

public interface ParticleTypeDefinition extends Definition
{
	public String getName();
	
	public Content.ParticlesTypes store();
	
	///** When should we render those particle types */
	//public enum RenderTime {
	//	/** Actually iterated anyways! Use case is light particles, mostly. */
	//	NEVER,
	//	/** Done after the rest of the opaque GBuffer stuff. */
	//	GBUFFER,
	//	FORWARD
	//	;
	//}
	
	public enum ParticleRenderingMode {
		BILLBOARD;
	}
	
	/** Returns the name of the render pass we should use to render those particles. */ 
	public abstract String getRenderPass();
	
	public default String getShaderName()
	{
		return "particles";
	}
	
	/** Returns null or a path to an asset. */
	public abstract String getAlbedoTexture();
	
	/** Returns null or a path to an asset. */
	public abstract String getNormalTexture();

	/** Returns null or a path to an asset. */
	public abstract String getMaterialTexture();
	
	/** Defaults to 1.0f */
	public abstract float getBillboardSize();
}
