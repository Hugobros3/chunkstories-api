//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.particles;

import io.xol.chunkstories.api.client.ClientContent;
import io.xol.chunkstories.api.client.ClientInterface;
import io.xol.chunkstories.api.rendering.RenderingInterface;

/** Only the client actually renders particles */
public interface ParticlesRenderer extends ParticlesManager
{
	public ClientInterface getClient();
	
	public ClientContent getContent();
	
	/** Internal to the engine */
	public void renderParticles(RenderingInterface renderingInterface);
}
