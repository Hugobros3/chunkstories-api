//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.net.packets;

import io.xol.chunkstories.api.net.PacketDestinator;
import io.xol.chunkstories.api.net.PacketSender;
import io.xol.chunkstories.api.net.PacketSendingContext;
import io.xol.chunkstories.api.net.PacketWorld;
import io.xol.chunkstories.api.world.World;
import io.xol.chunkstories.api.net.PacketReceptionContext;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/** The server just tells the time */
public class PacketTime extends PacketWorld {
	public long time;
	public float overcastFactor;

	public PacketTime(World world) {
		super(world);
	}

	@Override
	public void send(PacketDestinator destinator, DataOutputStream out, PacketSendingContext context) throws IOException {
		out.writeLong(time);
		out.writeFloat(overcastFactor);
	}

	public void process(PacketSender sender, DataInputStream in, PacketReceptionContext context) throws IOException {
		time = in.readLong();
		overcastFactor = in.readFloat();

		if (!context.isServer() && world != null) {
			world.setTime(time);
			world.setWeather(overcastFactor);
		}
	}

}
