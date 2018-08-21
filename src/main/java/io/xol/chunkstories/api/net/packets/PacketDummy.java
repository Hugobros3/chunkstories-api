//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.net.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.xol.chunkstories.api.exceptions.PacketProcessingException;
import io.xol.chunkstories.api.net.Packet;
import io.xol.chunkstories.api.net.PacketDestinator;
import io.xol.chunkstories.api.net.PacketSender;
import io.xol.chunkstories.api.net.PacketSendingContext;
import io.xol.chunkstories.api.net.PacketReceptionContext;

/** A dummy packet has no header, nor any particular meaning. It is expected to
 * contain some header information in the data so the other end knows what it is
 * supposed to be. Usefull to spoof other packets and do various forms of
 * hackery */
public class PacketDummy extends Packet {
	public byte[] data;

	@Override
	public void send(PacketDestinator destinator, DataOutputStream out, PacketSendingContext context) throws IOException {
		out.write(data);
	}

	@Override
	public void process(PacketSender sender, DataInputStream in, PacketReceptionContext processor) throws IOException, PacketProcessingException {
		throw new UnsupportedOperationException();
	}

}
