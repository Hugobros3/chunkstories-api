//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.pipeline;

import io.xol.chunkstories.api.rendering.RenderingInterface;

//TODO rename to something more meaningfull
public interface PipelineConfiguration
{
	public DepthTestMode getDepthTestMode();

	public BlendMode getBlendMode();

	public CullingMode getCullingMode();
	
	public PolygonFillMode getPolygonFillMode();

	public static enum DepthTestMode {
		DISABLED, GREATER, GREATER_OR_EQUAL, EQUAL, LESS_OR_EQUAL, LESS;
	}
	
	public static enum BlendMode {
		DISABLED, ADD, MIX, PREMULT_ALPHA;
	}
	
	public static enum CullingMode {
		DISABLED, CLOCKWISE, COUNTERCLOCKWISE;
	}
	
	public static enum PolygonFillMode {
		FILL, WIREFRAME, POINTS;
	}

	public void setup(RenderingInterface renderingInterface);
}