//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.net;

import xyz.chunkstories.api.world.serialization.StreamTarget;

/** Someone we can send packets to */
public interface PacketDestinator extends StreamTarget {
	public void pushPacket(Packet packet);

	public void flush();

	public void disconnect();

	public void disconnect(String disconnectionReason);
}
