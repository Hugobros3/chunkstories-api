//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import xyz.chunkstories.api.exceptions.PacketProcessingException;

/** Packets are atomic data bits used to communicate over the network */
public abstract class Packet {
	public Packet() {

	}

	/** Called at send time, be consistent with the data you send, and give yourself
	 * a way to know how many bytes to expect if it's a variable length */
	public abstract void send(PacketDestinator destinator, DataOutputStream out, PacketSendingContext context) throws IOException;

	/** Called at reception, has to read the exact number of bytes sent or bad stuff
	 * happens ! */
	public abstract void process(PacketSender sender, DataInputStream in, PacketReceptionContext context) throws IOException, PacketProcessingException;
}
