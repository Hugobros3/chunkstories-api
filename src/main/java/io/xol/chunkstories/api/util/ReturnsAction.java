//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.util;

public interface ReturnsAction<T, RETURN_TYPE> {
	public RETURN_TYPE run(T object);
}
