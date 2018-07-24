//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.rendering.vertex;

import java.nio.ByteBuffer;

/**
 * Some ByteBuffer wrapped in a class that requires to be recycled after use
 * (for pooled or explicitly allocated memory )
 */
public interface RecyclableByteBuffer {
	public ByteBuffer accessByteBuffer();

	public void recycle();
}