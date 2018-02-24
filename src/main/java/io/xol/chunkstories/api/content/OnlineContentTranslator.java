//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.content;

import io.xol.chunkstories.api.net.PacketDefinition;

/** Additional bindings when connected to a server. */
public interface OnlineContentTranslator extends ContentTranslator {
	
	/** Return the assignated ID for this definition or -1 if it isn't a part of the current content */
	public int getIdForPacket(PacketDefinition definition);
	
	//public int getIdForPacket(Packet packet); @see Content

	/** Return the PacketDefinition associated with that ID or null if the ID was outside of bounds */
	public PacketDefinition getPacketForId(int id);
}
