//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.content;

import xyz.chunkstories.api.net.PacketDefinition;

import javax.annotation.Nullable;

/** Additional bindings when connected to a server. */
public interface OnlineContentTranslator extends ContentTranslator {

	/** Return the assignated ID for this declaration or -1 if it isn't a part of
	 * the current content */
	public int getIdForPacket(PacketDefinition definition);

	// public int getIdForPacket(Packet packet); @see Content

	/** Return the PacketDefinition associated with that ID or null if the ID was
	 * outside of bounds */
	@Nullable
	public PacketDefinition getPacketForId(int id);
}
