//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.net.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import xyz.chunkstories.api.exceptions.PacketProcessingException;
import xyz.chunkstories.api.net.Packet;
import xyz.chunkstories.api.net.PacketDestinator;
import xyz.chunkstories.api.net.PacketSender;
import xyz.chunkstories.api.net.PacketSendingContext;
import xyz.chunkstories.api.net.PacketReceptionContext;

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
