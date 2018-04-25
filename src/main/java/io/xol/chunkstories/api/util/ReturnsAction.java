package io.xol.chunkstories.api.util;

public interface ReturnsAction<T, RETURN_TYPE> {
	public RETURN_TYPE run(T object);
}
