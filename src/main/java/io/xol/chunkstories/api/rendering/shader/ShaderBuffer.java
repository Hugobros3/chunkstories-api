package io.xol.chunkstories.api.rendering.shader;

import java.nio.ByteBuffer;

/** Represents the interface to both an UBO and SSBO */
public interface ShaderBuffer {

	void upload(ByteBuffer data);

	void destroy();
}