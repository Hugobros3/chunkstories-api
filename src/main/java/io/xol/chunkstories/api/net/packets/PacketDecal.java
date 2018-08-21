//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.net.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.joml.Vector3d;
import org.joml.Vector3dc;

import io.xol.chunkstories.api.client.net.ClientPacketsProcessor;
import io.xol.chunkstories.api.exceptions.PacketProcessingException;
import io.xol.chunkstories.api.net.PacketDestinator;
import io.xol.chunkstories.api.net.PacketReceptionContext;
import io.xol.chunkstories.api.net.PacketSender;
import io.xol.chunkstories.api.net.PacketSendingContext;
import io.xol.chunkstories.api.net.PacketWorld;
import io.xol.chunkstories.api.world.World;

/** Simply sends a decal to the client to be drawn */
public class PacketDecal extends PacketWorld {
	private String decalName;
	private Vector3dc position;
	private Vector3dc orientation;
	private Vector3dc size;

	public PacketDecal(World world) {
		super(world);
	}

	public PacketDecal(World world, String decalName, Vector3dc position, Vector3dc orientation, Vector3dc size) {
		super(world);
		this.decalName = decalName;
		this.position = position;
		this.orientation = orientation;
		this.size = size;
	}

	@Override
	public void send(PacketDestinator destinator, DataOutputStream out, PacketSendingContext context) throws IOException {
		out.writeUTF(decalName);
		out.writeDouble(position.x());
		out.writeDouble(position.y());
		out.writeDouble(position.z());
		out.writeDouble(orientation.x());
		out.writeDouble(orientation.y());
		out.writeDouble(orientation.z());
		out.writeDouble(size.x());
		out.writeDouble(size.y());
		out.writeDouble(size.z());
	}

	@Override
	public void process(PacketSender sender, DataInputStream in, PacketReceptionContext processor) throws IOException, PacketProcessingException {
		decalName = in.readUTF();
		Vector3d position = new Vector3d();
		position.x = (in.readDouble());
		position.y = (in.readDouble());
		position.z = (in.readDouble());

		Vector3d orientation = new Vector3d();
		orientation.x = (in.readDouble());
		orientation.y = (in.readDouble());
		orientation.z = (in.readDouble());

		Vector3d size = new Vector3d();
		size.x = (in.readDouble());
		size.y = (in.readDouble());
		size.z = (in.readDouble());

		if (processor instanceof ClientPacketsProcessor) {
			ClientPacketsProcessor cpp = (ClientPacketsProcessor) processor;
			cpp.getContext().getDecalsManager().drawDecal(position, orientation, size, decalName);
		}
	}
}
