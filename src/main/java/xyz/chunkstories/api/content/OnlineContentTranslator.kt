//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.content

import xyz.chunkstories.api.net.PacketDefinition

/** Additional bindings when connected to a server.  */
interface OnlineContentTranslator : ContentTranslator {

    /** Return the assignated ID for this declaration or -1 if it isn't a part of
     * the current content  */
    fun getIdForPacket(definition: PacketDefinition): Int

    // public int getIdForPacket(Packet packet); @see Content

    /** Return the PacketDefinition associated with that ID or null if the ID was
     * outside of bounds  */
    fun getPacketForId(id: Int): PacketDefinition?
}
