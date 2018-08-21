//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.net;

import io.xol.chunkstories.api.world.serialization.StreamTarget;

/** Someone we can send packets to */
public interface PacketDestinator extends StreamTarget {
	public void pushPacket(Packet packet);

	public void flush();

	public void disconnect();

	public void disconnect(String disconnectionReason);
}
