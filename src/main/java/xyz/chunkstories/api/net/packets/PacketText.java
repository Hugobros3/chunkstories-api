//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.net.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import xyz.chunkstories.api.net.Packet;
import xyz.chunkstories.api.net.PacketDestinator;
import xyz.chunkstories.api.net.PacketSender;
import xyz.chunkstories.api.net.PacketSendingContext;
import xyz.chunkstories.api.net.PacketReceptionContext;

public class PacketText extends Packet {
	public String text;

	public PacketText() {

	}

	public PacketText(String text) {
		this.text = text;
	}

	@Override
	public void send(PacketDestinator destinator, DataOutputStream out, PacketSendingContext context) throws IOException {
		out.writeUTF(text);
	}

	public void process(PacketSender sender, DataInputStream in, PacketReceptionContext context) throws IOException {
		text = in.readUTF();

		// Actual handling is left to internal code defined in proprietary subclasses
		// that expose the inner workings
		// ( see clientClass and serverClass within .packets files )
	}
}
