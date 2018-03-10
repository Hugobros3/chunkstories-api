//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering;

/** Offers a platform-agnostic simplification of the main OpenGL state */
public interface StateMachine
{
	public static enum DepthTestMode {
		DISABLED, GREATER, GREATER_OR_EQUAL, EQUAL, LESS_OR_EQUAL, LESS, ALWAYS;
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
	
	public DepthTestMode getDepthTestMode();

	public BlendMode getBlendMode();

	public CullingMode getCullingMode();
	
	public PolygonFillMode getPolygonFillMode();
	
	public void setDepthTestMode(DepthTestMode depthTestMode);
	
	public void setCullingMode(CullingMode cullingMode);

	public void setBlendMode(BlendMode blendMode);

	public void setPolygonFillMode(PolygonFillMode polygonFillMode);
}